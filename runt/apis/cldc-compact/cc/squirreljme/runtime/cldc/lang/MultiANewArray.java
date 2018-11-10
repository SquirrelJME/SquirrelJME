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

import cc.squirreljme.runtime.cldc.asm.ObjectAccess;

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
	 * @param __skip The initial number of dimensions to skip in the initial
	 * array.
	 * @param __dims The dimensions and the number of them to use.
	 * @return The allocated multi-dimensional array.
	 * @throws NegativeArraySizeException If an allocated array would be
	 * of a negative size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/03
	 */
	public static final Object multiANewArray(Class<?> __type, int __skip,
		int... __dims)
		throws NegativeArraySizeException, NullPointerException
	{
		if (__type == null || __dims == null)
			throw new NullPointerException("NARG");
		
		// Count the number of dimensions represented in the type
		String typename = __type.getName();
		int typedims = 0;
		while (typename.charAt(typedims) == '[')
			typedims++;
		
		// {@squirreljme.error ZZ09 Negative number of dimensions available
		// or input type is not correct for the array type.}
		int dims = __dims.length - __skip;
		if (__skip < 0 || dims <= 0 || typedims < dims)
			throw new IllegalArgumentException("ZZ09");
		
		// Allocate array of this type
		int numelem = __dims[__skip];
		Object[] rv = (Object[])ObjectAccess.arrayNew(__type, numelem);
		
		// The array has more dimensions which must be set
		if (dims > 1)
		{
			// Remove a single brace from the class to cut down its dimensions
			// Class names use dot forms, so refer to classes using the class
			// handler.
			Class<?> subtype;
			try
			{
				subtype = Class.forName(typename.substring(1));
			}
			
			// {@squirreljme.error ZZ0a Could not find the sub-type for
			// multi-dimensional array.}
			catch (ClassNotFoundException e)
			{
				throw new Error("ZZ0a", e);
			}
			
			// Skipping ahead by one
			int nxskip = __skip + 1;
			
			// Allocate
			for (int i = 0; i < numelem; i++)
				rv[i] = MultiANewArray.multiANewArray(subtype, nxskip, __dims);
		}
		
		return (Object)rv;
	}
}

