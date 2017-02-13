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
	 * @throws IllegalArgumentException If a variant was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	public static final MIPSRevision of(String __s)
		throws IllegalArgumentException, NullPointerException
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
		throw new IllegalArgumentException(String.format("AM01 %s", __s));
	}
}

