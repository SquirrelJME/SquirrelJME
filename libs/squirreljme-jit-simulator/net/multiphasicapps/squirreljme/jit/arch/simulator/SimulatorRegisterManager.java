// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch.simulator;

import net.multiphasicapps.squirreljme.jit.arch.RegisterManager;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigKey;
import net.multiphasicapps.squirreljme.jit.JITConfigValue;

/**
 * This is a manager which is used to manage registers used by the simulator.
 *
 * @since 2017/08/19
 */
public class SimulatorRegisterManager
	extends RegisterManager
{
	/**
	 * Initializes the register manager.
	 *
	 * @param __conf The configuration of the target simulator system.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/19
	 */
	public SimulatorRegisterManager(JITConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

