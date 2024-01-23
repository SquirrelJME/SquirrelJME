// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows JDWP and otherwise to link to a given set of objects to be able to
 * find them by ID and otherwise.
 * 
 * This class uses references to refer to target objects so that they may
 * be garbage collected and otherwise accordingly.
 *
 * @param <T> The type of values to store.
 * @since 2021/03/13
 */
public final class JDWPHostLinker<T>
{
	/** Returns the type of the linked data. */
	protected final Class<T> type;
	
	/** Links for IDs to existing object types. */
	private final Map<Integer, Reference<T>> _links =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the linker.
	 * 
	 * @param __type The type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public JDWPHostLinker(Class<T> __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
	}
	
	/**
	 * Returns the object by the given ID.
	 * 
	 * @param __id The ID to get.
	 * @return The instance for the given ID, may be {@code null}.
	 * @since 2021/03/13
	 */
	public final T get(int __id)
	{
		// Protect these!
		Map<Integer, Reference<T>> links = this._links;
		synchronized (this)
		{
			// Get by the id
			Reference<T> ref = links.get(__id);
			if (ref == null)
				return null;
			
			// If we had a reference but it got GCed, clear it out to
			// remove wasted space
			T rv = ref.get();
			if (rv == null)
				links.remove(__id);
			
			return rv;
		}
	}
	
	/**
	 * Puts in a new type.
	 * 
	 * @param __t The type to put.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public final void put(T __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// The ID used
		Integer id = System.identityHashCode(__t);
		
		// Protect these!
		Map<Integer, Reference<T>> links = this._links;
		synchronized (this)
		{
			Reference<T> ref = links.get(id);
			if (ref == null || ref.get() == null)
				ref = links.put(id, new WeakReference<>(__t));
			
			// Did this change? This should not normally happen unless two
			// objects share the same identity hash code but are not the
			// same objects... maybe the VM reallocated?
			if (ref != null)
			{
				T val = ref.get();
				if (val != null && val != __t)
					Debugging.debugNote(String.format(
						"Change of reference: %s -> %s",
						val, __t));
			}
		}
	}
	
	/**
	 * Returns the estimated size.
	 * 
	 * @return The estimated size.
	 * @since 2021/03/13
	 */
	public final int sizeEstimate()
	{
		synchronized (this)
		{
			return this._links.size();
		}
	}
	
	/**
	 * Returns all of the linked state values.
	 * 
	 * @return All the values in the linked state, this is a copy of the values
	 * and changes will not reflect the underlying linked values.
	 * @since 2021/03/13
	 */
	@SuppressWarnings("unchecked")
	public final List<T> values()
	{
		List<T> result = new ArrayList<>(this.sizeEstimate());
		
		// Protect these!
		Map<Integer, Reference<T>> links = this._links;
		synchronized (this)
		{
			for (Iterator<Map.Entry<Integer, Reference<T>>> it =
				links.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry<Integer, Reference<T>> entry = it.next();
				
				// Use the value if it still exists
				Reference<T> ref = entry.getValue();
				T val = ref.get();
				
				// Has this been collected?
				if (val != null)
					result.add(val);
				
				// If it got GCed, remove it
				else
					it.remove();
			}
		}
		
		// Used fixed array
		return Arrays.asList(
			result.<T>toArray((T[])new Object[result.size()]));
	}
}
