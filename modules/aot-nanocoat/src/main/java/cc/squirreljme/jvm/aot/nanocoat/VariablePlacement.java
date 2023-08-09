// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.jvm.aot.nanocoat.common.JvmPrimitiveType;

/**
 * Represents a single variable placement.
 *
 * @see VariablePlacements
 * @see VariablePlacementMap
 * @since 2023/08/09
 */
public class VariablePlacement
{
	/** The type that this is. */
	public final JvmPrimitiveType type;
	
	/** The index where it is contained within. */
	public final int index;
	
	/**
	 * Sets the variable placement.
	 *
	 * @param __type The type used.
	 * @param __index The index to store at.
	 * @throws IllegalArgumentException If the index is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public VariablePlacement(JvmPrimitiveType __type, int __index)
		throws IllegalArgumentException, NullPointerException
	{
		if (__index < 0)
			throw new IllegalArgumentException("NEGV");
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.index = __index;
	}
}
