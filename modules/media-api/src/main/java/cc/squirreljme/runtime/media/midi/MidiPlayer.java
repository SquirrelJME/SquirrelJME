// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.media.AbstractPlayer;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.MIDIControl;

/**
 * This is used to play MIDIs.
 *
 * @since 2022/04/24
 */
public class MidiPlayer
	extends AbstractPlayer
{
	/** Magic number for MThd. */
	private static final int MTHD_MAGIC =
		0x4D546864;
	
	/** Magic number for MTrk. */
	private static final int MTRK_MAGIC =
		0x4D54726B;
	
	/** The control used to emit MIDI sounds. */
	protected final MIDIControl midiControl;
	
	/** The un-realized input stream. */
	private volatile InputStream _unrealizedIn;
	
	/** The MIDI track data. */
	private volatile byte[] _data;
	
	/** Tracks within the MIDI. */
	private volatile MTrkParser[] _tracks;
	
	/** The number of microseconds per tick division. */
	private volatile long _microsPerTickDiv =
		-1;
	
	/**
	 * Initializes the MIDI player.
	 * 
	 * @param __in The stream to source from.
	 * @throws IOException On read errors.
	 * @throws MediaException If the player could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	public MidiPlayer(InputStream __in)
		throws IOException, MediaException, NullPointerException
	{
		super("audio/midi");
		
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// We need a player to emit the MIDI events to
		Player midiPlayer = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR);
		this.midiControl = (MIDIControl)midiPlayer.getControl(
			MIDIControl.class.getName());
		
		// For later realization
		this._unrealizedIn = __in;
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
				
				// Read in the data and drop the unrealized stream
				this._data = StreamUtils.readAll(this._unrealizedIn);
				this._unrealizedIn = null;
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
	 * @since 2022/04/24
	 */
	@Override
	protected void becomingPrefetched()
		throws MediaException
	{
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
								this._microsPerTickDiv = 1_000_000 /
									(frames * subFrames);
							}
							
							// Reversed value from 120 BPM with 24 PPQN
							// which was 0.02083s. Reversing operation with
							// multiplication gives 0.5s so this is used as
							// the base.
							else
								this._microsPerTickDiv = 500_000 / tickDiv;
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
	protected void becomingStarted()
		throws MediaException
	{
		throw Debugging.todo();
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
		return highestTickDivDuration * this._microsPerTickDiv;
	}
	
	@Override
	public void close()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void deallocate()
	{
		throw Debugging.todo();
	}
	
	@Override
	public Control getControl(String __control)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	public Control[] getControls()
	{
		return new Control[]{this.midiControl};
	}
	
	@Override
	public long getMediaTime()
	{
		throw Debugging.todo();
	}
	
	@Override
	public long setMediaTime(long __now)
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	@Override
	public void stop()
		throws MediaException
	{
		throw Debugging.todo();
	}
}
