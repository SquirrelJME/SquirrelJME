// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
	protected final InfoKind type;
	
	/** The internal item cache. */
	private final Map<Integer, I> _cache =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the stored info.
	 *
	 * @param __type The type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/20
	 */
	public StoredInfo(InfoKind __type)
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
	public I[] all(DebuggerState __state)
	{
		synchronized (this)
		{
			// Get all the known values, perform garbage collection on them
			Collection<I> values = this._cache.values();
			if (__state != null)
				this.__gc(__state);
			
			I[] result = (I[])values.toArray(new Info[values.size()]);
			
			// Perform sorting on the values, where possible, although it is
			// very iffy
			Arrays.sort(result);
			
			// Use everything
			return result;
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
		return this.get(null, __id);
	}
	
	/**
	 * Obtains the given item if it is already known if it is not then it
	 * is created accordingly.
	 *
	 * @param __state Optional state, if passed then there will be an implicit
	 * update to the added item.
	 * @param __id The ID of the item to get.
	 * @return The item, if this returns {@code null} then the item was likely
	 * disposed of.
	 * @since 2024/01/20
	 */
	@SuppressWarnings("unchecked")
	public final I get(DebuggerState __state, int __id)
	{
		Map<Integer, I> cache = this._cache;
		synchronized (this)
		{
			I rv = cache.get(__id);
			if (rv == null)
			{
				rv = (I)this.type.seed(__id);
				
				// Perform update of the object?
				if (__state != null)
					if (!rv.update(__state, null))
					{
						// Debug
						Debugging.debugNote("Initial was disposed?");
						
						return null;
					}
				
				// Store into the cache
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
	
	/**
	 * Performs garbage collection on the given values.
	 *
	 * @param __state The state being ran in.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	private void __gc(DebuggerState __state)
		throws NullPointerException
	{
		synchronized (this)
		{
			Map<Integer, I> items = this._cache;
			for (Iterator<Map.Entry<Integer, I>> it =
				 items.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<Integer, I> entry = it.next();
				I item = entry.getValue();
				
				// If the item is known to be disposed, remove it
				if (item.isDisposed())
					it.remove();
				
				// Request an update for this item, if it gets disposed of then
				// remove that as well
				else if (!item.update(__state, null))
					it.remove();
			}
		}
	}
}
