// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import java.io.PrintStream;
import javax.microedition.swm.InstallErrorCodes;
import javax.microedition.swm.Suite;
import javax.microedition.swm.SuiteInstallListener;
import javax.microedition.swm.SuiteInstallStage;
import javax.microedition.swm.SuiteManagementTracker;

/**
 * This class allows for suites to be waited upon when they are installed.
 *
 * @since 2017/12/27
 */
class __SuiteInstallWaiter__
	implements SuiteInstallListener
{
	/** Where messages on progress are printed to. */
	protected final PrintStream out;
	
	/** The installed suite. */
	private volatile Suite _suite;
	
	/** Installation error code. */
	private volatile InstallErrorCodes _error;
	
	/**
	 * Initializes the waiter.
	 *
	 * @param __out The stream where progress is printed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/27
	 */
	__SuiteInstallWaiter__(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * Returns the installed suite.
	 *
	 * @return The installed suite.
	 * @throws RuntimeException If the suite did not install properly.
	 * @since 2017/12/27
	 */
	public Suite get()
	{
		for (;;)
			synchronized (this)
			{
				// If a suite was installed use that
				Suite suite = this._suite;
				if (suite != null)
					return suite;
				
				// {@squirreljme.error AU13 The suite installation failed with
				// the given error code. (The error code)}
				InstallErrorCodes error = this._error;
				if (error != null)
					throw new RuntimeException(String.format("AU13 %s",
						error));
				
				// Installation can be notified from another thread
				try
				{
					this.wait(1_000);
				}
				catch (InterruptedException e)
				{
				}
			}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public void installationDone(InstallErrorCodes __err,
		SuiteManagementTracker __tracker)
	{
		this.out.printf("Finished: %s%n", __err);
		
		// One thread will be locked on this
		synchronized (this)
		{
			// Set suite and code
			this._error = __err;
			this._suite = __tracker.getSuite();
		
			// Signal self that the suite was installed
			this.notify();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public void updateStatus(SuiteManagementTracker __tracker,
		SuiteInstallStage __stage, int __percent)
	{
		this.out.printf("%s: %d%%%n", __stage, __percent);
	}
}

