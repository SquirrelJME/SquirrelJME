// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.target;

/**
 * Represents the name of a target architecture family.
 *
 * @since 2022/09/04
 */
public enum TargetArchitecture
	implements HasTargetVariant<TargetArchitectureVariant>
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
	SUMMERCOAT,
	
	/** X86 Family. */
	X86,
	
	/* End. */
	;
}
