// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launcher;

import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KIOException;
import net.multiphasicapps.squirreljme.kernel.KIOSocket;
import net.multiphasicapps.squirreljme.ui.ipc.client.UIDisplayManagerClient;
import net.multiphasicapps.squirreljme.ui.ipc.DMServiceID;
import net.multiphasicapps.squirreljme.ui.UIDisplayManager;

/**
 * This class is the standard launcher which is used to run list programs,
 * launch programs, and perform other various things.
 *
 * Due to the design of SquirrelJME, only a single launcher is required
 * because the heavy lifting of UI code is done by the implementation
 * specific display manager.
 *
 * @since 2016/05/20
 */
public class LauncherInterface
	implements Runnable
{
	/** The kernel to launch and control for. */
	protected final Kernel kernel;
	
	/** The process of the kernel. */
	protected final KernelProcess kernelprocess;
	
	/** The display manager to use to interact with the user. */
	protected final UIDisplayManager displaymanager;
	
	/**
	 * Initializes the launcher interface.
	 *
	 * @param __k The kernel to provide a launcher for.
	 * @throws IllegalStateException If the server could not be found or did
	 * not permit a connection.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public LauncherInterface(Kernel __k)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		KernelProcess kernelprocess = __k.kernelProcess();
		this.kernelprocess = kernelprocess;
		
		// Find the display manager server.
		try
		{
			// Find processes which provide the given service
			KernelProcess[] svs = __k.locateServer(DMServiceID.SERVICE_ID);
			
			// {@squirreljme.error BH02 No process provides the display
			// manager service.}
			if (svs.length <= 0)
				throw new IllegalStateException("BH02");
			
			// Connect to the server
			KIOSocket sock = kernelprocess.connectSocket(
				DMServiceID.SERVICE_ID, svs[0]);
			
			// Initialize the display manager
			this.displaymanager = new UIDisplayManagerClient(sock);
		}
		
		// {@squirreljme.error BH01 Could not create the socket to the server.}
		catch (KIOException e)
		{
			throw new IllegalStateException("BH01", e);
		}
		
		// Setup new launcher thread which runs under the kernel
		kernelprocess.createThread(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/20
	 */
	@Override
	public void run()
	{
		// Infinite loop
		for (;;)
		{
			// Yield to let other threads besides the launcher run
			Thread.yield();
		}
	}
}

