// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.swm.JarStreamSupplier;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Set;

/**
 * This performs the installation and tracks progress.
 *
 * @since 2017/12/28
 */
final class __SuiteTracker__
	extends SuiteManagementTracker
{
	/** Bytes per percent threshold. */
	private static final int _PERCENT_THRESHOLD =
		4096;
	
	/** The suite when it is installed. */
	volatile Suite _suite;
	
	/**
	 * Initializes the tracker.
	 *
	 * @param __i The owning installer.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	__SuiteTracker__(SuiteInstaller __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Setup thread which performs the actual installation
		Thread thread = new Thread(new __Runner__(this, __i),
			"SquirrelJME-Suite-Installer");
		SystemCall.EASY.setDaemonThread(thread);
		thread.start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/24
	 */
	@Override
	public Suite getSuite()
	{
		return this._suite;
	}
	
	/**
	 * This thread performs the installation.
	 *
	 * @since 2017/12/28
	 */
	private static final class __Runner__
		implements Runnable
	{
		/** The tracker which is given the suite when finished. */
		protected final __SuiteTracker__ tracker;
		
		/** The supplier for the JAR data. */
		protected final JarStreamSupplier supplier;
	
		/** Listeners for suites. */
		private final Set<SuiteInstallListener> _listeners;
		
		/**
		 * Initializes the runner.
		 *
		 * @param __tracker The tracker where the resulting suite is placed.
		 * @param __i The installer used.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/12/28
		 */
		private __Runner__(__SuiteTracker__ __tracker, SuiteInstaller __i)
			throws NullPointerException
		{
			if (__tracker == null || __i == null)
				throw new NullPointerException("NARG");
			
			this.tracker = __tracker;
			this.supplier = __i._supplier;
			this._listeners = __i._listeners;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/12/28
		 */
		@Override
		public void run()
		{
			try
			{
				// Read the JAR data stream 
				byte[] data;
				__update(SuiteInstallStage.DOWNLOAD_DATA, 0);
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
					InputStream is = this.supplier.get())
				{
					// Read in source data
					byte[] buf = new byte[512];
					for (int count = 0, last = 0;;)
					{
						int rc = is.read(buf);
					
						if (rc < 0)
							break;
					
						baos.write(buf, 0, rc);
					
						// Update progress for the first initial set of blocks
						if (last < 99)
						{
							count += rc;
							int now = count / _PERCENT_THRESHOLD;
							if (last != now && now <= 99)
							{
								__update(SuiteInstallStage.DOWNLOAD_DATA, now);
								last = now;
							}
						}
					}
				
					// Get the entire data buffer
					baos.flush();
					data = baos.toByteArray();
				}
			
				// Could not read the JAR
				catch (IOException e)
				{
					__done(InstallErrorCodes.IO_FILE_ERROR);
					return;
				}
				__update(SuiteInstallStage.DOWNLOAD_DATA, 100);
				
				// Need the library manager
				__SystemSuiteManager__ ssm =
					(__SystemSuiteManager__)ManagerFactory.getSuiteManager();
				LibrariesClient manager = ssm._manager;
			
				// Send it to the kernel
				__update(SuiteInstallStage.VERIFYING, 0);
				LibraryInstallationReport report = manager.install(
					data, 0, data.length);
			
				// Failed
				int error = report.error();
				if (error != 0)
				{
					// Determine the error code
					error--;
					InstallErrorCodes[] codes = InstallErrorCodes.values();
					InstallErrorCodes code = (error >= 0 &&
						error < codes.length ? codes[error] :
						InstallErrorCodes.OTHER_ERROR);
					
					// This will be the only chance to print the installation
					// report
					// {@squirreljme.error DG07 Failed to install the program
					// due to the specified error. (The error code; The more
					// detailed message associated with the error)}
					System.err.printf("DG07 %s %s%n", code, report.message());
					
					// Mark as done
					__done(code);
					return;
				}
			
				// Set the suite used
				this.tracker._suite = new Suite(report.library());
			
				// Did not fail, but report it anyway
				__update(SuiteInstallStage.VERIFYING, 100);
			
				// Finished
				__update(SuiteInstallStage.DONE, 100);
				__done(InstallErrorCodes.NO_ERROR);
			}
			
			// Oops
			catch (Throwable t)
			{
				// Just print the trace
				t.printStackTrace();
				
				// And use some other error code to indicate failure 
				__done(InstallErrorCodes.OTHER_ERROR);
			}
		}
		
		/**
		 * Called when installation has finished, potentially with an error.
		 *
		 * @param __code The error code.
		 * @since 2017/12/28
		 */
		private void __done(InstallErrorCodes __code)
		{
			__SuiteTracker__ tracker = this.tracker;
			for (SuiteInstallListener l : this._listeners)
				try
				{
					l.installationDone(__code, tracker);
				}
				catch (Exception e)
				{
				}
		}
		
		/**
		 * Updates the current install stage.
		 *
		 * @param __stage The current installation stage.
		 * @param __pct The percentage complete.
		 * @since 2017/12/28
		 */
		private void __update(SuiteInstallStage __stage, int __pct)
		{
			__SuiteTracker__ tracker = this.tracker;
			for (SuiteInstallListener l : this._listeners)
				try
				{
					l.updateStatus(tracker, __stage, __pct);
				}
				catch (Exception e)
				{
				}
		}
	}
}

