// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import __squirreljme.IPCServer;

/**
 * This is a controller which hosts an IPC server which is then used.
 *
 * @since 2016/05/31
 */
public class KernelController
{
	/**
	 * {@squirreljme.serviceid 2 This is the service which allows the launcher
	 * to control the kernel that it provides a user interface to. The purpose
	 * of this service is to grant the ability of the launcher to run in user
	 * space and potentially permit alternative launchers which may be used.
	 * Another benefit of user-space launchers is that the rerecording
	 * interpreters can be more deterministic when they are not in kernel
	 * space.}
	 */
	public static final int CONTROLLER_SERVICE_ID =
		2;
	
	/** The kernel to control. */
	protected final Kernel kernel;
	
	/** The IPC server for this controller. */
	protected final IPCServer server;
	
	/**
	 * Intiailizes the kernel controller.
	 *
	 * @param __k The kernel to control.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/31
	 */
	public KernelController(Kernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		
		// Setup the server socket
		this.server = new IPCServer(__k.ipcAlternative(),
			CONTROLLER_SERVICE_ID);
	}
}

