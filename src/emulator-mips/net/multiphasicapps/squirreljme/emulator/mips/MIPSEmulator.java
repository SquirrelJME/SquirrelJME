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

import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.squirreljme.emulator.HypoVisor;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;

/**
 * This class contains the MIPS emulator.
 *
 * @since 2016/07/30
 */
public class MIPSEmulator
	extends Emulator
{
	/**
	 * Initializes the MIPS CPU emulator.
	 *
	 * @param __hv The system hyporvisor.
	 * @param __bits The CPU bit size.
	 * @param __endian The endianess of the CPU.
	 * @param __variant The variant of the CPU.
	 * @throws IllegalArgumentException If the number of bits is not 32 or
	 * 64.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public MIPSEmulator(HypoVisor __hv, int __bits, JITCPUEndian __endian,
		String __variant)
		throws IllegalArgumentException, NullPointerException
	{
		super(__hv);
		
		// Check
		if (__endian == null || __variant == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BZ01 The CPU bit size of the emulated MIPS
		// system is neither 32-bit or 64-bit. (The number of bits)}
		if (__bits != 32 && __bits != 64)
			throw new IllegalArgumentException(String.format("BZ01 %d",
				__bits));
		
		throw new Error("TODO");
	}
}

