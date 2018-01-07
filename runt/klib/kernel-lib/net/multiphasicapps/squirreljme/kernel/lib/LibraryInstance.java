// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.lib;

import net.multiphasicapps.squirreljme.kernel.packets.Packet;
import net.multiphasicapps.squirreljme.kernel.service.ServiceInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;	

/**
 * This manages communication between the client process and the library
 * server.
 *
 * @since 2018/01/05
 */
public final class LibraryInstance
	extends ServiceInstance
{
	/** The library server since libraries are managed in unison. */
	protected final LibraryServer server;
	
	/**
	 * Initializes the library instance.
	 *
	 * @param __task The task which has the instance open.
	 * @param __stream The stream for communicating with the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public LibraryInstance(SystemTask __task, ServicePacketStream __stream,
		LibraryServer __sv)
		throws NullPointerException
	{
		super(__task, __stream);
		
		if (__sv == null)
			throw new NullPointerException("NARG");
		
		this.server = __sv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public Packet handlePacket(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		switch (__p.type())
		{
			case LibraryPacketTypes.LIST_PROGRAMS:
				return this.__list(__p);
			
				// {@squirreljme.error BC02 Unknown packet. (The packet)}
			default:
				throw new IllegalArgumentException(
					String.format("BC02 %s", __p));
		}
	}
	
	/**
	 * Returns the list of libraries which are available.
	 *
	 * @return The library list.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/06
	 */
	private final Packet __list(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Read the library set
		Library[] libs = this.server.list(__p.readInteger(0));
		
		// The response is just the library identifiers
		int n = libs.length;
		Packet rv = __p.respond(2 + (2 * n));
		
		// Write library count
		rv.writeShort(0, n);
		
		// Write the library indexes
		for (int i = 0, o = 2; i < n; i++, o += 2)
			rv.writeShort(o, libs[i].index());
		
		return rv;
	}
}

