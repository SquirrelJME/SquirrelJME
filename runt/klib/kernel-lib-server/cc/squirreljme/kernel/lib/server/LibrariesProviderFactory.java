// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.server;

import cc.squirreljme.kernel.service.ClientInstance;
import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.kernel.service.ServiceProvider;
import cc.squirreljme.kernel.service.ServiceProviderFactory;

/**
 * This acts as the base class for the library server which is required to be
 * implemented by native services if they wish to access libraries.
 *
 * @since 2018/01/05
 */
public abstract class LibrariesProviderFactory
	implements ServiceProviderFactory
{
	/**
	 * Creates a library server instance.
	 *
	 * @return The instance of the library server.
	 * @since 2018/01/05
	 */
	protected abstract LibrariesProvider createLibrariesProvider();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public final ServiceProvider createProvider()
	{
		return this.createLibrariesProvider();
	}
}

