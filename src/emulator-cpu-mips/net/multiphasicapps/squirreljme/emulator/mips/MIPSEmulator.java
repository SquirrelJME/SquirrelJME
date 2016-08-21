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
	/** The CPU endianess. */
	protected final JITCPUEndian endianess;
	
	/** The CPU bits. */
	protected final int bits;
	
	/** The MIPS variant. */
	protected final MIPSVariant variant;
	
	/**
	 * Initializes the MIPS CPU emulator.
	 *
	 * @param __hv The system hyporvisor.
	 * @param __bits The CPU bit size.
	 * @param __endian The endianess of the CPU.
	 * @param __variant The variant of the CPU.
	 * @throws IllegalArgumentException If the number of bits is not 32 or
	 * 64; or the endianess is not big or little.
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
		
		// {@squirreljme.error BZ02 The requested endianess of the given CPU
		// is not supported. (The CPU endianess)}
		if (__endian != JITCPUEndian.LITTLE && __endian != JITCPUEndian.BIG)
			throw new IllegalArgumentException(String.format("BZ02 %s",
				__endian));
		
		// Set
		this.bits = __bits;
		this.endianess = __endian;
		
		// {@squirreljme.error BZ03 Unsupported MIPS CPU variant. (The name
		// of the variant)}
		MIPSVariant var = null;
		for (MIPSVariant v : MIPSVariant.values())
			if (__variant.equals(v.toString()))
			{
				var = v;
				break;
			}
		if (var == null)
			throw new IllegalArgumentException(String.format("BZ03 %s",
				__variant));
		this.variant = var;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/06
	 */
	@Override
	public CPUState createCPUState()
	{
		return new MIPSCPUState(this);
	}
}

