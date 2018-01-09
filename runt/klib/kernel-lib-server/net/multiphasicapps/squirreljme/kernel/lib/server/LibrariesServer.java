// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.lib.server;

import net.multiphasicapps.squirreljme.kernel.lib.client.Library;
import net.multiphasicapps.squirreljme.kernel.lib.client.PacketTypes;
import net.multiphasicapps.squirreljme.kernel.packets.Packet;
import net.multiphasicapps.squirreljme.kernel.service.ServerInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;	

/**
 * This manages communication between the client process and the library
 * server.
 *
 * @since 2018/01/05
 */
public final class LibrariesServer
	extends ServerInstance
{
	/** The class where permissions are checked against. */
	private static final String _PERMISSION_CLASS =
		"javax.microedition.swm.SWMPermission";
	
	/** The library server since libraries are managed in unison. */
	protected final LibrariesProvider provider;
	
	/**
	 * Initializes the library instance.
	 *
	 * @param __task The task which has the instance open.
	 * @param __stream The stream for communicating with the task.
	 * @param __p The provider for the actual library service.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public LibrariesServer(SystemTask __task, ServicePacketStream __stream,
		LibrariesProvider __p)
		throws NullPointerException
	{
		super(__task, __stream);
		
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.provider = __p;
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
			case PacketTypes.LIST_PROGRAMS:
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
		
		// Need permission to do this
		__checkPermission("manageSuite");
		
		// Read the library set
		Library[] libs = this.provider.list(__p.readInteger(0));
		
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
	
	/**
	 * Checks whether the specified permission is valid.
	 *
	 * @param __act The action to check.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permission is not granted.
	 * @since 2018/01/09
	 */
	private final void __checkPermission(String __act)
		throws NullPointerException, SecurityException
	{
		if (__act == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		boolean sameclient;
		if (true)
			throw new todo.TODO();
		
		this.task.checkPermission(LibraryServer._PERMISSION_CLASS,
			(sameclient ? : ));*/
	}
}

