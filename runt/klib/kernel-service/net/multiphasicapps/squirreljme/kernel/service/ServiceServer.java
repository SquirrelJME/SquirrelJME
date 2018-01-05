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

import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;

/**
 * This is the base class for services which exist on the kernel side which
 * are used to initialize instances for each client as needed.
 *
 * @since 2018/01/03
 */
public abstract class ServiceServer
{
	/** The client class this provides a server for. */
	protected final Class<?> clientclass;
	
	/**
	 * Initializes the base server.
	 *
	 * @param __cl The client class which is used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	protected ServiceServer(Class<?> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.clientclass = __cl;
	}
	
	/**
	 * Returns the client class which this server provides an implementation
	 * for.
	 *
	 * @return The client class for the server.
	 * @since 2018/01/05
	 */
	public final Class<?> clientClass()
	{
		return this.clientclass;
	}
	
	/**
	 * Creates a new client which uses the given packet stream to
	 * communicate with the service instance in the kernel.
	 *
	 * @param __sps The stream to send packets into.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public final Object createClient(ServicePacketStream __sps)
		throws NullPointerException
	{
		if (__sps == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Creates an instance of the server for usage by client facilities.
	 *
	 * @param __task The task to provide an instance for.
	 * @param __sps The packet stream which communicates with the client.
	 * @return The instance of the server for server usage.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public final ServiceInstance createInstance(SystemTask __task,
		ServicePacketStream __sps)
		throws NullPointerException
	{
		if (__task == null || __sps == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

