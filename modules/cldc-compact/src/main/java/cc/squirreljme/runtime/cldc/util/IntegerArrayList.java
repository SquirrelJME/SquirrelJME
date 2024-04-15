// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * Provides a list view of a {@code int} array.
 *
 * @since 2020/07/11
 */
public class IntegerArrayList
	extends AbstractList<Integer>
	implements RandomAccess
{
	/** The backing array. */
	protected final IntegerArray array;
	
	/**
	 * Initializes the long array view.
	 *
	 * @param __a The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/11
	 */
	public IntegerArrayList(int[] __a)
		throws NullPointerException
	{
		this(__a, 0, __a.length);
	}
	
	/**
	 * Initializes the long array view.
	 *
	 * @param __a The array to wrap.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/11
	 */
	public IntegerArrayList(int[] __a, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		this(new IntegerIntegerArray(__a, __o, __l));
	}
	
	/**
	 * Initializes the list.
	 *
	 * @param __a The array to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/09
	 */
	public IntegerArrayList(IntegerArray __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		this.array = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/11
	 */
	@Override
	public Integer get(int __i)
	{
		if (__i < 0 || __i >= this.array.size())
			throw new IndexOutOfBoundsException("IOOB");
		
		return this.array.get(__i);
	}
	
	/**
	 * Sets the value of the given index,
	 *
	 * @param __i The index to set.
	 * @param __v The value to set.
	 * @return The old value.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @since 2020/07/11
	 */
	public int set(int __i, int __v)
		throws IndexOutOfBoundsException
	{
		IntegerArray array = this.array;
		if (__i < 0 || __i >= array.size())
			throw new IndexOutOfBoundsException("IOOB");
		
		// Cycle values
		int old = array.get(__i);
		array.set(__i, __v);
		return old;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/11
	 */
	@Override
	public Integer set(int __i, Integer __v)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		return this.set(__i, __v.intValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/11
	 */
	@Override
	public int size()
	{
		return this.array.size();
	}
	
	/**
	 * Returns the boxed list type of the given primitive array.
	 * 
	 * @param __array The array to wrap.
	 * @return The boxed list type.
	 * @since 2020/07/11
	 */
	public static List<Integer> asList(int... __array)
	{
		return new IntegerArrayList(__array);
	}
	
	/**
	 * Returns the string representation of the given integer array.
	 * 
	 * @param __ints The array to represent as a string.
	 * @return The array as a string or {@code "null"} if {@code null}.
	 * @since 2022/02/04
	 */
	public static String toString(int... __ints)
	{
		if (__ints == null)
			return "null";
		
		return IntegerArrayList.asList(__ints).toString();
	}
}
