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

import net.multiphasicapps.squirreljme.kernel.ipc.base.PacketTypes;
import net.multiphasicapps.squirreljme.kernel.packets.Packet;
import net.multiphasicapps.squirreljme.kernel.packets.PacketFarm;
import net.multiphasicapps.squirreljme.kernel.packets.PacketStream;
import net.multiphasicapps.squirreljme.kernel.packets.RemoteThrowable;

/**
 * This is a wrapped packet stream which .
 *
 * @since 2018/01/05
 */
public final class ServicePacketStream
{
	/** The stream to send packets through. */
	protected final PacketStream stream;
	
	/** The service index. */
	protected final int index;
	
	/**
	 * Initializes the service packet stream.
	 *
	 * @param __ps The stream to the server.
	 * @param __dx The index of the service to send under.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public ServicePacketStream(PacketStream __ps, int __dx)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		this.stream = __ps;
		this.index = __dx;
	}
	
	/**
	 * Returns the packet farm for creating packets.
	 *
	 * @return The packet farm.
	 * @since 2018/01/05
	 */
	public final PacketFarm farm()
	{
		return this.stream.farm();
	}
	
	/**
	 * Sends the given packet to the remote server.
	 *
	 * @param __p The packet to send to the remote.
	 * @return The result from the packet, if it does not produce a result
	 * then {@code null} is returned.
	 * @throws NullPointerException On null arguments.
	 * @throws RemoteException If the remote end threw an exception.
	 * @since 2018/01/05
	 */
	public final Packet send(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		PacketStream stream = this.stream;
		
		int plen = __p.length(),
			ptype = __p.type();
		boolean wantresponse = (ptype > 0);
		
		// Build the appropriate packet that can fit this packet
		try (Packet p = stream.farm().create(
			(wantresponse ? PacketTypes.SERVICE_WITH_RESULT :
				PacketTypes.SERVICE_NO_RESULT), 4 + 4 + plen))
		{
			// Write service details
			p.writeShort(0, this.index);
			p.writeShort(2, ptype);
			p.writeInteger(4, plen);
			p.writePacketData(8, __p);
			
			// Send packet to server
			return stream.send(p);
		}
	}
}

