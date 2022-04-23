// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a MIDI player which uses the native support of MIDI through
 * {@link MidiShelf} if it is available.
 *
 * @since 2022/04/23
 */
public final class MidiShelfControl
	extends SimpleMidiControl
{
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
