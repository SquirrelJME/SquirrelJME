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
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.TimeBase;
import javax.microedition.media.control.MIDIControl;

/**
 * This is used to play MIDIs.
 *
 * @since 2022/04/24
 */
public class MidiPlayer
	implements Player
{
	/** The control used to emit MIDI sounds. */
	protected final Control control;
	
	/** The MIDI track data. */
	protected final byte[] data;
	
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
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// We need a player to emit the MIDI events to
		Player midiPlayer = Manager.createPlayer(Manager.MIDI_DEVICE_LOCATOR);
		this.control = midiPlayer.getControl(MIDIControl.class.getName());
		
		// Read in all of the MIDI data
		this.data = StreamUtils.readAll(__in);
	}
	
	@Override
	public void addPlayerListener(PlayerListener __a)
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
	public String getContentType()
	{
		throw Debugging.todo();
	}
	
	@Override
	public Control getControl(String __control)
	{
		throw Debugging.todo();
	}
	
	@Override
	public Control[] getControls()
	{
		throw Debugging.todo();
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
	public int getState()
	{
		throw Debugging.todo();
	}
	
	@Override
	public TimeBase getTimeBase()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void prefetch()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	@Override
	public void realize()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	@Override
	public void removePlayerListener(PlayerListener __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setLoopCount(int __a)
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
	public void setTimeBase(TimeBase __a)
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	@Override
	public void start()
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
