// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim.cpu.powerpc;

import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.powerpc32.PPCVariant;
import net.multiphasicapps.squirrelsim.EmulatedCPU;

/**
 * This provides a basic emulator which is capable of simulating a 32-bit
 * PowerPC CPU.
 *
 * @since 2016/07/04
 */
public class PowerPC32CPU
	extends EmulatedCPU
{
	/**
	 * Initializes the emulated 32-bit PowerPC CPU.
	 *
	 * @param __var The variant of the CPU to emulate.
	 * @param __end The endianess of the CPU.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public PowerPC32CPU(PPCVariant __var, JITCPUEndian __end)
		throws NullPointerException
	{
		// Check
		if (__var == null || __end == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

