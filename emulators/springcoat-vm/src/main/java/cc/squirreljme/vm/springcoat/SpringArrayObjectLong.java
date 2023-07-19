// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayIndexOutOfBoundsException;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayStoreException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNegativeArraySizeException;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Array backed by a long array.
 *
 * @since 2018/11/14
 */
public final class SpringArrayObjectLong
	extends SpringArrayObject
{
	/** Elements in the array. */
	private final long[] _elements;
	
	/**
	 * Initializes the array.
	 *
	 * @param __self The self type.
	 * @param __l The array length.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/11/14
	 */
	public SpringArrayObjectLong(SpringClass __self, int __l)
		throws NullPointerException
	{
		super(__self, __l);
		
		// Initialize elements
		this._elements = new long[__l];
	}
	
	/**
	 * Wraps the native array.
	 *
	 * @param __self The self type.
	 * @param __a The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/18
	 */
	public SpringArrayObjectLong(SpringClass __self, long[] __a)
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
	public final long[] array()
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
			return (C)Long.valueOf(this._elements[__dx]);
		}
		
		/* {@squirreljme.error BK0n Out of bounds access to array. (The index;
		The length of the array)} */
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				__error__("BK0n %d %d", __dx, this.length), e);
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
			this._elements[__dx] = ((Long)__v).longValue();
		}
		
		/* {@squirreljme.error BK0o Could not set the index in the long
		array.} */
		catch (ClassCastException e)
		{
			throw new SpringArrayStoreException("BK0o", e);
		}
		
		/* {@squirreljme.error BK0p Out of bounds access to array. (The index;
		The length of the array)} */
		catch (IndexOutOfBoundsException e)
		{
			throw new SpringArrayIndexOutOfBoundsException(
				String.format("BK0p %d %d", __dx, this.length), e);
		}
	}
}

