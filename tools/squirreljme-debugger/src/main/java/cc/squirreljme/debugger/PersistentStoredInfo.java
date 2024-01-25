// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Persistently stored information.
 *
 * @param <I> The information to store.
 * @since 2024/01/25
 */
public class PersistentStoredInfo<I extends Info>
	extends StoredInfo<I>
{
	/** The internal item cache. */
	private final Map<JDWPId, I> _cache =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the stored info.
	 *
	 * @param __type The type to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/25
	 */
	public PersistentStoredInfo(InfoKind __type)
		throws NullPointerException
	{
		super(__type);
	}
	
	/**
	 * Returns all the known values.
	 *
	 * @param __state The debugger state.
	 * @param __basis The basis type.
	 * @return All the known values.
	 * @since 2024/01/20
	 */
	@Override
	@SuppressWarnings("unchecked")
	public I[] all(DebuggerState __state, Class<? extends Info[]> __basis)
	{
		synchronized (this)
		{
			// Get all the known values, perform garbage collection on them
			Collection<I> values = this._cache.values();
			this.__gc(__state);
			
			I[] result = (I[])Arrays.copyOf(
				values.toArray(new Info[values.size()]),
				values.size(),
				__basis);
			
			// Update all values that we can
			for (I item : result)
				item.update(__state, null);
			
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
	 * @param __state Optional state, if passed then there will be an implicit
	 * update to the added item.
	 * @param __id The ID of the item to get.
	 * @param __extra Any extra values if this needs to be seeded.
	 * @return The item, if this returns {@code null} then the item was likely
	 * disposed of.
	 * @since 2024/01/20
	 */
	@Override
	@SuppressWarnings("unchecked")
	public I get(DebuggerState __state, JDWPId __id, Object... __extra)
	{
		if (__id == null)
			throw new NullPointerException("NARG");
		
		Map<JDWPId, I> cache = this._cache;
		synchronized (this)
		{
			I rv = cache.get(__id);
			if (rv == null)
			{
				rv = (I)this.type.seed(__state, __id, __extra);
				
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
	 * Performs garbage collection on the given values.
	 *
	 * @param __state The current debugger state.
	 * @since 2024/01/21
	 */
	private void __gc(DebuggerState __state)
	{
		synchronized (this)
		{
			Map<JDWPId, I> items = this._cache;
			for (Iterator<Map.Entry<JDWPId, I>> it =
				 items.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<JDWPId, I> entry = it.next();
				I item = entry.getValue();
				
				// If the item is known to be disposed, remove it
				if (item.isDisposed())
					it.remove();
				
				// Request an update for this item, if it gets disposed of then
				// remove that as well
				else if (__state != null &&
					!item.update(__state, null))
					it.remove();
			}
		}
	}
}
