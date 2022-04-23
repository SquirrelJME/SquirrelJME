// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * Wraps {@link MidiDeviceBracket}.
 *
 * @since 2022/04/22
 */
public final class MidiDeviceObject
	extends AbstractGhostObject
{
	/** The MIDI device to interact with. */
	public final MidiDeviceBracket device;
	
	/**
	 * Initializes the midi device wrapper.
	 *
	 * @param __machine The machine used.
	 * @param __dev The device to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/22
	 */
	public MidiDeviceObject(SpringMachine __machine, MidiDeviceBracket __dev)
		throws NullPointerException
	{
		super(__machine, MidiDeviceBracket.class);
		
		if (__dev == null)
			throw new NullPointerException("NARG");
		
		this.device = __dev;
	}
}
