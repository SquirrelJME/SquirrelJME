// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

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
	
	/**
	 * Initializes the event request.
	 * 
	 * @param __id The identifier of the request.
	 * @param __eventKind The kind of event this is.
	 * @param __suspendPolicy The suspension policy.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public EventRequest(int __id, EventKind __eventKind,
		SuspendPolicy __suspendPolicy)
		throws NullPointerException
	{
		if (__eventKind == null || __suspendPolicy == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.eventKind = __eventKind;
		this.suspendPolicy = __suspendPolicy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int id()
	{
		return this.id;
	}
}
