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
		throws ArrayIndexOutOfBoundsException, ArrayStoreException,
			NullPointerException
	{
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		Object source;
		int srcpos;
		
		// Obtain a primitive source array
		if (__src instanceof Array)
		{
			Array array = (Array)__src;
			
			// Use the array directly if it is local
			if (array instanceof LocalArray)
			{
				source = ((LocalArray)__src).localArray();
				srcpos = __srcpos;
			}
			
			// Read array into temporary buffer
			else
			{
				throw new todo.TODO();
			}
		}
		else
		{
			source = __src;
			srcpos = __srcpos;
		}
		
		// Write into the destination array
		if (__dest instanceof Array)
		{
			Array array = (Array)__dest;
			
			// Use the array directly if it is local
			if (array instanceof LocalArray)
				System.arraycopy(source, srcpos,
					((LocalArray)array).localArray(), __destpos, __len);
			
			// Write into remote array
			else
			{
				throw new todo.TODO();
			}
		}
		else
			System.arraycopy(source, srcpos, __dest, __destpos, __len);
	}
	
	/**
	 * Returns the length of the array.
	 *
	 * @param __a The array to get the length of.
	 * @return The length of the given array.
	 * @throws ClassCastException If it is not an array type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/28
	 */
	public static final int length(Object __a)
		throws ClassCastException, NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		if (__a instanceof Array)
			return ((Array)__a).length();
		else if (__a instanceof Object[])
			return ((Object[])__a).length;
		else if (__a instanceof boolean[])
			return ((boolean[])__a).length;
		else if (__a instanceof byte[])
			return ((byte[])__a).length;
		else if (__a instanceof short[])
			return ((short[])__a).length;
		else if (__a instanceof char[])
			return ((char[])__a).length;
		else if (__a instanceof int[])
			return ((int[])__a).length;
		else if (__a instanceof long[])
			return ((long[])__a).length;
		else if (__a instanceof float[])
			return ((float[])__a).length;
		else if (__a instanceof double[])
			return ((double[])__a).length;
		
		// {@squirreljme.error ZZ0s Cannot get the length of the given object
		// because it is not an array. (The class type)}
		else
			throw new ClassCastException(String.format("ZZ0s %s",
				__a.getClass()));
	}
}

