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
	
	/** The number of nanoseconds per tick division. */
	volatile long _nanosPerTickDiv =
		-1;
	
	/** The tick division used. */
	volatile long _tickDiv =
		-1;
	
	/** The MIDI track data. */
	private volatile byte[] _data;
	
	/** Tracks within the MIDI. */
	private volatile MTrkParser[] _tracks;
	
	/** The un-realized input stream. */
	@SquirrelJMEVendorApi
	private volatile InputStreamConnection _unrealizedIn;
	
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
							int tickDiv = header.readUnsignedShort();
							if ((tickDiv & 0x8000) != 0)
							{
								// Parse values
								int frames = -((byte)(tickDiv >>> 8));
								int subFrames = (byte)tickDiv;
								
								// Is essentially frames and subframes per
								// second
								this._nanosPerTickDiv = 1_000_000__000 /
									(frames * subFrames);
								this._tickDiv = 1;
							}
							
							// Reversed value from 120 BPM with 24 PPQN
							// which was 0.02083s. Reversing operation with
							// multiplication gives 0.5s so this is used as
							// the base.
							else
							{
								this._nanosPerTickDiv = 500_000__000 / tickDiv;
								this._tickDiv = tickDiv;
							}
						}
						break;
						
						// MIDI track, just store where the track is since
						// it just contains events
					case MidiPlayer.MTRK_MAGIC:
						tracks.add(new MTrkParser(data, filePos, length));
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
	 *
	 * @return
	 * @since 2022/04/24
	 */
	@Override
	protected boolean becomingStarted()
		throws MediaException
	{
		// We just need to set up the tracker
		MidiPlayer.__createTracker(this);
		
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
		synchronized (MidiPlayer.class)
		{
			MidiTracker tracker = MidiPlayer._TRACKER;
			if (tracker != null)
				tracker.fastForward(__micros);
		}
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
		
		// The length of the MIDI is the duration of the longest track
		int highestTickDivDuration = 0;
		for (MTrkParser track : this._tracks)
			highestTickDivDuration = Math.max(highestTickDivDuration,
				track.tickDivDuration());
		
		// The actual song length is basic multiplication
		return highestTickDivDuration * this._nanosPerTickDiv;
	}
	
	@Override
	protected void useVolume(int __volume)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Creates a MIDI tracker.
	 * 
	 * @return The resultant MIDI tracker.
	 * @throws MediaException If the tracker cannot be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/27
	 */
	private static MidiTracker __createTracker(MidiPlayer __player)
		throws MediaException, NullPointerException
	{
		if (__player == null)
			throw new NullPointerException("NARG");
		
		synchronized (MidiPlayer.class)
		{
			// Stop an existing track from playing, if any
			MidiTracker tracker = MidiPlayer._TRACKER;
			if (tracker != null)
				tracker.player.stop();
			
			// Setup new tracker
			tracker = new MidiTracker(__player, __player._tracks);
			
			// Make sure it is a daemon thread, so it gets killed on exit
			ThreadShelf.javaThreadSetDaemon(tracker);
			
			// Start it, yay!
			tracker.start();
			
			// Use this tracker
			MidiPlayer._TRACKER = tracker;
			return tracker;
		}
	}
}
