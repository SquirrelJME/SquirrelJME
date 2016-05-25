// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.autoboot;

import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KIOException;
import net.multiphasicapps.squirreljme.launcher.LauncherInterface;
import net.multiphasicapps.squirreljme.ui.UIManager;

/**
 * This is the autoboot kernel which instead of having the standard launcher
 * being initializes in the implementation code, it is instead initialized
 * in this class and utilized for the kernel and the user to use.
 *
 * When the kernel boot has finished, the loop does not terminate until the
 * kernel has no process remaining or the kernel quits.
 *
 * @since 2016/05/20
 */
public abstract class AutoBootKernel
	extends Kernel
{
	/**
	 * Initializes the auto-booting kernel.
	 *
	 * @since 2016/05/20
	 */
	public AutoBootKernel()
	{
		// Finished booting
		bootFinished(AutoBootKernel.class);
	}
	
	/**
	 * Returns the array of class unit provides which are available for
	 * usage.
	 *
	 * @return The array of class unit provides.
	 * @since 2016/06/25
	 */
	protected abstract ClassUnitProvider[] classUnitProviders();
	
	/**
	 * Returns the display manager to use when displaying the launcher
	 * interface.
	 *
	 * @return The display manager to use.
	 * @since 2016/05/21
	 */
	protected abstract UIManager getDisplayManager();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	protected void bootFinishRunner()
	{
		// Call super code first
		super.bootFinishRunner();
		
		// Setup launcher
		LauncherInterface li = new LauncherInterface(this,
			getDisplayManager(), classUnitProviders());
		
		// Block until all workers are terminated
		for (;;)
		{
			// Kernel loop
			try
			{
				untilProcessless();
		
				// Would normally terminate
				return;
			}
		
			// Interrupted, yield and retry
			catch (InterruptedException e)
			{
				Thread.yield();
			}
		}
	}
}

