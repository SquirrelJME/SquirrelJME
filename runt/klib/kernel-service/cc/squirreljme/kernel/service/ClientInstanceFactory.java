// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.service;

/**
 * This is used to create instances of {@link ClientInstance} which is used
 * to communicate with a remote service.
 *
 * @since 2018/01/05
 */
public abstract class ClientInstanceFactory
{
	/**
	 * Creates a new client which uses the given packet stream to
	 * communicate with the service instance in the kernel.
	 *
	 * @param __sps The stream to send packets into.
	 * @return The client service.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	protected abstract ClientInstance initializeClient(
		ServicePacketStream __sps)
		throws NullPointerException;
	
	/**
	 * Initializes a new client handler for the remote server but additionally
	 * provides a means of sending events to the client for handling.
	 *
	 * @param __sps The stream to send packets into.
	 * @return The client service with an accessible packet sending interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public final ClientInstanceAccessor createClient(ServicePacketStream __sps)
		throws NullPointerException
	{
		if (__sps == null)
			throw new NullPointerException("NARG");
		
		// Create instance but also provide a handler to send events to
		return new ClientInstanceAccessor(this.initializeClient(__sps));
	}	
}

