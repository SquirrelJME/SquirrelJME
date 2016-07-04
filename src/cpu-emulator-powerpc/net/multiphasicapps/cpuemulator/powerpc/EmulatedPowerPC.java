// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.cpuemulator.powerpc;

import net.multiphasicapps.cpuemulator.CachedMemoryAccessor;
import net.multiphasicapps.cpuemulator.EmulatedCPU;
import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.powerpc32.PPCVariant;

/**
 * This emulates a 32-bit or 64-bit PowerPC CPU.
 *
 * @since 2016/07/04
 */
public class EmulatedPowerPC
	extends EmulatedCPU
{
	/** The cached memory accessor. */
	protected final CachedMemoryAccessor cachedaccessor;
	
	/**
	 * Initializes the emulated PowerPC CPU.
	 *
	 * @param __cma The memory accessor which emulates a cache.
	 * @param __var The variant of the CPU.
	 * @param __end The endianess of the CPU.
	 * @param __bits The number of CPU bits.
	 * @throws IllegalArgumentException If the number of bits is invalid or
	 * is not supported by the given CPU variant.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	public EmulatedPowerPC(CachedMemoryAccessor __cma, PPCVariant __var,
		JITCPUEndian __end, int __bits)
		throws NullPointerException
	{
		super(__cma);
		
		// Check
		if (__var == null || __end == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.cachedaccessor = __cma;
		
		throw new Error("TODO");
	}
}

