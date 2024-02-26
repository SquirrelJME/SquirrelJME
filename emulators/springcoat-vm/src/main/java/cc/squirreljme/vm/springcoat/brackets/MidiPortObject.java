// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * Wraps {@link MidiPortBracket}.
 *
 * @since 2022/04/22
 */
public final class MidiPortObject
	extends AbstractGhostObject
{
	/** The MIDI port to interact with. */
	public final MidiPortBracket port;
	
	/**
	 * Initializes the midi device wrapper.
	 *
	 * @param __machine The machine used.
	 * @param __port The port to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/22
	 */
	public MidiPortObject(SpringMachine __machine, MidiPortBracket __port)
		throws NullPointerException
	{
		super(__machine, MidiPortBracket.class);
		
		if (__port == null)
			throw new NullPointerException("NARG");
		
		this.port = __port;
	}
}
