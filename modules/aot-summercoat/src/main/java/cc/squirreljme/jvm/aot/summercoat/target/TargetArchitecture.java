// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.target;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents the name of a target architecture family.
 *
 * @since 2022/09/04
 */
public enum TargetArchitecture
	implements Banglet, HasTargetVariant<TargetArchitectureVariant>
{
	/** ARM. */
	ARM,
	
	/** Itanium (IA64). */
	ITANIUM,
	
	/** Motorola 68k Series. */
	M68K,
	
	/** MIPS. */
	MIPS,
	
	/** MMIX. */
	MMIX,
	
	/** OpenRISC. */
	OPENRISC,
	
	/** PA-RISC. */
	PA_RISC,
	
	/** Power ISA (POWER, PowerPC, etc.). */
	POWER_ISA,
	
	/** RISC-V. */
	RISC_V,
	
	/** SPARC. */
	SPARC,
	
	/** SuperH. */
	SUPERH,
	
	/** SummerCoat Virtual Machine. */
	SUMMERCOAT(TargetArchitectureVariant.NONE,
		SummerCoatArchitectureVariant.C),
	
	/** X86 Family. */
	X86,
	
	/* End. */
	;
	
	/** The available variants. */
	private final TargetArchitectureVariant[] _variants;
	
	/**
	 * Initializes the architecture.
	 * 
	 * @param __variants The variants used, if none specified this defaults
	 * to a single NONE.
	 * @since 2022/09/06
	 */
	TargetArchitecture(TargetArchitectureVariant... __variants)
	{
		if (__variants == null || __variants.length <= 0)
			this._variants = new TargetArchitectureVariant[]{
				TargetArchitectureVariant.NONE};
		else
			this._variants = __variants;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public final TargetArchitectureVariant[] variants()
	{
		return this._variants.clone();
	}
}
