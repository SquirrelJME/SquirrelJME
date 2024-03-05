// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

/**
 * This contains static helper methods for the MIDI format.
 *
 * @since 2022/04/24
 */
public final class MidiUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2022/04/24
	 */
	private MidiUtils()
	{
	}
	
	/**
	 * Returns the length of the MIDI event.
	 * 
	 * @param __type The event type.
	 * @return The length of the event in bytes or {@code -1} if undefined.
	 * @since 2022/04/24
	 */
	public static int midiMessageLength(int __type)
	{
		// Three byte
		if ((__type >= 0x80 && __type <= 0xBF) ||
			(__type >= 0xE0 && __type <= 0xEF) ||
			(__type == 0xF2))
			return 3;
		
		// Two byte
		else if ((__type >= 0xC0 && __type <= 0xDF) || (__type == 0xF3))
			return 2;
		
		// Single byte
		else if ((__type == 0xF1) || (__type >= 0xF4 && __type <= 0xFF))
			return 1;
		
		// Unknown/Undefined
		return -1;
	}
}
