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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represents a collection which cannot be modified.
 *
 * @since 2017/10/09
 */
public final class UnmodifiableCollection<T>
	extends AbstractCollection<T>
{
	/** The collection to wrap. */
	protected final Collection<T> wrapped;
	
	/**
	 * Initializes a collection view which cannot be modified.
	 *
	 * @param __w The collection to wrap a view for.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	private UnmodifiableCollection(Collection<T> __w)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// Wrap it
		wrapped = __w;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/09
	 */
	@Override
	public boolean contains(Object __o)
	{
		return wrapped.contains(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/09
	 */
	@Override
	public boolean isEmpty()
	{
		return wrapped.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/09
	 */
	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
			{
				/** The base iterator. */
				final Iterator<T> _base =
					wrapped.iterator();
				
				/**
				 * {@inheritDoc}
				 * @since 2017/10/09
				 */
				@Override
				public boolean hasNext()
				{
					return _base.hasNext();
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2017/10/09
				 */
				@Override
				public T next()
				{
					return _base.next();
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2017/10/09
				 */
				@Override
				public void remove()
				{
					throw new UnsupportedOperationException("RORO");
				}
			};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/09
	 */
	@Override
	public int size()
	{
		return wrapped.size();
	}
	
	/**
	 * This creates a view of the specified collection which cannot be
	 * modified.
	 *
	 * @param <T> The type of value the set stores.
	 * @param __c The collection to wrap to disable modifications of.
	 * @return An unmodifiable view of the collection.
	 * @since 2017/10/09
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> Collection<T> of(Collection<T> __c)
	{
		// If already one, return that collection
		if (__c instanceof UnmodifiableCollection)
			return __c;
		
		// Use List instead
		else if (__c instanceof List)
			return UnmodifiableList.<T>of((List<T>)__c);
		
		// Use Set instead
		else if (__c instanceof Set)
			return UnmodifiableSet.<T>of((Set<T>)__c);
		
		// Otherwise wrap as a collection
		return new UnmodifiableCollection<T>(__c);
	}
}

