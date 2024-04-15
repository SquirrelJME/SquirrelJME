// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This is a set which is synchronized against a lock.
 *
 * @param <V> The set value.
 * @since 2019/05/05
 */
public final class SynchronizedSet<V>
	extends AbstractSet<V>
{
	/** The locking object. */
	protected final Object lock;
	
	/** The backing set. */
	protected final Set<V> set;
	
	/**
	 * Initializes the synchronized set.
	 *
	 * @param __lock The locking object.
	 * @param __set The set object.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/05
	 */
	public SynchronizedSet(Object __lock, Set<V> __set)
		throws NullPointerException
	{
		if (__lock == null || __set == null)
			throw new NullPointerException("NARG");
		
		this.lock = __lock;
		this.set = __set;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final Iterator<V> iterator()
	{
		Object lock = this.lock;
		synchronized (lock)
		{
			return new SynchronizedIterator<V>(lock, this.set.iterator());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final int size()
	{
		synchronized (this.lock)
		{
			return this.set.size();
		}
	}
}

