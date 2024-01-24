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

/**
 * Represents an event request.
 *
 * @since 2021/03/13
 */
public final class JDWPHostEventRequest
{
	/** The ID of the event. */
	public final int id;
	
	/** The kind of event used. */
	public final JDWPEventKind eventKind;
	
	/** The suspend policy. */
	public final JDWPSuspendPolicy suspendPolicy;
	
	/** The event filter. */
	protected final JDWPHostEventFilter filter;
	
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
	public JDWPHostEventRequest(int __id, JDWPEventKind __eventKind,
		JDWPSuspendPolicy __suspendPolicy, int __occurrenceLimit,
		JDWPHostEventFilter __filter)
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
