// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.media.protocol.DataSource;

/**
 * This is a MIDI device which plays SoundFont 2 ({@code .sf2}) sound banks
 * and synthesizes the MIDI audio through standard audio streams. This is for
 * any devices which do not support native MIDI devices.
 *
 * @since 2022/04/23
 */
public final class SoundFontTwoMidiControl
	extends SimpleMidiControl
{
	/**
	 * Returns the data source for playing streaming audio.
	 * 
	 * @return The data source for audio streaming.
	 * @since 2022/04/23
	 */
	public final DataSource dataSource()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public int longMidiEvent(byte[] __b, int __o, int __l)
		throws IllegalArgumentException, IllegalStateException
	{
		throw Debugging.todo();
	}
}
