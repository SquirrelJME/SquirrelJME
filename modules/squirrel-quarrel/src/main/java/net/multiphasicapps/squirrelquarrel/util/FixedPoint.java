// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	int fraction();
	
	/**
	 * Returns the whole number part.
	 *
	 * @return The whole number part.
	 * @since 2018/03/18
	 */
	int whole();
}

