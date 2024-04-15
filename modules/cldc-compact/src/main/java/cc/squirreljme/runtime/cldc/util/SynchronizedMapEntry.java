// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.Map;

/**
 * This is a synchronized map entry.
 *
 * @param <K> The key.
 * @param <V> The value.
 * @since 2019/05/05
 */
public final class SynchronizedMapEntry<K, V>
	implements Map.Entry<K, V>
{
	/** The locking object. */
	protected final Object lock;
	
	/** The backing entry. */
	protected final Map.Entry<K, V> entry;
	
	/**
	 * Initializes the synchronized entry.
	 *
	 * @param __lock The locking object.
	 * @param __ent The entry object.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/05
	 */
	public SynchronizedMapEntry(Object __lock, Map.Entry<K, V> __ent)
		throws NullPointerException
	{
		if (__lock == null || __ent == null)
			throw new NullPointerException("NARG");
		
		this.lock = __lock;
		this.entry = __ent;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final boolean equals(Object __o)
	{
		synchronized (this.lock)
		{
			return this.entry.equals(__o);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final K getKey()
	{
		synchronized (this.lock)
		{
			return this.entry.getKey();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final V getValue()
	{
		synchronized (this.lock)
		{
			return this.entry.getValue();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final int hashCode()
	{
		synchronized (this.lock)
		{
			return this.entry.hashCode();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final V setValue(V __v)
	{
		synchronized (this.lock)
		{
			return this.entry.setValue(__v);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public final String toString()
	{
		synchronized (this.lock)
		{
			return this.entry.toString();
		}
	}
}

