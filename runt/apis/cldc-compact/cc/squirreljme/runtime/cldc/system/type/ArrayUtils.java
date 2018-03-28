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
 * This class contains static utility methods which add convience for some
 * array operations.
 *
 * @since 2018/03/28
 */
public final class ArrayUtils
{
	/**
	 * Not used.
	 *
	 * @since 2018/03/28
	 */
	private ArrayUtils()
	{
	}
	
	/**
	 * Copies from the source array to the destination array.
	 *
	 * @param __src The source array.
	 * @param __srcpos The source position.
	 * @param __dest The destination array.
	 * @param __destpos The destination position.
	 * @param __len The number of elements to copy.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws ArrayStoreException If the arrays are of two different types.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/28
	 */
	public static final void copy(Object __src, int __srcpos,
		Object __dest, int __destpos, int __len)
		throws IndexOutOfBoundsException, ArrayStoreException,
			NullPointerException
	{
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

