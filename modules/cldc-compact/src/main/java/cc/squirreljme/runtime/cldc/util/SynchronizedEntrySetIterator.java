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
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This is a synchronized iterator over entry set iterators.
 *
 * @param <K> The key.
 * @param <V> The value.
 * @since 2019/05/05
 */
public class SynchronizedEntrySetIterator<K, V>
	implements Iterator<Map.Entry<K, V>>
{
	/** The locking object. */
	protected final Object lock;
	
	/** The backing iterator. */
	protected final Iterator<Map.Entry<K, V>> iterator;
	
	/**
	 * Initializes the synchronized iterator.
	 *
	 * @param __lock The locking object.
	 * @param __it The iterator object.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/05
	 */
	public SynchronizedEntrySetIterator(Object __lock,
		Iterator<Map.Entry<K, V>> __it)
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
	public final Map.Entry<K, V> next()
		throws NoSuchElementException
	{
		Object lock = this.lock;
		synchronized (lock)
		{
			return new SynchronizedMapEntry<K, V>(lock, this.iterator.next());
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

