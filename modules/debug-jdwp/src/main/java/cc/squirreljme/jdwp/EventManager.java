// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Manager for Debugger Events.
 *
 * @since 2021/03/14
 */
public final class EventManager
{
	/** Event mappings by Kind. */
	private final Map<EventKind, List<EventRequest>> _eventByKind =
		new EnumTypeMap<EventKind, List<EventRequest>>(
			EventKind.class, EventKind.values());
	
	/** Event mapping by Id. */
	private final Map<Integer, EventRequest> _eventById =
		new LinkedHashMap<>();
	
	/**
	 * Adds an event request for later event handling.
	 * 
	 * @param __request The request to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public void addEventRequest(EventRequest __request)
		throws NullPointerException
	{
		if (__request == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("JDWP: Adding event %s", __request);
		
		Map<EventKind, List<EventRequest>> eventByKind = this._eventByKind;
		synchronized (this)
		{
			// Get list of the event
			List<EventRequest> list = eventByKind.get(__request.eventKind);
			if (list == null)
				eventByKind.put(__request.eventKind,
					(list = new LinkedList<>()));
			
			// Map events
			list.add(__request);
			this._eventById.put(__request.debuggerId(), __request);
		}
	}
	
	/**
	 * Gets the given event.
	 * 
	 * @param __kind The kind of event to get.
	 * @param __matchers Matchers for modifiers.
	 * @return The request, will return {@code null} if not requested.
	 * @since 2021/03/14
	 */
	public EventRequest get(EventKind __kind,
		EventModifierMatcher... __matchers)
	{
		synchronized (this)
		{
			List<EventRequest> requests = this._eventByKind.get(__kind);
			if (requests == null)
				return null;
			
			// Find a matching one
			for (EventRequest request : requests)
			{
				// Go through all the matchers and find any modifiers which
				// are compatible with matching and do not match
				if (__matchers != null && __matchers.length > 0)
					for (EventModifierMatcher matcher : __matchers)
						for (EventModifier mod : request.modifiers())
							if (matcher.mayMatch(mod))
								if (!matcher.isMatch(mod))
									return null;
				
				// Limit number of times this happens?
				if (request._occurrencesLeft > 0)
				{
					// Did this run out?
					if (--request._occurrencesLeft <= 0)
					{
						requests.remove(request);
						return null;
					}
				} 
				
				// Use this one
				return request;
			}
		}
		
		// Not found
		return null;
	}
}
