// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.exceptions.SpringArrayIndexOutOfBoundsException;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayStoreException;

/**
 * Interface for any array type.
 *
 * @since 2024/08/04
 */
public interface SpringArray
	extends SpringObject
{
	/**
	 * Returns the raw backing array.
	 *
	 * @return The array.
	 * @since 2018/11/19
	 */
	Object array();
	
	/**
	 * Sets the index to the specified value.
	 *
	 * @param <C> The type of value to get.
	 * @param __cl The type of value to get.
	 * @param __dx The index to set.
	 * @return The contained value.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringArrayStoreException If the array cannot store the given
	 * type.
	 * @throws SpringArrayIndexOutOfBoundsException If the index is not within
	 * bounds.
	 * @since 2018/09/16
	 */
	<C> C get(Class<C> __cl, int __dx)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException;
	
	/**
	 * Is this an actual array?
	 *
	 * @return If this is an actual array.
	 * @since 2024/08/04
	 */
	boolean isArray();
	
	/**
	 * Sets the index to the specified value.
	 *
	 * @param __dx The index to set.
	 * @param __v The value to set.
	 * @throws SpringArrayStoreException If the array cannot store the given
	 * type.
	 * @throws SpringArrayIndexOutOfBoundsException If the index is not within
	 * bounds.
	 * @since 2018/09/16
	 */
	void set(int __dx, Object __v)
		throws SpringArrayStoreException, SpringArrayIndexOutOfBoundsException;
	
	/**
	 * Returns the length of this array.
	 *
	 * @return The array length.
	 * @since 2018/09/16
	 */
	int length();
}
