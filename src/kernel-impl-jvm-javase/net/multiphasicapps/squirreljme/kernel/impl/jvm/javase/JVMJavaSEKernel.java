// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm.javase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.multiphasicapps.squirreljme.kernel.impl.autoboot.AutoBootKernel;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KIOException;
import net.multiphasicapps.squirreljme.kernel.KIOSocket;
import net.multiphasicapps.squirreljme.ui.ipc.DMServiceID;
import net.multiphasicapps.squirreljme.ui.ipc.server.UIDisplayManagerServer;

/**
 * This contains the launcher used by the host Java SE system.
 *
 * @since 2016/05/14
 */
public class JVMJavaSEKernel
	extends AutoBootKernel
{
	/**
	 * This initializes the launcher which uses an existing full Java SE JVM.
	 *
	 * @param __args The arguments to the launcher.
	 * @since 2016/05/14
	 */
	public JVMJavaSEKernel(String... __args)
	{
		// Must always exist
		if (__args == null)
			__args = new String[0];
		
		// Setup the display manager
		SwingDisplayManager sdm = new SwingDisplayManager();
		
		// And the display manager server
		try
		{
			KernelProcess kp = kernelProcess();
			UIDisplayManagerServer uisv = new UIDisplayManagerServer(
				kp.createSocket(DMServiceID.SERVICE_ID), sdm);
			kp.createThread(uisv);
		}
		
		// {@squirreljme.error AZ01 Could not create the display manager socket
		// to display future user interfaces.}
		catch (KIOException e)
		{
			throw new RuntimeException("AZ01", e);
		}
		
		// Finished booting
		bootFinished(JVMJavaSEKernel.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public void quitKernel()
	{
		// Can quit
		System.exit(0);
	}
}

