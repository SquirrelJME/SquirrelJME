// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.executable.ExecutableClass;
import net.multiphasicapps.squirreljme.kernel.ContextClass;
import net.multiphasicapps.squirreljme.kernel.ContextLoadException;
import net.multiphasicapps.squirreljme.kernel.InvalidSuiteException;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelLaunchParameters;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KernelSuiteManager;
import net.multiphasicapps.squirreljme.kernel.KernelThread;
import net.multiphasicapps.squirreljme.kernel.KernelThreadListener;
import net.multiphasicapps.squirreljme.kernel.KernelThreadManager;
import net.multiphasicapps.squirreljme.kernel.ProcessCreationException;
import net.multiphasicapps.squirreljme.kernel.SuiteDataAccessor;
import net.multiphasicapps.squirreljme.kernel.SystemInstalledSuites;
import net.multiphasicapps.squirreljme.kernel.ThreadCreationException;
import net.multiphasicapps.squirreljme.kernel.UserInstalledSuites;
import net.multiphasicapps.squirreljme.suiteid.APIConfiguration;
import net.multiphasicapps.squirreljme.suiteid.APIProfile;
import net.multiphasicapps.squirreljme.suiteid.APIStandard;

/**
 * This is the normal kernel manager which runs code as fast as possible.
 *
 * @since 2016/11/02
 */
public class InterpreterKernelManager
	implements KernelLaunchParameters, KernelSuiteManager, KernelThreadManager
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** Interrupt trigger. */
	protected final Object interrupt =
		new Object();
	
	/** The owning auto interpreter. */
	protected final AutoInterpreter autointerpreter;
	
	/** The thread listener to use. */
	private volatile KernelThreadListener _threadlistener;
	
	/** System suites. */
	private volatile SystemInstalledSuites _syssuites;
	
	/** The real launch parameters to use. */
	protected final KernelLaunchParameters launchparms;
	
	/**
	 * Initializes the normal kernel manager.
	 *
	 * @param __ai The interpreter owning this.
	 * @param __lp Kernel launch parameters.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/02
	 */
	public InterpreterKernelManager(AutoInterpreter __ai,
		KernelLaunchParameters __lp)
		throws NullPointerException
	{
		// Check
		if (__ai == null || __lp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.autointerpreter = __ai;
		this.launchparms = __lp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/03
	 */
	@Override
	public KernelProcess createProcess(Kernel __k, SuiteDataAccessor[] __cp)
		throws NullPointerException, ProcessCreationException
	{
		// Check
		if (__k == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Create it
		return new InterpreterKernelProcess(__k, __cp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/16
	 */
	@Override
	public KernelThread createThread(KernelProcess __kp, String __mc,
		String __m)
		throws NullPointerException, ThreadCreationException
	{
		// Check
		if (__kp == null || __mc == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Create thread
		return new InterpreterKernelThread(__kp, __mc, __m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/08
	 */
	@Override
	public final String[] getCommandLine()
	{
		return this.launchparms.getCommandLine();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/08
	 */
	@Override
	public final String getSystemProperty(String __k)
	{
		return this.launchparms.getSystemProperty(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/03
	 */
	@Override
	public void runThreads()
		throws InterruptedException
	{
		// Wait for interrupt requests
		Object interrupt = this.interrupt;
		synchronized (interrupt)
		{
			interrupt.wait();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/11/02
	 */
	@Override
	public final void setThreadListener(KernelThreadListener __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._threadlistener = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/16
	 */
	@Override
	public final SystemInstalledSuites systemSuites()
	{
		// Lock
		synchronized (this.lock)
		{
			SystemInstalledSuites rv = this._syssuites;
			if (rv == null)
				this._syssuites = (rv = new InterpreterSystemSuites(
					this.autointerpreter, this.launchparms));
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/16
	 */
	@Override
	public final UserInstalledSuites userSuites()
	{
		throw new todo.TODO();
	}
}

