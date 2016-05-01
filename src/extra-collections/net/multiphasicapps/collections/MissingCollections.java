// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

/**
 * This class contains helper methods for methods that exist in the desktop
 * {@link java.util.Collections} but ones for which are missing in Java ME.
 *
 * @since 2016/02/28
 */
public class MissingCollections
{
	/** The empty iterator. */
	private static volatile Reference<Iterator> _EMPTY_ITERATOR;
	
	/** The empty list. */
	private static volatile Reference<List> _EMPTY_LIST;
	
	/** The empty set. */
	private static volatile Reference<Set> _EMPTY_SET;
	
	/**
	 * Not initialized.
	 *
	 * @since 2016/02/28
	 */
	private MissingCollections()
	{
	}
	
	/**
	 * Makes a boxed wrapped around a given list.
	 *
	 * @param __v The list to wrap boxed values for.
	 * @return The wrapped array as boxed values or the empty list if the
	 * input array is {@code null} or is empty.
	 * @since 2016/04/11
	 */
	public static List<Integer> boxedList(int... __v)
	{
		if (__v == null || __v.length <= 0)
			return MissingCollections.<Integer>emptyList();
		return new __BoxedIntegerList__(__v);
	}
	
	/**
	 * This returns an iterator which contains nothing.
	 *
	 * @param <V> The type of values to iterate over.
	 * @return The empty iterator.
	 * @since 2016/04/10
	 */
	@SuppressWarnings({"unchecked"})
	public static <V> Iterator<V> emptyIterator()
	{
		// Get reference
		Reference<Iterator> ref = _EMPTY_ITERATOR;
		Iterator rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
			_EMPTY_ITERATOR = new WeakReference<>(
				(rv = new __EmptyIterator__()));
		
		// Return it
		return (Iterator<V>)rv;
	}
	
	/**
	 * This returns a list which contains nothing.
	 *
	 * @param <V> The type of values the list contains.
	 * @return The empty list.
	 * @since 2016/04/10
	 */
	@SuppressWarnings({"unchecked"})
	public static <V> List<V> emptyList()
	{
		// Get reference
		Reference<List> ref = _EMPTY_LIST;
		List rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
			_EMPTY_LIST = new WeakReference<>(
				(rv = new __EmptyList__()));
		
		// Return it
		return (List<V>)rv;
	}
	
	/**
	 * This returns an empty and unmodifiable set.
	 *
	 * @param <V> The element type used by the set.
	 * @return The unmodifiable and empty set.
	 * @since 2016/04/10
	 */
	@SuppressWarnings({"unchecked"})
	public static <V> Set<V> emptySet()
	{
		// Get reference
		Reference<Set> ref = _EMPTY_SET;
		Set rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
			_EMPTY_SET = new WeakReference<>((rv = new __EmptySet__()));
		
		// Return it
		return (Set<V>)rv;
	}
}

