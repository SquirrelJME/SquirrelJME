// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

/**
 * Array backed by a char array.
 *
 * @since 2018/11/04
 */
public final class SpringArrayObjectChar
	extends SpringArrayObject
{
	/** Elements in the array. */
	private final char[] _elements;
	
	/**
	 * Initializes the array.
	 *
	 * @param __self The self type.
	 * @param __cl The component type.
	 * @param __l The array length.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/11/04
	 */
	public SpringArrayObjectChar(SpringClass __self, SpringClass __cl,
		int __l)
		throws NullPointerException
	{
		super(__self, __cl, __l);
		
		// Initialize elements
		this._elements = new char[__l];
	}
	
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
	 * @since 2018/11/04
	 */
	public final <C> C get(Class<C> __cl, int __dx)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException
	{
		// Read value
		try
		{
			return __cl.cast(Integer.valueOf(this._elements[__dx]));
		}
		
		// {@squirreljme.error BK2g Out of bounds access to array. (The index;
		// The length of the array)}
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK2g %d %d", __dx, length), e);
		}
	}
	
	/**
	 * Sets the index to the specified value.
	 *
	 * @param __dx The index to set.
	 * @param __v The value to set.
	 * @throws SpringArrayStoreException If the array cannot store the given
	 * type.
	 * @throws SpringArrayIndexOutOfBoundsException If the index is not within
	 * bounds.
	 * @since 2018/11/04
	 */
	public final void set(int __dx, Object __v)
		throws SpringArrayStoreException, SpringArrayIndexOutOfBoundsException
	{
		// Try setting
		try
		{
			this._elements[__dx] = (char)((Integer)__v).intValue();
		}
		
		// {@squirreljme.error BK2e Could not set the index in the char
		// array.}
		catch (ClassCastException e)
		{
			throw new SpringArrayStoreException("BK2e", e);
		}
		
		// {@squirreljme.error BK2f Out of bounds access to array. (The index;
		// The length of the array)}
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK2f %d %d", __dx, this.length), e);
		}
	}
}

