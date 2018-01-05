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

import net.multiphasicapps.squirreljme.kernel.service.ClientInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;
import net.multiphasicapps.squirreljme.kernel.service.ServiceServer;
import net.multiphasicapps.squirreljme.kernel.service.ServiceServerFactory;

/**
 * This acts as the base class for the library server which is required to be
 * implemented by native services if they wish to access libraries.
 *
 * @since 2018/01/05
 */
public abstract class LibraryServerFactory
	implements ServiceServerFactory
{
	/**
	 * Creates a library server instance.
	 *
	 * @return The instance of the library server.
	 * @since 2018/01/05
	 */
	protected abstract LibraryServer createLibraryServer();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public final ClientInstance createClient(ServicePacketStream __sps)
		throws NullPointerException
	{
		if (__sps == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public final ServiceServer createServer()
	{
		return this.createLibraryServer();
	}
}

