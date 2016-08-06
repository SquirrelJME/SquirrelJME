// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.mips;

import net.multiphasicapps.squirreljme.emulator.CPUState;

/**
 * This contains the mutable CPU state for MIPS based systems.
 *
 * MIPS has 32 general purpose registers, where the first is used as a
 * dedicated zero register or bit bucket.
 *
 * @since 2016/08/06
 */
public class MIPSCPUState
	implements CPUState
{
	/**
	 * Initializes the CPU state.
	 *
	 * @param __emu The emulator creating this, to setup the register state.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/06
	 */
	public MIPSCPUState(MIPSEmulator __emu)
		throws NullPointerException
	{
		// Check
		if (__emu == null)
			throw new NullPointerException("NARG");
	}
}

