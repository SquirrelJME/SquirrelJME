// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui.extra;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages tracking of arbitrary objects and {@link ExtraState}.
 *
 * @since 2026/09/25
 */
@SquirrelJMEVendorApi
public final class ExtraStateManager
{
	/** Extra state pairs. */
	private static final List<__Pair__> _pairs =
		new LinkedList<>();
	
	/**
	 * Not used.
	 *
	 * @since 2026/09/25
	 */
	private ExtraStateManager()
	{
	}
	
	/**
	 * Binds the given object to the given extra state.
	 *
	 * @param <E> The class this state is bound under.
	 * @param __o The object to bind.
	 * @param __as The class this state is bound under.
	 * @param __state The state to bind to.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/09/25
	 */
	@SquirrelJMEVendorApi
	public static final <E extends ExtraState> void bind(Object __o,
		Class<E> __as, E __state)
		throws NullPointerException
	{
		if (__o == null || __state == null)
			throw new NullPointerException("NARG");
		
		// Only a pair needs to be added, luckily linked lists are a thing
		List<__Pair__> pairs = ExtraStateManager._pairs;
		synchronized (ExtraStateManager.class)
		{
			pairs.add(new __Pair__(__o, __state));
		}
	}
	
	/**
	 * Locates the bound extra state, if any exists, and it has not been
	 * garbage collected. The result of this should be cached where possible.
	 *
	 * @param <E> The class to cast as.
	 * @param __as The class to cast as.
	 * @param __o The input object to get the state from.
	 * @return The resultant extra state or {@code null} exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/09/25
	 */
	@SquirrelJMEVendorApi
	public static final <E extends ExtraState> E locate(Class<E> __as,
		Object __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// This is a bit slower as we need to go through the linked list
		// and find the appropriate object and matching pair
		List<__Pair__> pairs = ExtraStateManager._pairs;
		synchronized (ExtraStateManager.class)
		{
			// We want an iterator as we can just remove any instances
			// that get GCed
			Iterator<__Pair__> it = pairs.iterator();
			while (it.hasNext())
			{
				// Was this instance GCed? Then remove the pair so it gets
				// cleaned up accordingly
				__Pair__ pair = it.next();
				Object key = pair._ref.get();
				if (key == null)
				{
					it.remove();
					continue;
				}
				
				// Is this even the object we want?
				if (__o != key)
					continue;
				
				// Is this the wanted state?
				ExtraState rv = pair._state;
				if (__as.isInstance(rv))
					return __as.cast(rv);
			}
		}
		
		// None found
		return null;
	}
}
