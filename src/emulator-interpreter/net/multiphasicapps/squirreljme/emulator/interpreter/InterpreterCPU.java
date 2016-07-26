// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.interpreter;

import net.multiphasicapps.squirreljme.emulator.EmulatorComponent;
import net.multiphasicapps.squirreljme.emulator.EmulatorSystem;

/**
 * This emulates the CPU that the interpreter uses.
 *
 * @since 2016/07/26
 */
public class InterpreterCPU
	extends EmulatorComponent
{
	/**
	 * Initializes the CPU.
	 *
	 * @param __es The owning system.
	 * @param __id The CPU identifier.
	 * @param __args The arguments to the CPU.
	 * @since 2016/07/26
	 */
	public InterpreterCPU(EmulatorSystem __es, String __id, String... __args)
	{
		super(__es, __id, __args);
	}
}

