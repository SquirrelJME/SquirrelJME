// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

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
	 * Equivalent to {@link System#arraycopy(Object, int, Object, int, int)}
	 * for faster copy speed.
	 *
	 * @param __srcOff The source offset of this array.
	 * @param __dest The destination array.
	 * @param __destOff The destination offset.
	 * @param __len The number of values to copy.
	 * @throws IndexOutOfBoundsException If the offsets are out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public void copyFrom(int __srcOff,
		int[] __dest, int __destOff, int __len)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__dest == null)
			throw new NullPointerException("NARG");
		
		// Check to make sure offsets are valid
		int srcLen = this.length;
		int destLen = __dest.length;
		if (__srcOff < 0 || (__srcOff + __len) < 0 ||
				(__srcOff + __len) > srcLen ||
			__destOff < 0 || (__destOff + __len) < 0 ||
				(__destOff + __len) > destLen)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Forward to system copy method
		System.arraycopy(this.array, this.offset + __srcOff,
			__dest, __destOff, __len);
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

