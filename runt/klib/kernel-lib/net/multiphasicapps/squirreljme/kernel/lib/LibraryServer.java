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
import net.multiphasicapps.squirreljme.kernel.service.ServiceInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;
import net.multiphasicapps.squirreljme.kernel.service.ServiceServer;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;	

/**
 * This is the base class which manages the library of installed programs
 * on the server.
 *
 * @since 2018/01/05
 */
public abstract class LibraryServer
	extends ServiceServer
{
	/**
	 * Initializes the base library server.
	 *
	 * @param __fc The factory class.
	 * @since 2018/01/05
	 */
	public LibraryServer(Class<? extends LibraryServerFactory> __fc)
	{
		super(LibraryClient.class, __fc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	public final ServiceInstance createInstance(SystemTask __task,
		ServicePacketStream __sps)
		throws NullPointerException
	{
		if (__task == null || __sps == null)
			throw new NullPointerException("NARG");
		
		return new LibraryInstance(__task, __sps);
	}
}

