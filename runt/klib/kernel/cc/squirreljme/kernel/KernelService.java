// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

import cc.squirreljme.runtime.cldc.service.ServiceDefinition;
import cc.squirreljme.runtime.cldc.service.ServiceServer;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import java.util.HashMap;
import java.util.Map;

/**
 * This represents a single definition of a kernel service which manages that
 * are defined.
 *
 * @since 2018/03/15
 */
public final class KernelService
{
	/** The lock for this service. */
	protected final Object lock =
		new Object();
	
	/** The service index. */
	protected final int index;
	
	/** The server class. */
	protected final String serverclass;
	
	/** The client class. */
	protected final String clientclass;
	
	/** Active clients for services. */
	private final Map<SystemTask, ServiceServer> _servers =
		new HashMap<>();
	
	/** The initialized definition for this service. */
	private volatile ServiceDefinition _definition;
	
	/**
	 * Initializes the service information.
	 *
	 * @param __dx The service index.
	 * @param __cl The client class.
	 * @param __sv The server class.
	 * @since 2018/03/15
	 */
	public KernelService(int __dx, String __cl, String __sv)
		throws NullPointerException
	{
		if (__cl == null || __sv == null)
			throw new NullPointerException("NARG");
		
		this.index = __dx;
		this.clientclass = __cl;
		this.serverclass = __sv;
	}
	
	/**
	 * Returns the client class.
	 *
	 * @return The client class.
	 * @since 2018/03/15
	 */
	public final String clientClass()
	{
		return this.clientclass;
	}
	
	/**
	 * Returns the class which provides for the client.
	 *
	 * @return The providing provider class.
	 * @since 2018/03/15
	 */
	public final String clientProviderClass()
	{
		return this.__definition().clientProvider().getName();
	}
	
	/**
	 * Returns the index of this service.
	 *
	 * @return The service index.
	 * @since 2018/03/15
	 */
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * Returns the server for the given task.
	 *
	 * @param __t The task to get the service for.
	 * @return The server for the given task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/16
	 */
	public final ServiceServer server(SystemTask __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		Map<SystemTask, ServiceServer> servers = this._servers;
		synchronized (servers)
		{
			ServiceServer rv = servers.get(__t);
			if (rv == null)
				servers.put(__t, (rv = this.__definition().newServer(__t)));
			return rv;
		}
	}
	
	/**
	 * Returns the definition for this server, it will load it if it has not
	 * been specified.
	 *
	 * @return The definition for the service.
	 * @since 2018/03/16
	 */
	private final ServiceDefinition __definition()
	{
		ServiceDefinition rv = this._definition;
		if (rv == null)
			synchronized (this.lock)
			{
				try
				{
					rv = (ServiceDefinition)Class.forName(this.serverclass).
						newInstance();
				}
				
				// {@squirreljme.error AP01 Could not initialize the service
				// definition.}
				catch (ClassCastException|ClassNotFoundException|
					IllegalAccessException| InstantiationException e)
				{
					throw new RuntimeException("AP01", e);
				}
				
				this._definition = rv;
			}
		return rv;
	}
}

