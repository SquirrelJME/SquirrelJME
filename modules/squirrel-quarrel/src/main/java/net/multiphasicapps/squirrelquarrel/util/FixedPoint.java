// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.util;

/**
 * This represents a fixed point number.
 *
 * @since 2018/03/18
 */
public interface FixedPoint
	extends Comparable<FixedPoint>
{
	/**
	 * Returns the fraction part.
	 *
	 * @return The fraction part.
	 * @since 2018/03/18
	 */
	public abstract int fraction();
	
	/**
	 * Returns the whole number part.
	 *
	 * @return The whole number part.
	 * @since 2018/03/18
	 */
	public abstract int whole();
}

