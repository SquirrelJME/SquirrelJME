// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.sorted;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a sorted {@link Set} which internally uses a red-black tree to sort
 * the entries.
 *
 * @param <V> The type of value stored in the set.
 * @since 2016/09/06
 */
public class RedBlackSet<V>
	extends AbstractSet<V>
{
	/**
	 * Initializes an empty red/black set using the natural comparator.
	 *
	 * @since 2016/09/06
	 */
	public RedBlackSet()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Initializes a red/black set using the natural comparator which is
	 * initialized with the given values.
	 *
	 * @param __s The collection to copy values from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	public RedBlackSet(Collection<? extends Comparable<V>> __s)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Initializes an empty red/black set using the given comparator.
	 *
	 * @param __comp The comparator to use for values.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	public RedBlackSet(Comparator<? extends V> __comp)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Initializes a red/black set using the given comparator which is
	 * initialized with the given values.
	 *
	 * @param __comp The comparator to use for values.
	 * @param __s The collection to copy values from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	public RedBlackSet(Comparator<? extends V> __comp,
		Collection<? extends V> __s)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public Iterator<V> iterator()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public int size()
	{
		throw new Error("TODO");
	}
}

