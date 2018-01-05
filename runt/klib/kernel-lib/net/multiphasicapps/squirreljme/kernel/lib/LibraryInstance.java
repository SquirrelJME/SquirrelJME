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

import net.multiphasicapps.squirreljme.kernel.service.ServiceInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;	

/**
 * This manages communication between the client process and the library
 * server.
 *
 * @since 2018/01/05
 */
public final class LibraryInstance
	extends ServiceInstance
{
	/**
	 * Initializes the library instance.
	 *
	 * @param __task The task which has the instance open.
	 * @param __stream The stream for communicating with the task.
	 * @since 2018/01/05
	 */
	public LibraryInstance(SystemTask __task, ServicePacketStream __stream)
	{
		super(__task, __stream);
	}
}

