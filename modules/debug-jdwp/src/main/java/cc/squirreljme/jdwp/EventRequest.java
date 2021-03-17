// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import java.util.Arrays;

/**
 * Represents an event request.
 *
 * @since 2021/03/13
 */
public final class EventRequest
	implements JDWPId
{
	/** The ID of the event. */
	public final int id;
	
	/** The kind of event used. */
	public final EventKind eventKind;
	
	/** The suspend policy. */
	public final SuspendPolicy suspendPolicy;
	
	/** Modifiers for events. */
	private final EventModifier[] _modifiers;
	
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
	 * @param __modifiers Modifiers for the event.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public EventRequest(int __id, EventKind __eventKind,
		SuspendPolicy __suspendPolicy, int __occurrenceLimit,
		EventModifier... __modifiers)
		throws NullPointerException
	{
		if (__eventKind == null || __suspendPolicy == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.eventKind = __eventKind;
		this.suspendPolicy = __suspendPolicy;
		this._occurrencesLeft = __occurrenceLimit;
		this._modifiers = (__modifiers == null ? new EventModifier[0] :
			__modifiers.clone());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
	
	/**
	 * Returns the modifiers for this event.
	 * 
	 * @return The modifiers for this event.
	 * @since 2021/03/14
	 */
	public final EventModifier[] modifiers()
	{
		return this._modifiers.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/16
	 */
	@Override
	public final String toString()
	{
		return String.format(
			"EventRequest[id=%d,kind=%s,suspend=%s,left=%d,mods=%s]",
			this.id,
			this.eventKind,
			this.suspendPolicy,
			this._occurrencesLeft,
			Arrays.asList(this._modifiers));
	}
}
