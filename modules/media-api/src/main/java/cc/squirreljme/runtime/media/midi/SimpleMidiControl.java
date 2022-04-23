// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.MIDIControl;

/**
 * This is a basic class which provides a base simple implementation of
 * MIDI controls.
 *
 * @since 2022/04/23
 */
public abstract class SimpleMidiControl
	implements MIDIControl
{
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final int[] getBankList(boolean __custom)
		throws IllegalStateException, MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final int getChannelVolume(int __channel)
		throws IllegalArgumentException, IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final String getKeyName(int __bank, int __prog, int __key)
		throws IllegalArgumentException, IllegalStateException, MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final int[] getProgram(int __channel)
		throws IllegalArgumentException, IllegalStateException, MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final int[] getProgramList(int __bank)
		throws IllegalArgumentException, IllegalStateException, MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final String getProgramName(int __bank, int __program)
		throws IllegalArgumentException, IllegalStateException, MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final boolean isBankQuerySupported()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final void setChannelVolume(int __channel, int __volume)
		throws IllegalArgumentException, IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final void setProgram(int __channel, int __bank, int __program)
		throws IllegalArgumentException, IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public final void shortMidiEvent(int __type, int __data1, int __data2)
		throws IllegalArgumentException, IllegalStateException
	{
		throw Debugging.todo();
	}
}
