// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

/**
 * Wraps an integer array for access.
 *
 * @since 2018/10/28
 */
public final class IntegerIntegerArray
	extends AbstractIntegerArray
{
	/** The backed array. */
	protected final int[] array;
	
	/** The offset. */
	protected final int offset;
	
	/** The length. */
	protected final int length;
	
	/**
	 * Initializes the array wrapper.
	 *
	 * @param __a The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/28
	 */
	public IntegerIntegerArray(int[] __a)
		throws NullPointerException
	{
		this(__a, 0, __a.length);
	}
	
	/**
	 * Initializes the integer array.
	 *
	 * @param __a The array used.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public IntegerIntegerArray(int[] __a, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __a.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		this.array = __a;
		this.offset = __o;
		this.length = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final int get(int __i)
	{
		if (__i < 0 || __i >= this.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		return this.array[this.offset + __i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final void set(int __i, int __v)
	{
		if (__i < 0 || __i >= this.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		this.array[this.offset + __i] = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final int size()
	{
		return this.length;
	}
}

