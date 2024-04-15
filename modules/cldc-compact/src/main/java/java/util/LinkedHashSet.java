// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

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
@Api
public class LinkedHashSet<E>
	extends HashSet<E>
	implements Set<E>, Cloneable
{
	/**
	 * Initializes the set with the given capacity and load factor.
	 *
	 * @param __initcap The capacity used.
	 * @param __load The load factor used.
	 * @throws IllegalArgumentException If the capacity is negative or the
	 * load factor is not positive.
	 * @since 2018/11/01
	 */
	@Api
	public LinkedHashSet(int __initcap, float __load)
		throws IllegalArgumentException
	{
		super(__initcap, __load);
	}
	
	/**
	 * Initializes the set with the given capacity and the default load factor.
	 *
	 * @param __initcap The capacity used.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2018/11/01
	 */
	@Api
	public LinkedHashSet(int __initcap)
		throws IllegalArgumentException
	{
		super(__initcap);
	}
	
	/**
	 * Initializes the set with the default capacity and load factor.
	 *
	 * @since 2018/11/01
	 */
	@Api
	public LinkedHashSet()
	{
	}
	
	/**
	 * Initializes a set which is a copy of the other set.
	 *
	 * The default load factor is used and the capacity is set to the
	 * capacity of the input set.
	 *
	 * @param __s The set to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	@Api
	public LinkedHashSet(Collection<? extends E> __s)
	{
		super(__s);
	}
}

