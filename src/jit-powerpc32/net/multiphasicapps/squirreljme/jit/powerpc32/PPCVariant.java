// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.powerpc32;

import net.multiphasicapps.squirreljme.jit.JITCPUVariant;

/**
 * The CPU variants that the JIT supports when targetting a given system.
 *
 * @since 2016/07/03
 */
public enum PPCVariant
	implements JITCPUVariant
{
	/** PowerPC G1 (601). */
	G1("g1"),
	
	/** PowerPC G2 (603). */
	G2("g2"),
	
	/** PowerPC G3 (7xx). */
	G3("g3"),
	
	/** PowerPC G4. */
	G4("g4"),
	
	/** PowerPC G5 (IBM 970). */
	G5("g5"),
	
	/** Power ISA 2.02. */
	POWER_ISA_2_02("isa202"),
	
	/** Power ISA 2.03. */
	POWER_ISA_2_03("isa203"),
	
	/** Power ISA 2.04. */
	POWER_ISA_2_04("isa204"),
	
	/** Power ISA 2.05. */
	POWER_ISA_2_05("isa205"),
	
	/** Power ISA 2.06. */
	POWER_ISA_2_06("isa206"),
	
	/** Power ISA 2.07. */
	POWER_ISA_2_07("isa207"),
	
	/** Power ISA 3.00. */
	POWER_ISA_3_00("isa300"),
	
	/** End. */
	;
	
	/** The variant name. */
	protected final String variant;
	
	/**
	 * Initializes the variant information.
	 *
	 * @param __v The name of the variant.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/25
	 */
	private PPCVariant(String __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.variant = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/25
	 */
	@Override
	public String variantName()
	{
		return this.variant;
	}
}

