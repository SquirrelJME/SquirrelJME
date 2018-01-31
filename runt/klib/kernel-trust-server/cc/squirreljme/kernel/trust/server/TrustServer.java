// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.trust.server;

import cc.squirreljme.kernel.packets.Packet;
import cc.squirreljme.kernel.service.ServerInstance;
import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.kernel.trust.TrustPacketTypes;
import cc.squirreljme.runtime.cldc.SystemTask;

/**
 * This implements the server for a trust service and provides the
 * bridge between the client and the provider.
 *
 * @since 2018/01/17
 */
public final class TrustServer
	extends ServerInstance
{
	/** The owning trust provider. */
	protected final TrustProvider provider;
	
	/**
	 * Initializes the trust server.
	 *
	 * @param __p The owning provider.
	 * @param __t The task this is running for.
	 * @param __ps The stream from the client.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/17
	 */
	TrustServer(TrustProvider __p, SystemTask __t, ServicePacketStream __ps)
		throws NullPointerException
	{
		super(__t, __ps);
		
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.provider = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	public Packet handlePacket(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		switch (__p.type())
		{
				// Get an untrusted trust
			case TrustPacketTypes.GET_UNTRUSTED_TRUST:
				return this.__untrustedTrust(__p);
			
				// {@squirreljme.error BK01 Unknown packet. (The packet)}
			default:
				throw new IllegalArgumentException(
					String.format("BK01 %s", __p));
		}
	}
	
	/**
	 * Returns the untrusted trust for the given client.
	 *
	 * @param __p The packet.
	 * @return The resulting packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	private final Packet __untrustedTrust(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

