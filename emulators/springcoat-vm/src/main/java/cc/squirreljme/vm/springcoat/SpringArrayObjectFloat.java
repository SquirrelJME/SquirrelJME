// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.exceptions.SpringArrayIndexOutOfBoundsException;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayStoreException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNegativeArraySizeException;

/**
 * Array backed by a float array.
 *
 * @since 2018/11/14
 */
public final class SpringArrayObjectFloat
	extends SpringArrayObject
{
	/** Elements in the array. */
	private final float[] _elements;
	
	/**
	 * Initializes the array.
	 *
	 * @param __self The self type.
	 * @param __l The array length.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/11/14
	 */
	public SpringArrayObjectFloat(SpringClass __self, int __l)
		throws NullPointerException
	{
		super(__self, __l);
		
		// Initialize elements
		this._elements = new float[__l];
	}
	
	/**
	 * Wraps the native array.
	 *
	 * @param __self The self type.
	 * @param __a The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/18
	 */
	public SpringArrayObjectFloat(SpringClass __self, float[] __a)
		throws NullPointerException
	{
		super(__self, __a.length);
		
		this._elements = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public final float[] array()
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
			return (C)Float.valueOf(this._elements[__dx]);
		}
		
		/* {@squirreljme.error BK0e Out of bounds access to array. (The index;
		The length of the array)} */
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK0e %d %d", __dx, this.length), e);
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
			this._elements[__dx] = ((Float)__v).floatValue();
		}
		
		/* {@squirreljme.error BK0f Could not set the index in the float
		array.} */
		catch (ClassCastException e)
		{
			throw new SpringArrayStoreException("BK0f", e);
		}
		
		/* {@squirreljme.error BK0g Out of bounds access to array. (The index;
		The length of the array)} */
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK0g %d %d", __dx, this.length), e);
		}
	}
}

