// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	public abstract int get(int __i);
	
	/**
	 * Sets the value at the given index.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set.
	 * @throws IndexOutOfBoundsException If the index is outside of bounds.
	 * @since 2018/10/28
	 */
	public abstract void set(int __i, int __v);
	
	/**
	 * Returns the size of the array.
	 *
	 * @return The array size.
	 * @since 2018/10/28
	 */
	public abstract int size();
}

