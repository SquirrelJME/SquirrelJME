// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * This is a set which cannot be modified.
 *
 * @param <T> The value to store in the set.
 * @since 2016/02/28
 */
@UnmodifiableView
public final class UnmodifiableSet<T>
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
	private UnmodifiableSet(Set<T> __w)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// Wrap it
		this.wrapped = __w;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/28
	 */
	@Override
	public boolean contains(Object __o)
	{
		return this.wrapped.contains(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean isEmpty()
	{
		return this.wrapped.isEmpty();
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
				final Iterator<T> _base = UnmodifiableSet.this.wrapped.iterator();
				
				/**
				 * {@inheritDoc}
				 * @since 2016/02/28
				 */
				@Override
				public boolean hasNext()
				{
					return this._base.hasNext();
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/02/28
				 */
				@Override
				public T next()
				{
					return this._base.next();
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/02/28
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
	 * @since 2016/02/28
	 */
	@Override
	public int size()
	{
		return this.wrapped.size();
	}
	
	/**
	 * This creates a view of the specified set which cannot be modified.
	 *
	 * @param <T> The type of value the set stores.
	 * @param __s The set to wrap to disable modifications of.
	 * @return An unmodifiable view of the set.
	 * @since 2016/02/28
	 */
	public static <T> Set<T> of(Set<T> __s)
	{
		// If already one, return that set
		if (__s instanceof UnmodifiableSet)
			return __s;
		
		// Otherwise create a new one
		return new UnmodifiableSet<T>(__s);
	}
}

