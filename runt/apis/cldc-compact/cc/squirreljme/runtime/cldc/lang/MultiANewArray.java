// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

/**
 * This method contains the code which is able to initialize multidimensional
 * arrays.
 *
 * @since 2018/11/03
 */
public final class MultiANewArray
{
	/**
	 * Not used.
	 *
	 * @since 2018/11/03
	 */
	private MultiANewArray()
	{
	}
	
	/**
	 * Allocates a new multi-dimensional array.
	 *
	 * @param __type The type with minimum dimension sizes specified.
	 * @param __dims The dimensions and the number of them to use.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/03
	 */
	public static final Object multiANewArray(Class<?> __type, int... __dims)
		throws NegativeArraySizeException, NullPointerException
	{
		if (__type == null || __dims == null)
			throw new NullPointerException("NARG");
		
		// Debug
		todo.DEBUG.note("For %s, %d dims", __type, __dims.length);
		
		throw new todo.TODO();
	}
}

