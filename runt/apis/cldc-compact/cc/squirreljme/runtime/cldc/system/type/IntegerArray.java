// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.type;

/**
 * Accesses an interger array.
 *
 * @since 2018/02/21
 */
public interface IntegerArray
	extends Array
{
	/**
	 * Returns the value at the given index.
	 *
	 * @param __i The index to get.
	 * @return The value.
	 * @throws ArrayIndexOutOfBoundsException If out of bounds.
	 * @since 2018/03/04
	 */
	public abstract int get(int __i)
		throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Copies multiple values from this array to the destination array.
	 *
	 * @param __i The base index.
	 * @param __v The destination array.
	 * @param __o The offset into the destination array.
	 * @param __l The number of values to read.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/04
	 */
	public abstract void get(int __i, int[] __v, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Sets the value at the given index.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set.
	 * @throws ArrayIndexOutOfBoundsException If out of bounds.
	 * @since 2018/03/04
	 */
	public abstract void set(int __i, int __v)
		throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Copies multiple values from the source array to this array.
	 *
	 * @param __i The base index.
	 * @param __v The source array.
	 * @param __o The offset into the source array.
	 * @param __l The number of values to write.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/04
	 */
	public abstract void set(int __i, int[] __v, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException;
}

