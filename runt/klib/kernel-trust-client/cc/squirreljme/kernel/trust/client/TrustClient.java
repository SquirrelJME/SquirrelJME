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
import cc.squirreljme.kernel.packets.PacketFarm;
import cc.squirreljme.kernel.packets.PacketReader;
import cc.squirreljme.kernel.packets.PacketWriter;
import cc.squirreljme.kernel.service.ClientInstance;
import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.kernel.trust.InvalidTrustException;
import cc.squirreljme.kernel.trust.TrustPacketTypes;
import cc.squirreljme.runtime.cldc.SystemTrustGroup;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the client which manages trusts which are stored on the remote end
 * of the server.
 *
 * @since 2018/01/17
 */
public final class TrustClient
	extends ClientInstance
{
	/** Local trusts. */
	private final Map<Integer, __LocalTrust__> _trusts =
		new HashMap<>();
	
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
		
		Map<Integer, __LocalTrust__> trusts = this._trusts;
		synchronized (trusts)
		{
			ServicePacketStream stream = this.stream;
			try (Packet p = PacketFarm.createPacket(
				TrustPacketTypes.GET_UNTRUSTED_TRUST))
			{
				PacketWriter w = p.createWriter();
				
				w.writeString(__name);
				w.writeString(__vendor);
				
				try (Packet r = stream.send(p, true))
				{
					// {@squirreljme.error BI02 The specified untrusted
					// trust group is not valid. (The name; The vendor)}
					int dx = r.readInteger(0);
					if (dx < 0)
						throw new InvalidTrustException(
							String.format("BI02 %s %s", __name, __vendor));
					
					throw new todo.TODO();
				}
			}
		}
	}
}

