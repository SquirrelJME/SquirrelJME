// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.JDWPSuspendPolicy;
import cc.squirreljme.jdwp.host.event.JDWPHostEventFilter;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.EnumTypeMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.collections.EmptyList;

/**
 * Manager for Debugger Events.
 *
 * @since 2021/03/14
 */
public final class JDWPHostEventManager
{
	/** Event mappings by Kind. */
	private final Map<JDWPEventKind, List<JDWPHostEventRequest>> _eventByKind =
		new EnumTypeMap<JDWPEventKind, List<JDWPHostEventRequest>>(
			JDWPEventKind.class, JDWPEventKind.values());
	
	/** Event mapping by Id. */
	private final Map<Integer, JDWPHostEventRequest> _eventById =
		new HashMap<>();
	
	/** Unconditional events. */
	private final Map<JDWPEventKind, JDWPHostEventRequest> _unconditional =
		new EnumTypeMap<JDWPEventKind, JDWPHostEventRequest>(
			JDWPEventKind.class, JDWPEventKind.values());
	
	/**
	 * Initializes the event manager.
	 * 
	 * @since 2021/07/05
	 */
	public JDWPHostEventManager()
	{
		// Unconditional breakpoints
		this._unconditional.put(JDWPEventKind.BREAKPOINT,
			new JDWPHostEventRequest(0, JDWPEventKind.BREAKPOINT,
				JDWPSuspendPolicy.EVENT_THREAD, -1,
				null));
		
		// Suspend all on VM start
		this.addEventRequest(new JDWPHostEventRequest(0,
			JDWPEventKind.VM_START, JDWPSuspendPolicy.ALL, -1,
			null));
	}
	
	/**
	 * Adds an event request for later event handling.
	 * 
	 * @param __request The request to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public void addEventRequest(JDWPHostEventRequest __request)
		throws NullPointerException
	{
		if (__request == null)
			throw new NullPointerException("NARG");
		
		// Debug
		if (JDWPHostController._DEBUG)
			Debugging.debugNote("JDWP: Adding event %s", __request);
		
		Map<JDWPEventKind, List<JDWPHostEventRequest>> eventByKind =
			this._eventByKind;
		synchronized (this)
		{
			// Get list of the event
			List<JDWPHostEventRequest> list = eventByKind.get(
				__request.eventKind);
			if (list == null)
				eventByKind.put(__request.eventKind,
					(list = new LinkedList<>()));
			
			// Map events
			list.add(__request);
			this._eventById.put(__request.id, __request);
		}
	}
	
	/**
	 * Clears all events of the given kind.
	 * 
	 * @param __kind The kind of events to clear.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/30
	 */
	public void clear(JDWPEventKind __kind)
		throws NullPointerException
	{
		if (__kind == null)
			throw new NullPointerException("NARG");
		
		// Check if there are actual events to clear
		List<JDWPHostEventRequest> events = this._eventByKind.get(__kind);
		if (events == null)
			return;
		
		// Get all events to clear
		JDWPHostEventRequest[] clear;
		synchronized (this)
		{
			clear = events.toArray(new JDWPHostEventRequest[events.size()]);
		}
		
		// Delete all of them
		for (JDWPHostEventRequest event : clear)
			this.delete(event.id);
	}
	
	/**
	 * Clears all events.
	 * 
	 * @since 2021/04/30
	 */
	public void clearAll()
	{
		for (JDWPEventKind kind : JDWPEventKind.values())
			this.clear(kind);
	}
	
	/**
	 * Deletes the event by the given ID.
	 * 
	 * @param __id The event ID.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/18
	 */
	public void delete(int __id)
		throws NullPointerException
	{
		synchronized (this)
		{
			// If this request is found, remove from the respective maps
			// and lists
			JDWPHostEventRequest request = this._eventById.get(__id);
			if (request != null)
			{
				this._eventByKind.get(request.eventKind).remove(request);
				this._eventById.remove(__id);
			}
		}
	}
	
	/**
	 * Finds all of the matching requests.
	 * 
	 * @param __controller The controller used.
	 * @param __thread The context thread.
	 * @param __unconditional Is this an unconditional event?
	 * @param __kind The kind of event to look for.
	 * @param __args The arguments to the event call.
	 * @return The valid and found events, will be an empty list if none
	 * were found.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/17
	 */
	protected Iterable<JDWPHostEventRequest> find(
		JDWPHostController __controller, Object __thread,
		boolean __unconditional, JDWPEventKind __kind, Object... __args)
		throws NullPointerException
	{
		if (__controller == null || __kind == null)
			throw new NullPointerException("NARG");
		
		// Go through all previously registered requests
		List<JDWPHostEventRequest> requests = this._eventByKind.get(__kind);
		if (requests == null)
			if (__unconditional)
				return this.__unconditional(__controller, __kind);
			else
				return EmptyList.<JDWPHostEventRequest>empty();
		
		// Lock since this could be used by many threads
		List<JDWPHostEventRequest> rv = null;
		synchronized (this)
		{
			// Nothing?
			if (requests.isEmpty())
				if (__unconditional)
					return this.__unconditional(__controller, __kind);
				else
					return EmptyList.<JDWPHostEventRequest>empty();
			
			// Find matching events
			for (Iterator<JDWPHostEventRequest> iterator = requests.iterator();
				 iterator.hasNext();)
			{
				JDWPHostEventRequest request = iterator.next();
				
				// Are we filtering this? And does this meet this?
				JDWPHostEventFilter filter = request.filter;
				if (filter != null && !filter.meets(__controller, __thread,
					__kind, __args))
					continue;
				
				// Starting a fresh list?
				if (rv == null)
					rv = new LinkedList<>();
				
				// Use this request
				rv.add(request);
				
				// Is this only occurring a specific number of times?
				// Remove after this has ran out
				int occurrencesLeft = request._occurrencesLeft;
				if (occurrencesLeft >= 0)
					if ((--request._occurrencesLeft) <= 0)
						iterator.remove();
			}
		}
		
		// Unconditional event?
		if (__unconditional && (rv == null || rv.isEmpty()))
			return this.__unconditional(__controller, __kind);
		
		// If there are no found events, just use a single instance of the
		// created empty list, otherwise use that given list
		return (rv == null ? EmptyList.<JDWPHostEventRequest>empty() : rv);
	}
	
	/**
	 * Returns an unconditional event if one is available.
	 * 
	 * @param __controller The controller that is attached to the debugger.
	 * @param __kind The kind of event to return.
	 * @return The unconditional events.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/07/06
	 */
	private Iterable<JDWPHostEventRequest> __unconditional(
		JDWPHostController __controller,
		JDWPEventKind __kind)
		throws NullPointerException
	{
		if (__controller == null || __kind == null)
			throw new NullPointerException("NARG");
		
		// Was this ever registered?
		JDWPHostEventRequest request = this._unconditional.get(__kind);
		if (request != null)
		{
			Collection<JDWPHostEventRequest> rv = new ArrayList<>();
			
			rv.add(request);
			
			return rv;
		}
		
		// Just use this event
		return EmptyList.<JDWPHostEventRequest>empty();
	}
}
