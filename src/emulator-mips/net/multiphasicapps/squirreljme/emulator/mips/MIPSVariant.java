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

/**
 * This is a variant of the CPU that may be used in emulation.
 *
 * @since 2016/08/06
 */
public enum MIPSVariant
{
	/** The first MIPS CPU. */
	I("i"),	
	
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
}

