// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * These are the variants supported for the MIPS CPU.
 *
 * @since 2017/02/13
 */
public enum MIPSRevision
{
	/** The first MIPS CPU. */
	I("i"),	
	
	/** The second CPU. */
	II("ii"),
	
	/** The third. */
	III("iii"),
	
	/** The fourth. */
	IV("iv"),
	
	/** Revision 1. */
	R1("r1"),
	
	/** Revision 2. */
	R2("r2"),
	
	/** Revision 3. */
	R3("r3"),
	
	/** Revision 5. */
	R5("r5"),
	
	/** Revision 6. */
	R6("r6"),
	
	/** End. */
	;
	
	/** The variant name. */
	protected final String name;
	
	/**
	 * Initializes the variant information.
	 *
	 * @param __name The name of the variant.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/06
	 */
	private MIPSRevision(String __name)
		throws NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
	}
	
	/**
	 * Returns {@code true} if the revision has branch delay slots.
	 *
	 * @return {@code true} if this revision has branch delay slots.
	 * @since 2017/03/22
	 */
	public boolean hasBranchDelaySlots()
	{
		return true;
	}
	
	/**
	 * Returns {@code true} if the revision has load delay slots.
	 *
	 * @return {@code true} if this revision has load delay slots.
	 * @since 2017/03/22
	 */
	public boolean hasLoadDelaySlots()
	{
		// Only the original MIPS has load delay slots
		return this == I;
	}
	
	/**
	 * Checks if the CPU revision supports the given bits.
	 *
	 * @param __b The bits to check.
	 * @return {@code true} if it is supported.
	 * @since 2017/02/13
	 */
	public boolean supportsBits(int __b)
	{
		// 32-bit is supported by everything
		if (__b == 32)
			return true;
		
		// 64-bit depends on the CPU
		else if (__b == 64)
			switch (this)
			{
					// Supported
				case III:
				case IV:
				case R1:
				case R2:
				case R3:
				case R5:
				case R6:
					return true;
				
					// Unsupported
				default:
					return false;
			}
		
		// Anything else is not supported
		else
			return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/06
	 */
	@Override
	public final String toString()
	{
		return this.name;
	}
	
	/**
	 * Returns the revision of the CPU.
	 *
	 * @param __s The name of the revision.
	 * @return The CPU variant.
	 * @throws JITException If a revision is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	public static final MIPSRevision of(String __s)
		throws JITException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Go through all and check
		for (MIPSRevision rv : MIPSRevision.values())
			if (rv.name.equals(__s))
				return rv;
		
		// {@squirreljme.error AM01 Unknown MIPS architecture revision. (The
		// revision identifier)}
		throw new JITException(String.format("AM01 %s", __s));
	}
}

