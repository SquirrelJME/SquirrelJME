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
 * This class represents an instance of a service which has been created for
 * a given task from within the kernel.
 *
 * @since 2018/01/03
 */
public abstract class ServiceInstance
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
	public ServiceInstance(SystemTask __task, ServicePacketStream __stream)
		throws NullPointerException
	{
		if (__task == null || __stream == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.stream = __stream;
	}
}

