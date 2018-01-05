// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.service;

import net.multiphasicapps.squirreljme.kernel.packets.Packet;

/**
 * This class enables access to the client instance along with allowing the
 * handler to be called without exposing the method in the implementing
 * class.
 *
 * @since 2018/01/05
 */
public final class ClientInstanceAccessor
{
	/** The client instance. */
	protected final ClientInstance instance;
	
	/**
	 * Initializes the client instance accessor.
	 *
	 * @param __ci The client to be accessed.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public ClientInstanceAccessor(ClientInstance __ci)
		throws NullPointerException
	{
		if (__ci == null)
			throw new NullPointerException("NARG");
		
		this.instance = __ci;
	}
	
	/**
	 * Returns the instance of the client.
	 *
	 * @return The client instance.
	 * @since 2018/01/05
	 */
	public final ClientInstance instance()
	{
		return this.instance;
	}
	
	/**
	 * Handles a packet which has been sent from the remote side.
	 *
	 * @param __p The packet which was transmitted.
	 * @return The resulting packet, if no result is expected then this may be
	 * {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public final Packet handlePacket(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return this.instance.__handlePacket(__p);
	}
}

