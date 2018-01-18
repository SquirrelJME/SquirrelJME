// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.trust.client;

import cc.squirreljme.kernel.packets.Packet;
import cc.squirreljme.kernel.service.ClientInstance;
import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.runtime.cldc.SystemTrustGroup;

/**
 * This is the client which manages trusts which are stored on the remote end
 * of the server.
 *
 * @since 2018/01/17
 */
public final class TrustClient
	extends ClientInstance
{
	/**
	 * Initializes the trust client.
	 *
	 * @param __ps The packet stream to the server.
	 * @since 2018/01/17
	 */
	public TrustClient(ServicePacketStream __ps)
	{
		super(__ps);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	protected Packet handlePacket(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		switch (__p.type())
		{
				// {@squirreljme.error BI01 Unknown packet. (The packet)}
			default:
				throw new IllegalArgumentException(
					String.format("BI01 %s", __p));
		}
	}
	
	/**
	 * Returns a trust group for a trusted program which goes by the
	 * specified name.
	 *
	 * @param __name The midlet name.
	 * @param __vendor The midlet vendor.
	 * @return The trust group for the midlet.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/18
	 */
	public final SystemTrustGroup trustedTrust(String __name,
		String __vendor)
		throws NullPointerException
	{
		if (__name == null || __vendor == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns a trust group for an untrusted program which goes by the
	 * specified name.
	 *
	 * @param __name The midlet name.
	 * @param __vendor The midlet vendor.
	 * @return The trust group for the midlet.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/18
	 */
	public final SystemTrustGroup untrustedTrust(String __name,
		String __vendor)
		throws NullPointerException
	{
		if (__name == null || __vendor == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

