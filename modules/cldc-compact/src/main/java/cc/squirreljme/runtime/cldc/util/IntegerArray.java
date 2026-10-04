// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

/**
 * This interface represents integer arrays.
 *
 * @since 2018/10/28
 */
public interface IntegerArray
{
	/**
	 * Gets the value at the given index.
	 *
	 * @param __i The index to get.
	 * @return The value at the given index.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @since 2018/10/28
	 */
	int get(int __i);
	
	/**
	 * Sets the value at the given index.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @since 2018/10/28
	 */
	void set(int __i, int __v);
	
	/**
	 * Returns the size of the array.
	 *
	 * @return The array size.
	 * @since 2018/10/28
	 */
	int size();
}

