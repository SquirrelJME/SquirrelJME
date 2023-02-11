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
 * Represents a target operating system family, sub-variants may be different
 * versions of the OS and otherwise.
 *
 * @since 2022/09/04
 */
public enum TargetOperatingSystem
	implements Banglet, HasTargetVariant<TargetOperatingSystemVariant>
{
	/** Amiga. */
	AMIGA,
	
	/** DOS. */
	DOS,
	
	/** Linux. */
	LINUX,
	
	/** MacOS Classic. */
	MACOS_CLASSIC,
	
	/** MacOS X. */
	MACOS_X,
	
	/** PalmOS. */
	PALMOS,
	
	/** SummerCoat Virtual Machine. */
	SUMMERCOAT,
	
	/** Windows. */
	WINDOWS,
	
	/* End. */
	;
	
	/** The available variants. */
	private final TargetOperatingSystemVariant[] _variants;
	
	/**
	 * Initializes the operating system.
	 * 
	 * @param __variants The variants used, if none specified this defaults
	 * to a single NONE.
	 * @since 2022/09/06
	 */
	TargetOperatingSystem(TargetOperatingSystemVariant... __variants)
	{
		if (__variants == null || __variants.length <= 0)
			this._variants = new TargetOperatingSystemVariant[]{
				TargetOperatingSystemVariant.NONE};
		else
			this._variants = __variants;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public final TargetOperatingSystemVariant[] variants()
	{
		return this._variants.clone();
	}
}
