// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.AbstractMidiControl;
import cc.squirreljme.runtime.media.AbstractPlayer;
import cc.squirreljme.runtime.media.AbstractVolumeControl;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.MIDIControl;

/**
 * This is used to play MIDIs.
 *
 * @since 2022/04/24
 */
@SquirrelJMEVendorApi
public class MidiPlayer
	extends AbstractPlayer
{
	/** Magic number for MThd. */
	private static final int MTHD_MAGIC =
		0x4D546864;
	
	/** Magic number for MTrk. */
	private static final int MTRK_MAGIC =
		0x4D54726B;
	
	/** The tracker which plays MIDIs, one at a time. */
	private static volatile MidiTracker _TRACKER;
	
	/** The control used to emit MIDI sounds. */
	@SquirrelJMEVendorApi
	protected final AbstractMidiControl midiControl;
	
	/** The MIDI player this is using. */
	@SquirrelJMEVendorApi
	protected final Player midiPlayer;
	
	/** The timing that is shared for all MIDI tracks. */
	volatile MidiTimeDiv _timeDiv;
	
	/** The MIDI track data. */
	private volatile byte[] _data;
	
	/** Tracks within the MIDI. */
	private volatile MTrkParser[] _tracks;
	
	/** The un-realized input stream. */
	private volatile InputStreamConnection _unrealizedIn;
	
	/** The cached nanosecond duration. */
	private volatile long _nanoDuration;

	/** master Volume for MIDI notes. */
	@SquirrelJMEVendorApi
	volatile MidiVolume _volume;
	
	/**
	 * Initializes the MIDI player.
	 * 
	 * @param __in The stream to source from.
	 * @throws IOException On read errors.
	 * @throws MediaException If the player could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	public MidiPlayer(InputStreamConnection __in)
		throws IOException, MediaException, NullPointerException
	{
		super("audio/midi");
		
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// We need a player to emit the MIDI events to
		Player midiPlayer = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR);
		this.midiPlayer = midiPlayer;
		this.midiControl = (AbstractMidiControl)midiPlayer.getControl(
			MIDIControl.class.getName());

		this._volume = new MidiVolume((byte) 100);
		
		// For later realization
		this._unrealizedIn = __in;
		
		// Register the MIDI controller
		this.registerControl(this.midiControl);
		this.registerControl(new AbstractVolumeControl(this));
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public void becomingDeallocated()
		throws MediaException
	{
		this._data = null;
		this._tracks = null;
		
		// Close the input connection, if it was never read in
		InputStreamConnection unrealizedIn = this._unrealizedIn;
		if (unrealizedIn != null)
		{
			this._unrealizedIn = null;
			AbstractPlayer.closeConnection(unrealizedIn);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	protected void becomingPrefetched()
		throws MediaException
	{
		// Make sure the MIDI player is started
		this.midiPlayer.start();
		
		// Tracks that are loaded
		List<MTrkParser> tracks = new ArrayList<>();
		
		// Tick Division that was calculated
		int tickDiv = 0;
		long nanosPerTickDiv = 0;
		MidiTimeDiv timeDiv = null;
		
		// Load more information about the MIDI, such as its length along with
		// all the track information
		byte[] data = this._data;
		try (DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(data)))
		{
			// Loop to read all chunks
			for (int filePos = 0;;)
			{
				// Parse magic number and length for chunk type
				int magic;
				int length;
				try
				{
					magic = in.readInt();
					length = in.readInt();
				}
				catch (EOFException ignored)
				{
					break;
				}
				
				// {@squirreljme.error EA0j Invalid MIDI chunk length.
				// (The length)}.
				if (length < 0)
					throw new MediaException("EA0j " + length);
				
				// This is the actual file position now
				filePos += 8;
				
				// Depends on the chunk type
				switch (magic)
				{
						// MIDI header, it just gets parsed
					case MidiPlayer.MTHD_MAGIC:
						try (DataInputStream header = new DataInputStream(
							new ByteArrayInputStream(data, filePos, length)))
						{
							// {@squirreljme.error EA0k Unsupported MIDI
							// format, only format 0 and 1 are supported.
							// (The format)} 
							int format = header.readUnsignedShort();
							if (format != 0 && format != 1)
								throw new MediaException("EA0k " + format);
							
							// Ignore number of tracks
							header.readUnsignedShort();
							
							// Determine tick division.. either SMPTE or ppqn
							tickDiv = header.readUnsignedShort();
							nanosPerTickDiv =
								MidiPlayer.calculateTickDiv(tickDiv);
							
							// Store for later
							timeDiv = new MidiTimeDiv(
								tickDiv, nanosPerTickDiv);
							this._timeDiv = timeDiv;
						}
						break;
						
						// MIDI track, just store where the track is since
						// it just contains events
					case MidiPlayer.MTRK_MAGIC:
						tracks.add(new MTrkParser(data, filePos, length,
							timeDiv, this._volume));
						break;
					
						// Ignore unknown chunks
					default:
						break;
				}
				
				// Move file position up by length
				filePos += length;
				in.skipBytes(length);
			}
		}
		catch (IOException e)
		{
			// {@squirreljme.error EA0i Could not parse MIDI file.}
			MediaException toss = new MediaException("EA0i");
			toss.initCause(e);
			throw toss;
		}
		
		// Finalize tracks
		this._tracks = tracks.toArray(new MTrkParser[tracks.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected void becomingPrimed()
		throws MediaException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	protected void becomingRealized()
		throws MediaException
	{
		try
		{
			// If data is already here, stop
			byte[] data = this._data;
			if (data != null)
				return;
			
			// Otherwise 
			synchronized (this)
			{
				// Double check?
				data = this._data;
				if (data != null)
					return;
				
				// Data is already destroyed?
				if (this._unrealizedIn == null)
					throw new MediaException("GONE");
				
				// Read in the data and drop the unrealized stream
				try (InputStream in = this._unrealizedIn.openInputStream())
				{
					this._data = StreamUtils.readAll(in);
				}
			}
		}
		catch (IOException e)
		{
			// {@squirreljme.error EA0f Failed to realize MIDI data.}
			MediaException toss = new MediaException("EA0f");
			toss.initCause(e);
			throw toss;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected void becomingSolvent()
		throws MediaException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	protected boolean becomingStarted()
		throws MediaException
	{
		// We just need to set up the tracker
		MidiPlayer.__createTracker(this, this._timeDiv, this._volume);
		
		// Do set the new state
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/27
	 */
	@Override
	protected void becomingStopped()
		throws MediaException
	{
		synchronized (MidiPlayer.class)
		{
			// Trying to stop something that was already stopped?
			MidiTracker tracker = MidiPlayer._TRACKER;
			if (tracker == null || tracker.player != this)
				return;
				
			// Indicate to stop
			tracker.stopPlayback = true;
			
			// Wake it up, if it is sleeping
			synchronized (tracker)
			{
				tracker.notifyAll();
				tracker.interrupt();
			}
			
			// Remove it, when interrupted and stopped the thread will clean
			// itself up
			MidiPlayer._TRACKER = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected void clockFastForward(long __micros)
		throws MediaException
	{
		synchronized (MidiPlayer.class)
		{
			MidiTracker tracker = MidiPlayer._TRACKER;
			if (tracker != null)
				tracker.fastForward(__micros);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/15
	 */
	@Override
	protected long clockGet()
	{
		synchronized (MidiPlayer.class)
		{
			MidiTracker tracker = MidiPlayer._TRACKER;
			if (tracker != null)
				return tracker.micros();
			return Player.TIME_UNKNOWN;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/15
	 */
	@Override
	protected void clockSet(long __micros)
		throws MediaException
	{
		throw new MediaException("FAST");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/25
	 */
	@Override
	protected long determineDuration()
		throws MediaException
	{
		// MIDI needs to be prefetched first, so we know the track and MIDI
		// header details
		this.prefetch();
		
		/* Has this already been calculated? */
		long nanoDuration = this._nanoDuration;
		if (nanoDuration > 0)
			return nanoDuration;
		
		// The length of the MIDI is the duration of the longest track
		for (MTrkParser track : this._tracks)
			nanoDuration = Math.max(nanoDuration, track.duration());
		
		// Use the duration of the highest track
		this._nanoDuration = nanoDuration;
		return nanoDuration / 1_000L;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected boolean resetFastForward()
	{
		// This is a tracker based format
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/15
	 */
	@Override
	protected void useVolume(int __volume)
	{
		synchronized (MidiPlayer.class)
		{
			this._volume._value = (byte) __volume;
		}
	}
	
	/**
	 * Creates a MIDI tracker.
	 * 
	 * @return The resultant MIDI tracker.
	 * @param __timeDiv The MIDI time division, shared by all tracks.
	 * @throws MediaException If the tracker cannot be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/27
	 */
	private static MidiTracker __createTracker(MidiPlayer __player,
		MidiTimeDiv __timeDiv, MidiVolume __volume)
		throws MediaException, NullPointerException
	{
		if (__player == null || __timeDiv == null)
			throw new NullPointerException("NARG");
		
		synchronized (MidiPlayer.class)
		{
			// Stop an existing track from playing, if any
			MidiTracker tracker = MidiPlayer._TRACKER;
			if (tracker != null)
				tracker.player.stop();
			
			// Setup new tracker
			tracker = new MidiTracker(__player, __player._tracks, __timeDiv,
				__volume);
			
			// Make sure it is a daemon thread, so it gets killed on exit
			ThreadShelf.javaThreadSetDaemon(tracker);

			// Start it, yay!
			tracker.start();
			
			// Use this tracker
			MidiPlayer._TRACKER = tracker;
			return tracker;
		}
	}
	
	/**
	 * Calculates the nanoseconds per tick-division.
	 * 
	 * My wonderful spouse who is a musical technical artist helped me out
	 * with this.
	 *
	 * @param __rawTickDiv The raw tick division from the header.
	 * @return The nanoseconds per tick division.
	 * @since 2026/01/01
	 */
	@SquirrelJMEVendorApi
	public static long calculateTickDiv(int __rawTickDiv)
	{
		// Determine tick division.. either SMPTE or ppqn
		if ((__rawTickDiv & 0x8000) != 0)
		{
			// Parse values
			int frames = -((byte)(__rawTickDiv >>> 8));
			int subFrames = (byte)__rawTickDiv;
			
			// Is essentially frames and subframes per
			// second
			return 1_000_000__000 / (frames * subFrames);
		}
		
		// Reversed value from 120 BPM with 24 PPQN
		// which was 0.02083s. Reversing operation with
		// multiplication gives 0.5s so this is used as
		// the base.
		/*if (true)
			return 60_000__000L / (24L * __rawTickDiv);*/
		return 500_000__000 / __rawTickDiv;
	}
}
