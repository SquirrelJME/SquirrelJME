// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
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
	/** The port to play into. */
	private final MidiPortBracket port;
	
	/**
	 * Initializes the MIDI control.
	 * 
	 * @param __port The port to play into.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	public MidiShelfControl(MidiPortBracket __port)
		throws NullPointerException
	{
		if (__port == null)
			throw new NullPointerException("NARG");
		
		this.port = __port;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	public int longMidiEvent(byte[] __b, int __o, int __l)
		throws IllegalArgumentException, IllegalStateException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		MidiShelf.dataTransmit(this.port, __b, __o, __l);
		return __l;
	}
}
