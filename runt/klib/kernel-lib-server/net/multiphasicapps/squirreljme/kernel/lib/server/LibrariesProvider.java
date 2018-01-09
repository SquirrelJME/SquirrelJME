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

import net.multiphasicapps.squirreljme.kernel.lib.client.LibrariesClient;
import net.multiphasicapps.squirreljme.kernel.lib.client.LibrariesClientFactory;
import net.multiphasicapps.squirreljme.kernel.lib.client.Library;
import net.multiphasicapps.squirreljme.kernel.service.ClientInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServerInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;
import net.multiphasicapps.squirreljme.kernel.service.ServiceProvider;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;	

/**
 * This is the base class which manages the library of installed programs
 * on the server.
 *
 * @since 2018/01/05
 */
public abstract class LibrariesProvider
	extends ServiceProvider
{
	/**
	 * Initializes the base library server.
	 *
	 * @since 2018/01/05
	 */
	public LibrariesProvider()
	{
		super(LibrariesClient.class, LibrariesClientFactory.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public final ServerInstance createInstance(SystemTask __task,
		ServicePacketStream __sps)
		throws NullPointerException
	{
		if (__task == null || __sps == null)
			throw new NullPointerException("NARG");
		
		return new LibrariesServer(__task, __sps, this);
	}
	
	/**
	 * Lists the libraries which currently exist.
	 *
	 * @param __mask The mask for the library type.
	 * @return The list of libraries.
	 * @since 2018/01/07
	 */
	public final Library[] list(int __mask)
	{
		throw new todo.TODO();
	}
}

