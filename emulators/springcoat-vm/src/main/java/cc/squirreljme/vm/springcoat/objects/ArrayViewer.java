// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.objects;

import cc.squirreljme.vm.springcoat.exceptions.SpringArrayIndexOutOfBoundsException;

/**
 * This is an interface for the viewing of array types.
 *
 * @param <T> The type of value to store.
 * @since 2020/03/22
 */
public interface ArrayViewer<T>
	extends ObjectViewer
{
	/**
	 * Returns the value at the given index.
	 *
	 * @param __i The index to get.
	 * @throws SpringArrayIndexOutOfBoundsException If the index is out of
	 * bounds.
	 * @since 2020/03/22
	 */
	T get(int __i)
		throws SpringArrayIndexOutOfBoundsException;
	
	/**
	 * Returns the length of the array.
	 *
	 * @return The array length.
	 * @since 2020/03/22
	 */
	int length();
	
	/**
	 * Sets the given index to the specified value.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringArrayIndexOutOfBoundsException If the index is out of
	 * bounds.
	 * @since 2020/03/22
	 */
	void set(int __i, T __v)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException;
}
