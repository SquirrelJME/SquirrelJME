// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.service;

import cc.squirreljme.runtime.cldc.task.SystemTask;

/**
 * This defines a service which is available for the kernel to use.
 *
 * @since 2018/03/02
 */
@Deprecated
public abstract class ServiceDefinition
{
	/** The provider for the client class. */
	@Deprecated
	protected final Class<? extends ServiceClientProvider> clientprovider;
	
	/**
	 * Initializes the base service definition.
	 *
	 * @param __cp The client provider.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/02
	 */
	@Deprecated
	public ServiceDefinition(Class<? extends ServiceClientProvider> __cp)
		throws NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		this.clientprovider = __cp;
	}
	
	/**
	 * Initializes a new server for the given task.
	 *
	 * @param __task The task to provide a service for.
	 * @return The server for the given task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/02
	 */
	@Deprecated
	public abstract ServiceServer newServer(SystemTask __task)
		throws NullPointerException;
	
	/**
	 * Returns the class which is used to initialize the client.
	 *
	 * @return The class for initializing the client.
	 * @since 2018/03/02
	 */
	@Deprecated
	public final Class<? extends ServiceClientProvider> clientProvider()
	{
		return this.clientprovider;
	}
}

