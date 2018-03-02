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

import cc.squirreljme.runtime.cldc.service.ServiceCaller;
import cc.squirreljme.runtime.cldc.trust.InvalidTrustException;
import cc.squirreljme.runtime.cldc.trust.SystemTrustGroup;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the client which manages trusts which are stored on the remote end
 * of the server.
 *
 * @since 2018/01/17
 */
public final class TrustClient
{
	/** Local trusts. */
	private final Map<Integer, Reference<__LocalTrust__>> _trusts =
		new HashMap<>();
	
	/**
	 * Initializes the trust client.
	 *
	 * @param __sc The caller for the trust client.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/17
	 */
	public TrustClient(ServiceCaller __sc)
		throws NullPointerException
	{
		if (__sc == null)
			throw new NullPointerException("NARG");
		
		this.caller = __sc;
	}
	
	/**
	 * Returns the trust group for the given index.
	 *
	 * @param __dx The index to get.
	 * @return The trust group or {@code null} if it does not exist.
	 * @since 2018/02/12
	 */
	public final SystemTrustGroup byIndex(int __dx)
	{
		Integer idx = __dx;
		
		// Could be cached
		Map<Integer, Reference<__LocalTrust__>> trusts = this._trusts;
		synchronized (trusts)
		{
			Reference<__LocalTrust__> ref = trusts.get(idx);
			__LocalTrust__ rv;
			
			if (ref == null || null == (rv = ref.get()))
			{
				ServicePacketStream stream = this.stream;
				try (Packet p = PacketFarm.createPacket(
					TrustPacketTypes.CHECK_TRUST_ID, 4))
				{
					p.writeInteger(0, __dx);
					
					try (Packet r = stream.send(p, true))
					{
						int val = r.readInteger(0);
						if (val < 0)
							return null;
					}
				}
				
				// Cache it
				trusts.put(idx, new WeakReference<>((rv = this.__map(__dx))));
			}
			
			return rv;
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
		
		int rvdx;
		
		Map<Integer, Reference<__LocalTrust__>> trusts = this._trusts;
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
					rvdx = r.readInteger(0);
					if (rvdx < 0)
						throw new InvalidTrustException(
							String.format("BI02 %s %s", __name, __vendor));
				}
			}
			
			return this.__map(rvdx);
		}
	}
	
	/**
	 * Maps the index to a local trust.
	 *
	 * @param __dx The trust to map.
	 * @return The mapped trust.
	 * @since 2018/02/10
	 */
	private final __LocalTrust__ __map(int __dx)
	{
		Integer idx = __dx;
		
		// Lock
		Map<Integer, Reference<__LocalTrust__>> trusts = this._trusts;
		synchronized (trusts)
		{
			Reference<__LocalTrust__> ref = trusts.get(idx);
			__LocalTrust__ rv;
			
			if (ref == null || null == (rv = ref.get()))
				trusts.put(idx, new WeakReference<>((rv =
					new __LocalTrust__(__dx, this))));
			
			return rv;
		}
	}
}

