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
 * Array backed by a boolean array.
 *
 * @since 2018/11/14
 */
public final class SpringArrayObjectBoolean
	extends SpringArrayObject
{
	/** Elements in the array. */
	private final boolean[] _elements;
	
	/**
	 * Initializes the array.
	 *
	 * @param __self The self type.
	 * @param __l The array length.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/11/14
	 */
	public SpringArrayObjectBoolean(SpringClass __self, int __l)
		throws NullPointerException
	{
		super(__self, __l);
		
		// Initialize elements
		this._elements = new boolean[__l];
	}
	
	/**
	 * Wraps the native array.
	 *
	 * @param __self The self type.
	 * @param __a The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/18
	 */
	public SpringArrayObjectBoolean(SpringClass __self, boolean[] __a)
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
	public final boolean[] array()
	{
		return this._elements;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final <C> C get(Class<C> __cl, int __dx)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException
	{
		// Read value
		try
		{
			return (C)Integer.valueOf((this._elements[__dx] ? 1 : 0));
		}
		
		/* {@squirreljme.error BK02 Out of bounds access to array. (The index;
		The length of the array)} */
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK02 %d %d", __dx, this.length), e);
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
			boolean v;
			if (__v instanceof Integer)
				v = (((int)__v) & 0x1) != 0;
			else
				v = ((boolean)__v);
			
			this._elements[__dx] = v;
		}
		
		/* {@squirreljme.error BK03 Could not set the index in the boolean
		array.} */
		catch (ClassCastException e)
		{
			throw new SpringArrayStoreException("BK03", e);
		}
		
		/* {@squirreljme.error BK04 Out of bounds access to array. (The index;
		The length of the array)} */
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK04 %d %d", __dx, this.length), e);
		}
	}
}

