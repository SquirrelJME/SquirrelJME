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
import java.io.IOException;
import java.io.InputStream;
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
	/** The control used to emit MIDI sounds. */
	protected final MIDIControl midiControl;
	
	/** The un-realized input stream. */
	protected volatile InputStream _unrealizedIn;
	
	/** The MIDI track data. */
	protected volatile byte[] _data;
	
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
		// Load more information about the MIDI, such as its length
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
	public long getDuration()
	{
		throw Debugging.todo();
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
