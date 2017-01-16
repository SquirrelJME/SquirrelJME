// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This is the SquirrelJME kernel which uses a kernel interface to interface
 * with the native system, this class manages processes and threads within
 * the virtual machine.
 *
 * @since 2016/10/31
 */
public final class Kernel
	implements KernelThreadListener
{
	/** The suite manager. */
	protected final KernelSuiteManager suitemanager;
	
	/** The manager for threads. */
	protected final KernelThreadManager threadmanager;
	
	/**
	 * Processes running in the kernel, a list is used because generally there
	 * will not be that many processes running at any one time due to the
	 * limited nature of systems SquirrelJME runs on.
	 */
	private final List<KernelProcess> _processes =
		new ArrayList<>();
	
	/**
	 * Initializes the kernel using the given set of parameters.
	 *
	 * @param __kb The builder used for the kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/31
	 */
	Kernel(KernelBuilder __kb)
		throws NullPointerException
	{
		// Check
		if (__kb == null)
			throw new NullPointerException("NARG");
		
		// Used later
		KernelThreadManager threadmanager;
		KernelSuiteManager suitemanager;
		KernelLaunchParameters launchparms;
		
		// Lock here so that the builder can be reused after a build is
		// complete, perhaps by another thread
		synchronized (__kb._lock)
		{
			// {@squirreljme.error BH01 No kernel thread manager was
			// specified.}
			this.threadmanager = (threadmanager = Objects.<KernelThreadManager>
				requireNonNull(__kb._threadmanager, "BH01"));
		
			// {@squirreljme.error BH02 No kernel suite manager was specified.}
			this.suitemanager = (suitemanager = Objects.<KernelSuiteManager>
				requireNonNull(__kb._suitemanager, "BH02"));
				
			// {@squirreljme.error BH03 The kernel was not initialized with any
			// launch parameters.}
			launchparms = Objects.<KernelLaunchParameters>requireNonNull(
				__kb._launchparms, "BH03");
		}
		
		// When the state of threads changes, tell the kernel
		threadmanager.setThreadListener(this);
		
		// Setup the launcher process
		KernelProcess lp = null;
		try
		{
			// Parse the set of loaded APIs to see which ones are available
			// for processes to use (and the launcher that is to be used, if
			// any)
			SystemInstalledSuites sis = suitemanager.systemSuites();
			String main = sis.launcherMainClass();
			
			// {@squirreljme.error BH04 The main class for the launcher could
			// not be found.}
			if (main == null)
				throw new RuntimeException("BH04");
			
			// Create process, which gets its main
			lp = __newProcess(main, new SuiteDataAccessor[0],
				launchparms.getCommandLine());
		}
		
		// Failed to create it, destroy it
		catch (RuntimeException|Error e)
		{
			// Destroy the process but do not override failure state
			if (lp != null)
				try
				{
					lp.close();
				}
			
				// Suppress it
				catch (Throwable t)
				{
					e.addSuppressed(t);
				}
			
			// Re-throw
			throw e;
		}
	}
	
	/**
	 * Is the kernel exiting?
	 *
	 * @return {@code true} if the kernel is exiting.
	 * @since 2016/11/03
	 */
	public final boolean isExiting()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Runs the kernel loop.
	 *
	 * @return {@code true} if the kernel has not yet exited.
	 * @throws InterruptedException If the loop operation was interrupted.
	 * @since 2016/10/31
	 */
	public final boolean run()
		throws InterruptedException
	{
		// Run until the kernel exits
		KernelThreadManager threadmanager = this.threadmanager;
		for (boolean exitkernel = false; !exitkernel; exitkernel = isExiting())
		{
			// Run all threads
			try
			{
				threadmanager.runThreads();
			}
		
			// This may be thrown if an interrupt was generated for the kernel
			// to handle. It will handle it and then go back to running the
			// kernel loop.
			catch (InterruptedException e)
			{
				// Check if there are any interrupts to be handled at this
				// stage, if any are handled then the exception is consumed
				if (true)
					throw new Error("TODO");
				
				// Otherwise throw above
				throw e;
			}
		}
		
		// If this point reached, the kernel exits
		return false;
	}
	
	/**
	 * Returns the suite manager.
	 *
	 * @return The suite manager.
	 * @since 2017/01/05
	 */
	public final KernelSuiteManager suiteManager()
	{
		return this.suitemanager;
	}
	
	/**
	 * Creates a new process owned by this kernel and returns it.
	 *
	 * @param __main The main entry class for the process.
	 * @param __cp Suites that make up the class path, note that system
	 * suites are not included.
	 * @param __args Main program arguments.
	 * @return The process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	private KernelProcess __newProcess(String __main,
		SuiteDataAccessor[] __cp, String[] __args)
		throws NullPointerException
	{
		// Check
		if (__main == null)
			throw new NullPointerException("NARG");
		
		// Use nothing by default if not specified
		if (__cp == null)
			__cp = new SuiteDataAccessor[0];
		if (__args == null)
			__args = new String[0];
		
		// Setup new process
		KernelThreadManager threadmanager = this.threadmanager;
		KernelProcess rv = null;
		try
		{
			// Create it
			rv = threadmanager.createProcess(this, __cp);
			
			// {@squirreljme.error BH07 Process was assigned to a different
			// kernel during construction.}
			if (rv.kernel() != this)
				throw new ProcessCreationException("BH07");
			
			if (true)
				throw new Error("TODO");
			
			// Register process
			List<KernelProcess> processes = this._processes;
			synchronized (processes)
			{
				processes.add(rv);
			}
		}
		
		// Destroy the process
		catch (RuntimeException|Error e)
		{
			if (rv != null)
				try
				{
					rv.close();
				}
				catch (Throwable t)
				{
					e.addSuppressed(t);
				}
			
			throw e;
		}
		
		// Done
		return rv;
	}
	
	/**
	 * Creates a new thread which is owned by the given process.
	 *
	 * @param __kp The process owning the new thread.
	 * @param __mc The main class
	 * @param __mm The main method.
	 * @param __args Arguments to the thread, only boxed types and {@code null}
	 * are permitted.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/16
	 */
	final KernelThread __createThread(KernelProcess __kp, String __mc,
		String __mm, Object... __args)
		throws NullPointerException
	{
		// Check
		if (__kp == null || __mc == null || __mm == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return this.threadmanager.createThread(__kp, __mc, __mm, __args);
	}
}

