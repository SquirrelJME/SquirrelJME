// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is an iterator which locks on an object before accesses are made.
 *
 * @param <V> The type of values to iterate over.
 * @since 2019/05/05
 */
public final class SynchronizedIterator<V>
	implements Iterator<V>
{
	/** The locking object. */
	protected final Object lock;
	
	/** The backing iterator. */
	protected final Iterator<V> iterator;
	
	/**
	 * Initializes the synchronized iterator.
	 *
	 * @param __lock The locking object.
	 * @param __it The iterator object.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/05
	 */
	public SynchronizedIterator(Object __lock, Iterator<V> __it)
		throws NullPointerException
	{
		if (__lock == null || __it == null)
			throw new NullPointerException("NARG");
		
		this.lock = __lock;
		this.iterator = __it;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final boolean hasNext()
	{
		synchronized (this.lock)
		{
			return this.iterator.hasNext();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final V next()
		throws NoSuchElementException
	{
		synchronized (this.lock)
		{
			return this.iterator.next();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final void remove()
		throws IllegalStateException, UnsupportedOperationException
	{
		synchronized (this.lock)
		{
			this.iterator.remove();
		}
	}
}

