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
	protected final Class<? extends ClientInstance> clientclass;
	
	/** The factory for creating client instances. */
	protected final Class<? extends ClientInstanceFactory> clientfactoryclass;
	
	/**
	 * Initializes the base server.
	 *
	 * @param __cl The client class which is used.
	 * @param __fc The factory class for creating clients.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	protected ServiceServer(Class<? extends ClientInstance> __cl,
		Class<? extends ClientInstanceFactory> __fc)
		throws NullPointerException
	{
		if (__cl == null || __fc == null)
			throw new NullPointerException("NARG");
		
		this.clientclass = __cl;
		this.clientfactoryclass = __fc;
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
	public abstract ServiceInstance createInstance(SystemTask __task,
		ServicePacketStream __sps)
		throws NullPointerException;
	
	/**
	 * Returns the client class which this server provides an implementation
	 * for.
	 *
	 * @return The client class for the server.
	 * @since 2018/01/05
	 */
	public final Class<? extends ClientInstance> clientClass()
	{
		return this.clientclass;
	}
	
	/**
	 * Returns the factory class for creating client instances.
	 *
	 * @return The factory class for creating client instances.
	 * @since 2018/01/05
	 */
	public final Class<? extends ClientInstanceFactory> clientFactoryClass()
	{
		return this.clientfactoryclass;
	}
}

