// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

/**
 * This is a list of integers which is backed by a primitive array rather than
 * boxed types.
 *
 * @since 2017/11/26
 */
public final class IntegerList
	extends AbstractList<Integer>
	implements RandomAccess
{
	/** The internal integer list. */
	private volatile int[] _values;
	
	/** The number of values in the list. */
	private volatile int _size;
	
	/**
	 * Initializes an empty list.
	 *
	 * @since 2017/11/26
	 */
	public IntegerList()
	{
	}
	
	/**
	 * Initializes a list using the given collection of integers.
	 *
	 * @param __v The collection to source values from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/26
	 */
	public IntegerList(Collection<Integer> __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes a list using the given integer values from an array.
	 *
	 * @param __v The array of integers to use for values.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/26
	 */
	public IntegerList(int... __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public boolean add(Integer __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public void add(int __a, Integer __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Adds the specified integer to the list.
	 *
	 * @param __v The value to add.
	 * @return {@code true} if the list has changed.
	 * @since 2017/11/26
	 */
	public boolean addInteger(int __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Adds the specified integer to the list at the specified position.
	 *
	 * @param __i The index to add the value at.
	 * @param __v The value to add.
	 * @since 2017/11/26
	 */
	public void addInteger(int __i, int __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public void clear()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public boolean contains(Object __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Checks if the list contains the specified integer.
	 *
	 * @param __v The value to check.
	 * @return {@code true} if the list contains the given integer.
	 * @since 2017/11/26
	 */
	public boolean containsInteger(int __v)
	{
		return this.indexOfInteger(__v) >= 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public Integer get(int __i)
		throws IndexOutOfBoundsException
	{
		return this.getInteger(__i);
	}
	
	/**
	 * Obtains the integer at the given index.
	 *
	 * @param __i The index to get.
	 * @return The integer at the given index.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @since 2017/11/26
	 */
	public int getInteger(int __i)
		throws IndexOutOfBoundsException
	{
		if (__i < 0 || __i >= this._size)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public int indexOf(Object __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the index which contains the specified integer.
	 *
	 * @param __v The value to search for.
	 * @return The index of the specified integer or {@code -1} if the list
	 * contains no such value.
	 * @since 2017/11/26
	 */
	public int indexOfInteger(int __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public boolean isEmpty()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public int lastIndexOf(Object __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the index which contains the specified integer starting from
	 * the end of the list.
	 *
	 * @param __v The value to search for.
	 * @return The index of the specified integer or {@code -1} if the list
	 * contains no such value.
	 * @since 2017/11/26
	 */
	public int lastIndexOfInteger(int __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public boolean remove(Object __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public Integer remove(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public Integer set(int __a, Integer __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the integer at the specified index to the given value.
	 *
	 * @param __i The index to set.
	 * @param __v The value to set.
	 * @return The old value.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @since 2017/11/26
	 */
	public int setInteger(int __i, int __v)
		throws IndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/26
	 */
	@Override
	public int size()
	{
		return this._size;
	}
	
	/**
	 * Converts the integer list to an integer array.
	 *
	 * @return This list as an integer array.
	 * @since 2017/11/26
	 */
	public int[] toIntegerArray()
	{
		throw new todo.TODO();
	}
}

