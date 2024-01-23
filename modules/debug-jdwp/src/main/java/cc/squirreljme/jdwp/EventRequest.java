// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.event.EventFilter;

/**
 * Represents an event request.
 *
 * @since 2021/03/13
 */
public final class EventRequest
{
	/** The ID of the event. */
	public final int id;
	
	/** The kind of event used. */
	public final JDWPEventKind eventKind;
	
	/** The suspend policy. */
	public final SuspendPolicy suspendPolicy;
	
	/** The event filter. */
	protected final EventFilter filter;
	
	/** The number of occurrences left. */
	volatile int _occurrencesLeft;
	
	/**
	 * Initializes the event request.
	 * 
	 * @param __id The identifier of the request.
	 * @param __eventKind The kind of event this is.
	 * @param __suspendPolicy The suspension policy.
	 * @param __occurrenceLimit The number of times this may occur,
	 * negative values means to occur indefinitely.
	 * @param __filter Filter for the event.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public EventRequest(int __id, JDWPEventKind __eventKind,
		SuspendPolicy __suspendPolicy, int __occurrenceLimit,
		EventFilter __filter)
		throws NullPointerException
	{
		if (__eventKind == null || __suspendPolicy == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.eventKind = __eventKind;
		this.suspendPolicy = __suspendPolicy;
		this.filter = __filter;
		this._occurrencesLeft = __occurrenceLimit;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/16
	 */
	@Override
	public final String toString()
	{
		return String.format(
			"EventRequest[id=%d,kind=%s,suspend=%s,left=%d,filter=%s]",
			this.id,
			this.eventKind,
			this.suspendPolicy,
			this._occurrencesLeft,
			this.filter);
	}
}
