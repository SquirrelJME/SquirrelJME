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
import cc.squirreljme.kernel.packets.PacketReader;
import cc.squirreljme.kernel.service.ServerInstance;
import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.kernel.trust.TrustPacketTypes;
import cc.squirreljme.kernel.trust.TrustPermission;
import cc.squirreljme.runtime.cldc.SystemTask;
import cc.squirreljme.runtime.cldc.SystemTrustGroup;

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
			
				// Check if a trust exists
			case TrustPacketTypes.CHECK_TRUST_ID:
				return this.__checkTrustId(__p);
			
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
	 * @since 2018/02/12
	 */
	private final Packet __checkTrustId(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Read ID
		int dx = __p.readInteger(0);
		
		// Need to be allowed to do this
		super.checkPermission(TrustPermission.class, Integer.toString(dx),
			TrustPermission.ACTION_CHECK_TRUST_ID);
		
		// Only give success if it exists
		Packet rv = __p.respond(4);
		rv.writeInteger(0, (this.provider.byIndex(dx) == null ? -1 : dx));
		return rv;
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
		
		// Read name and vendor
		PacketReader r = __p.createReader();
		String name = r.readString(),
			vendor = r.readString();
		
		// Need to be allowed to do this
		super.checkPermission(TrustPermission.class, name + ":" + vendor,
			TrustPermission.ACTION_GET_UNTRUSTED);
		
		// Obtain the trust (or create it)
		SystemTrustGroup trust = this.provider.untrustedTrust(name, vendor);
		
		// Tell client about the trust
		Packet rv = __p.respond(4);
		rv.writeInteger(0, (trust == null ? -1 : trust.index()));
		return rv;
	}
}

