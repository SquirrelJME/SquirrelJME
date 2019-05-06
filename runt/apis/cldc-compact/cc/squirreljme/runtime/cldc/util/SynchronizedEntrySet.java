// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This is an entry set which is synchronized against a lock.
 *
 * @param <K> The entry keys.
 * @param <V> The entry values.
 * @since 2019/05/05
 */
public final class SynchronizedEntrySet<K, V>
	extends AbstractSet<Map.Entry<K, V>>
{
	/** The locking object. */
	protected final Object lock;
	
	/** The backing set. */
	protected final Set<Map.Entry<K, V>> set;
	
	/**
	 * Initializes the synchronized entry set.
	 *
	 * @param __lock The locking object.
	 * @param __set The entry set object.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/05
	 */
	public SynchronizedEntrySet(Object __lock, Set<Map.Entry<K, V>> __set)
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
	public final Iterator<Map.Entry<K, V>> iterator()
	{
		Object lock = this.lock;
		synchronized (lock)
		{
			return new SynchronizedEntrySetIterator<K, V>(
				lock, this.set.iterator());
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
