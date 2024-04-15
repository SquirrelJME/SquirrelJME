// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * This is a list which is volatile but not synchronized but is backed by an
 * array, the array which as it is managed should always be atomic. So any
 * operation which returns an array should return one which is valid and
 * cannot be changed. If it is out of date then it is just out of date.
 *
 * The list is backed by an array.
 *
 * @param <T> The type of values to store.
 * @since 2018/11/17
 */
final class __VolatileList__<T>
	implements Iterable<T>
{
	/** The values in the list. */
	private volatile Object[] _values =
		new Object[0];
	
	/**
	 * Adds the specified value but only if it is a unique object reference
	 * in this list.
	 *
	 * @param __v The value to add.
	 * @return If the value has been added or not.
	 * @since 2018/11/17
	 */
	public final boolean addUniqueObjRef(T __v)
	{
		Object[] values = this._values;
		
		// Already in the list? Do nothing
		for (Object v : values)
			if (v == __v)
				return false;
		
		// Otherwise append
		this.append(__v);
		return true;
	}
	
	/**
	 * Append the specified element to the list.
	 *
	 * @param __v The value to add.
	 * @return The index of this entry.
	 * @since 2018/11/17
	 */
	public final int append(T __v)
	{
		Object[] values = this._values;
		
		// Copy the array to increase the length
		int n = values.length;
		values = Arrays.<Object>copyOf(values, n + 1);
		
		// Store new value
		values[n] = __v;
		
		// Use this new array
		this._values = values;
		
		// Return the index of this entry
		return n;
	}
	
	/**
	 * Clears the list.
	 *
	 * @since 2018/11/17
	 */
	public final void clear()
	{
		this._values = new Object[0];
	}
	
	/**
	 * Checks if this list contains the given item.
	 * 
	 * @param __v The value to check.
	 * @return If this contains the item or not.
	 * @since 2020/09/27
	 */
	public boolean containsUniqueObjRef(T __v)
	{
		for (Object v : this._values)
			if (__v == v)
				return true;
		
		return false;
	}
	
	/**
	 * Gets the element at the given index.
	 *
	 * @param __i The index to get.
	 * @return The element.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2018/12/09
	 */
	@SuppressWarnings({"unchecked"})
	public final T get(int __i)
		throws IndexOutOfBoundsException
	{
		// Is this in bounds?
		Object[] values = this._values;
		if (__i < 0 || __i >= values.length)
			throw new IndexOutOfBoundsException("IOOB " + __i + " " +
				values.length);
		
		return (T)values[__i];
	}
	
	/**
	 * Inserts the item at the given index.
	 * 
	 * @param __at The position to add at.
	 * @param __item The item to add.
	 * @throws IndexOutOfBoundsException If the given index is not within the
	 * bounds for insertion.
	 * @since 2020/11/15
	 */
	public void insert(int __at, T __item)
		throws IndexOutOfBoundsException
	{
		Object[] values = this._values;
		int vn = values.length;
		
		// Out of bounds?
		if (__at < 0 || __at > vn)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Setup new array and shift values down 
		Object[] newValues = Arrays.copyOf(values, vn + 1);
		System.arraycopy(newValues, __at,
			newValues, __at + 1, vn - __at);
		
		// Store new value at this point
		newValues[__at] = __item;
		
		// Use these new values
		this._values = newValues;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final Iterator<T> iterator()
	{
		return this.valuesAsList().iterator();
	}
	
	/**
	 * Removes the specified item from the list.
	 *
	 * @param __v The item to remove.
	 * @return {@code true} if it was removed.
	 * @since 2019/04/15
	 */
	public final boolean remove(T __v)
	{
		Object[] values = this._values;
		
		// Check if we have this item first
		int dx = 0, n = values.length;
		for (; dx < n; dx++)
		{
			Object t = values[dx];
			
			// No match
			if ((__v == null) != (t == null) || !Objects.equals(t, __v))
				continue;
			
			// Stop
			break;
		}
		
		// No match
		if (dx >= n)
			return false;
		
		// Setup new array and copy all elements before and after
		Object[] newvalues = new Object[n - 1];
		System.arraycopy(values, 0, newvalues, 0, dx);
		for (int i = dx + 1, o = dx; i < n; i++, o++)
			newvalues[o] = values[i];
			
		// Use this new array
		this._values = newvalues;
		
		// Was changed
		return true;
	}
	
	/**
	 * Deletes the given item.
	 * 
	 * @param __dx The index to delete.
	 * @return The deleted item.
	 * @throws IndexOutOfBoundsException If the given item is not within
	 * bounds.
	 * @since 2020/11/21
	 */
	@SuppressWarnings("unchecked")
	public T remove(int __dx)
		throws IndexOutOfBoundsException
	{
		Object[] values = this._values;
		
		int n = values.length;
		if (__dx < 0 || __dx >= n)
			throw new IndexOutOfBoundsException("IOOB");
		
		Object[] newValues = new Object[n - 1];
		
		// Copy left
		System.arraycopy(values, 0,
			newValues, 0, __dx);
		
		// Copy right as long as there is something to copy
		if (__dx + 1 < n)
			System.arraycopy(values, __dx + 1,
				newValues, __dx, (n - __dx) - 1);
		
		// Update values
		this._values = newValues;
		
		// Return the old value
		return (T)values[__dx];
	}
	
	/**
	 * Returns the number of values.
	 *
	 * @return The number of values stored.
	 * @since 2018/11/18
	 */
	public final int size()
	{
		return this._values.length;
	}
	
	/**
	 * Converts the values to an array of the specific type.
	 *
	 * @param __ov The output array.
	 * @return The output array, may be recreated if too small.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/17
	 */
	@SuppressWarnings({"unchecked"})
	public final T[] toArray(T[] __ov)
		throws NullPointerException
	{
		if (__ov == null)
			throw new NullPointerException("NARG");
		
		// Get input values
		Object[] iv = this._values;
		int in = iv.length;
		
		// Too short of an array? Grow it
		int on = __ov.length;
		if (on < in)
			__ov = Arrays.<T>copyOf(__ov, in);
		
		// Copy values
		for (int i = 0; i < in; i++)
			__ov[i] = (T)iv[i];
		
		// And use the passed or recreated array
		return __ov;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/27
	 */
	@Override
	public final String toString()
	{
		return this.valuesAsList().toString();
	}
	
	/**
	 * Returns the values in the list.
	 *
	 * @return The list values.
	 * @since 2018/11/18
	 */
	@SuppressWarnings("unchecked")
	public final T[] values()
	{
		return (T[])this._values;
	}
	
	/**
	 * Returns the values in this list as a list.
	 *
	 * @return The list of values.
	 * @since 2018/12/09
	 */
	@SuppressWarnings({"unchecked"})
	public final List<T> valuesAsList()
	{
		return (List<T>)Arrays.<Object>asList(this._values);
	}
}

