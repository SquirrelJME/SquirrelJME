// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is a set which is backed by a hash table except that the iterator
 * order is in the order of which elements were added first.
 *
 * Otherwise this class is exactly the same as {@link HashSet}.
 *
 * @param <E> The element type to store.
 * @see HashSet
 * @since 2018/11/01
 */
public class LinkedHashSet<E>
	extends HashSet<E>
	implements Set<E>, Cloneable
{
	/**
	 * Initializes the set with the given capacity and load factor.
	 *
	 * @param __cap The capacity used.
	 * @param __load The load factor used.
	 * @throws IllegalArgumentException If the capacity is negative or the
	 * load factor is not positive.
	 * @since 2018/11/01
	 */
	public LinkedHashSet(int __initcap, float __load)
	{
		super(__initcap, __load);
	}
	
	/**
	 * Initializes the set with the given capacity and the default load factor.
	 *
	 * @param __cap The capacity used.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2018/11/01
	 */
	public LinkedHashSet(int __initcap)
	{
		super(__initcap);
	}
	
	/**
	 * Initializes the set with the default capacity and load factor.
	 *
	 * @since 2018/11/01
	 */
	public LinkedHashSet()
	{
		super();
	}
	
	/**
	 * Initializes a set which is a copy of the other map.
	 *
	 * The default load factor is used and the capacity is set to the
	 * capacity of the input set.
	 *
	 * @param __s The set to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	public LinkedHashSet(Collection<? extends E> __s)
	{
		super(__s);
	}
}

