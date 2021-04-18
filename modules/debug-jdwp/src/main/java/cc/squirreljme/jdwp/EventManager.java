// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.event.EventFilter;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.collections.EmptyList;

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
			this._eventById.put(__request.id, __request);
		}
	}
	
	/**
	 * Finds all of the matching requests.
	 * 
	 * @param __controller The controller used.
	 * @param __thread The context thread.
	 * @param __kind The kind of event to look for.
	 * @param __args The arguments to the event call.
	 * @return The valid and found events.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/17
	 */
	protected Iterable<EventRequest> find(JDWPController __controller,
		Object __thread, EventKind __kind, Object... __args)
		throws NullPointerException
	{
		if (__controller == null || __kind == null)
			throw new NullPointerException("NARG");
		
		List<EventRequest> rv = null;
		
		// Go through all previously registered requests
		List<EventRequest> requests = this._eventByKind.get(__kind);
		if (requests != null && !requests.isEmpty())
			for (Iterator<EventRequest> iterator = requests.iterator();
				iterator.hasNext();)
			{
				EventRequest request = iterator.next();
				
				// Are we filtering this?
				EventFilter filter = request.filter;
				if (filter != null)
					throw Debugging.todo();
				
				// Starting a fresh list?
				if (rv == null)
					rv = new LinkedList<>();
				
				// Use this request
				rv.add(request);
				
				// Is this only occuring a specific number of times?
				int occurrencesLeft = request._occurrencesLeft;
				if (occurrencesLeft >= 0)
					if (request._occurrencesLeft <= 0)
						iterator.remove();
			}
		
		// If there are no found events, just use a single instance of the
		// created empty list, otherwise use that given list
		return (rv == null ? EmptyList.<EventRequest>empty() : rv);
	}
}
