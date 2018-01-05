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

/**
 * This class represents a client connection to a server instance.
 *
 * @since 2018/01/05
 */
public abstract class ClientInstance
{
	/** Packet stream to the server. */
	protected final ServicePacketStream stream;
	
	/**
	 * Initializes the base client interface.
	 *
	 * @param __sps The packet stream to the server.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public ClientInstance(ServicePacketStream __sps)
		throws NullPointerException
	{
		if (__sps == null)
			throw new NullPointerException("NARG");
		
		this.stream = __sps;
	}
}

