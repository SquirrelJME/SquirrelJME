// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores event handlers.
 *
 * @since 2024/01/26
 */
public class EventHandlers
{
	/** Event handler storage. */
	private final Map<Integer, EventHandler<?>> _eventHandlers =
		new LinkedHashMap<>();
	
	/**
	 * Returns the event handler for the given request.
	 *
	 * @param __requestId The request to get.
	 * @return The handler for the given event, assuming it exists.
	 * @since 2024/01/20
	 */
	public EventHandler<?> eventHandler(int __requestId)
	{
		synchronized (this)
		{
			return this._eventHandlers.get(__requestId);
		}
	}
	
	/**
	 * Stores the event handler.
	 *
	 * @param __eventId The event ID.
	 * @param __handler The handler to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/26
	 */
	public void put(int __eventId, EventHandler<?> __handler)
		throws NullPointerException
	{
		if (__handler == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._eventHandlers.put(__eventId, __handler);
		}
	}
}
