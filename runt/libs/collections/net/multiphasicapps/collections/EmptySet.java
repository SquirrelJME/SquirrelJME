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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a set which cannot be changed and has no entries.
 *
 * @since 2016/04/10
 */
public final class EmptySet
	extends AbstractSet
{
	/** The empty set. */
	private static Reference<Set> _EMPTY_SET;
	
	/**
	 * Initializes the empty set.
	 *
	 * @since 2016/04/10
	 */
	private EmptySet()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean contains(Object __o)
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean containsAll(Collection __c)
	{
		return __c.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean isEmpty()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public Iterator iterator()
	{
		return EmptyIterator.empty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public int size()
	{
		return 0;
	}
	
	/**
	 * This returns an empty and unmodifiable set.
	 *
	 * @param <V> The element type used by the set.
	 * @return The unmodifiable and empty set.
	 * @since 2016/04/10
	 */
	@SuppressWarnings({"unchecked"})
	public static <V> Set<V> empty()
	{
		// Get reference
		Reference<Set> ref = _EMPTY_SET;
		Set rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
			_EMPTY_SET = new WeakReference<>((rv = new EmptySet()));
		
		// Return it
		return (Set<V>)rv;
	}
}

