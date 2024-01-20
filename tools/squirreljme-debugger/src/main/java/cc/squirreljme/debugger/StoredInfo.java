// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Collection;
import java.util.Map;

/**
 * Information storage for a single type.
 *
 * @param <I> The info type.
 * @since 2024/01/20
 */
public class StoredInfo<I extends Info>
{
	/** The type being stored. */
	protected final InfoType type;
	
	/** The internal item cache. */
	private final Map<Integer, I> _cache =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the stored info.
	 *
	 * @param __type The type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public StoredInfo(InfoType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
	}
	
	/**
	 * Returns all the known values.
	 *
	 * @return All the known values.
	 * @since 2024/01/20
	 */
	@SuppressWarnings("unchecked")
	public I[] all()
	{
		synchronized (this)
		{
			Collection<I> values = this._cache.values();
			return (I[])values.toArray(new Info[values.size()]);
		}
	}
	
	/**
	 * Obtains the given item if it is already known if it is not then it
	 * is created accordingly.
	 *
	 * @param __id The ID of the item to get.
	 * @return The item, this will never be {@code null} as one is always
	 * created.
	 * @since 2024/01/20
	 */
	@SuppressWarnings("unchecked")
	public final I get(int __id)
	{
		Map<Integer, I> cache = this._cache;
		synchronized (this)
		{
			I rv = cache.get(__id);
			if (rv == null)
			{
				rv = (I)this.type.seed(__id);
				cache.put(__id, rv);
			}
			
			return rv;
		}
	}
	
	/**
	 * If the item is known to exist, then it is returned otherwise it is not
	 * created and {@code null} is returned.
	 *
	 * @param __id The ID to check.
	 * @return The state information or {@code null} if not known.
	 * @since 2024/01/20
	 */
	public final I optional(int __id)
	{
		synchronized (this)
		{
			return this._cache.get(__id);
		}
	}
}
