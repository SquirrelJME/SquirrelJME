// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.powerpc;

import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.JITLogicAcceptor;

/**
 * This accepts and performs logical operations which output PowerPC machine
 * code.
 *
 * @since 2016/07/06
 */
public class PPCLogicAcceptor
	implements JITLogicAcceptor
{
	/** The target variant. */
	protected final PPCVariant variant;
	
	/** The number of bits to target. */
	protected final int bits;
	
	/** The endianess. */
	protected final JITCPUEndian endian;
	
	/**
	 * Initializes the logic acceptor.
	 *
	 * @param __var The variant to target.
	 * @param __bits The bits the CPU uses.
	 * @param __end The desired endianess.
	 * @throws JITException If the variant does not support the given number
	 * of bits.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/05
	 */
	public PPCLogicAcceptor(PPCVariant __var, int __bits, JITCPUEndian __end)
		throws JITException, NullPointerException
	{
		// Check
		if (__var == null || __end == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EE01 The given CPU variant does not support
		// the given word size. (The variant; The word size in bits)}
		if ((__bits != 32 && __bits != 64) ||
			(__bits == 64 && !__var.isSixtyFourBit()))
			throw new JITException(String.format("EE01 %s %d", __var, __bits));
		
		// Set
		this.variant = __var;
		this.bits = __bits;
		this.endian = __end;
	}
}

