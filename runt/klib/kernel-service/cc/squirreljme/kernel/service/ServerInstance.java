// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.service;

import cc.squirreljme.kernel.packets.Packet;
import cc.squirreljme.runtime.cldc.SystemTask;
import java.security.Permission;

/**
 * This class represents an instance of a service which has been created for
 * a given task from within the kernel.
 *
 * @since 2018/01/03
 */
public abstract class ServerInstance
{
	/** The task this provides an instance for. */
	protected final SystemTask task;
	
	/** The communication to the client. */
	protected final ServicePacketStream stream;
	
	/**
	 * Initializes the base instance.
	 *
	 * @param __task The task this is an instance for.
	 * @param __stream The stream used to communicate with the client.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public ServerInstance(SystemTask __task, ServicePacketStream __stream)
		throws NullPointerException
	{
		if (__task == null || __stream == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.stream = __stream;
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
	public abstract Packet handlePacket(Packet __p)
		throws NullPointerException;
	
	/**
	 * Checks that the specified permission is valid.
	 *
	 * @param __cl The class type of the permission.
	 * @param __n The name of the permission.
	 * @param __a The actions in the permission.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/31
	 */
	protected final void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		if (__cl == null || __n == null || __a == null)
			throw new NullPointerException("NARG");
		
		this.task.checkPermission(__cl, __n, __a);
	}
	
	/**
	 * Checks that the specified permission is valid.
	 *
	 * @param __cl The class type of the permission.
	 * @param __n The name of the permission.
	 * @param __a The actions in the permission.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/31
	 */
	protected final void checkPermission(Class<? extends Permission> __cl,
		String __n, String __a)
		throws NullPointerException, SecurityException
	{
		if (__cl == null || __n == null || __a == null)
			throw new NullPointerException("NARG");
		
		this.checkPermission(__cl.getName(), __n, __a);
	}
	
	/**
	 * Checks that the specified permission is valid.
	 *
	 * @param __p The permission to check.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/31
	 */
	protected final void checkPermission(Permission __p)
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.checkPermission(__p.getClass().getName(), __p.getName(),
			__p.getActions());
	}
}

