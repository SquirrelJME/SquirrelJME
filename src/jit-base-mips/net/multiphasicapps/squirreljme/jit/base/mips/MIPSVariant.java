// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.base.mips;

import net.multiphasicapps.squirreljme.jit.base.JITTriplet;

/**
 * These are the variants supported for the MIPS CPU.
 *
 * @since 2016/08/06
 */
public enum MIPSVariant
{
	/** The first MIPS CPU. */
	I("i"),	
	
	/** The second CPU. */
	II("ii"),
	
	/** The third. */
	III("iii"),
	
	/** The fourth. */
	IV("iv"),
	
	/** The fifth. */
	V("v"),
	
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
	private MIPSVariant(String __name)
		throws NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
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
	 * Returns the variant of the CPU.
	 *
	 * @param __t The triplet to get the enumated variant from.
	 * @return The CPU variant.
	 * @throws IllegalArgumentException If a variant was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	public static final MIPSVariant fromTriplet(JITTriplet __t)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Go through all and check
		String v = __t.architectureVariant();
		for (MIPSVariant rv : MIPSVariant.values())
			if (rv.name.equals(v))
				return rv;
		
		// {@squirreljme.error BV01 Unknown MIPS architecture variant. (The
		// variant identifier)}
		throw new IllegalArgumentException(String.format("BV01 %s", v));
	}
}

