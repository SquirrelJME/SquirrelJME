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
import java.util.RandomAccess;
import java.util.List;

/**
 * This is a list representation of an array which cannot be modified.
 *
 * @param <T> The type of values to store.
 * @since 2018/05/13
 */
public final class UnmodifiableArrayList<T>
	extends AbstractList<T>
	implements RandomAccess
{
	/** The element offset. */
	protected final int offset;
	
	/** The element length. */
	protected final int length;
	
	/** The source elements. */
	private final T[] _source;
	
	/**
	 * Initializes the list.
	 *
	 * @param __a The input array.
	 * @param __o The offset into the array.
	 * @param __l The number of elements to read.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/13
	 */
	UnmodifiableArrayList(T[] __a, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) >= __a.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		this._source = __a;
		this.offset = __o;
		this.length = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/13
	 */
	@Override
	public final T get(int __i)
		throws IndexOutOfBoundsException
	{
		if (__i < 0 || __i >= this.length)
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		
		return this._source[this.offset + __i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/13
	 */
	@Override
	public final int size()
	{
		return this.length;
	}
	
	/**
	 * Wraps the given array.
	 *
	 * @param __a The array to wrap.
	 * @return The iterator for the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/13
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> List<T> of(T... __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		return new UnmodifiableArrayList<T>(__a, 0, __a.length);
	}
	
	/**
	 * Wraps the given array.
	 *
	 * @param __a The input array.
	 * @param __o The offset into the array.
	 * @param __l The number of elements to read.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @return The iterator for the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/13
	 */
	public static <T> List<T> of(T[] __a, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) >= __a.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		return new UnmodifiableArrayList<T>(__a, __o, __l);
	}
}

