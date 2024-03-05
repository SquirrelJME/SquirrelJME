// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import javax.sound.midi.MidiMessage;

/**
 * Basic MIDI message.
 *
 * @since 2022/04/22
 */
class BasicMidiMessage
	extends MidiMessage
{
	/**
	 * Initializes the basic message.
	 * 
	 * @param __data The data to set.
	 * @since 2022/04/22
	 */
	BasicMidiMessage(byte[] __data)
	{
		super(__data);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/22
	 */
	@Override
	public Object clone()
	{
		return new BasicMidiMessage(this.data);
	}
}
