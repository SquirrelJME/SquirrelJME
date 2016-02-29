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

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a set which cannot be modified.
 *
 * @param <T> The value to store in the set.
 * @since 2016/02/28
 */
final class __UnmodifiableSet__<T>
	extends AbstractSet<T>
{
	/** The set to wrap. */
	protected final Set<T> wrapped;
	
	/**
	 * Initializes a set view which cannot be modified.
	 *
	 * @param __w The set to wrap a view for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/02/28
	 */
	__UnmodifiableSet__(Set<T> __w)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException();
		
		// Wrap it
		wrapped = __w;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/28
	 */
	@Override
	public boolean contains(Object __o)
	{
		return wrapped.contains(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/28
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
				 * @since 2016/02/28
				 */
				@Override
				public boolean hasNext()
				{
					return _base.hasNext();
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/02/28
				 */
				@Override
				public T next()
				{
					return _base.next();
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/02/28
				 */
				@Override
				public void remove()
				{
					throw new UnsupportedOperationException();
				}
			};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/28
	 */
	@Override
	public int size()
	{
		return wrapped.size();
	}
}

