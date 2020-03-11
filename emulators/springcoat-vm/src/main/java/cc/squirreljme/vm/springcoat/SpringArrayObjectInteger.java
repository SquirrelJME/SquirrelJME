// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * Array backed by an int array.
 *
 * @since 2018/11/04
 */
@Deprecated
public final class SpringArrayObjectInteger
	extends SpringArrayObject
{
	/** Elements in the array. */
	private final int[] _elements;
	
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
	public SpringArrayObjectInteger(SpringClass __self, SpringClass __cl,
		int __l)
		throws NullPointerException
	{
		super(__self, __cl, __l);
		
		// Initialize elements
		this._elements = new int[__l];
	}
	
	/**
	 * Wraps the native array.
	 *
	 * @param __self The self type.
	 * @param __cl The component type.
	 * @param __a The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/18
	 */
	public SpringArrayObjectInteger(SpringClass __self, SpringClass __cl,
		int[] __a)
		throws NullPointerException
	{
		super(__self, __cl, __a.length);
		
		this._elements = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public final Object array()
	{
		return this._elements;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public final <C> C get(Class<C> __cl, int __dx)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException
	{
		// Read value
		try
		{
			return (C)Integer.valueOf(this._elements[__dx]);
		}
		
		// {@squirreljme.error BK0k Out of bounds access to array. (The index;
		// The length of the array)}
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK0k %d %d", __dx, this.length), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final void set(int __dx, Object __v)
		throws SpringArrayStoreException, SpringArrayIndexOutOfBoundsException
	{
		// Try setting
		try
		{
			this._elements[__dx] = ((Integer)__v).intValue();
		}
		
		// {@squirreljme.error BK0l Could not set the index in the char
		// array.}
		catch (ClassCastException e)
		{
			throw new SpringArrayStoreException("BK0l", e);
		}
		
		// {@squirreljme.error BK0m Out of bounds access to array. (The index;
		// The length of the array)}
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK0m %d %d", __dx, this.length), e);
		}
	}
}

