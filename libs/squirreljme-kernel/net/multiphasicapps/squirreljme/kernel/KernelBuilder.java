// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This is used to create instances of the kernel.
 *
 * @since 2016/11/02
 */
public final class KernelBuilder
{
	/** Lock. */
	final Object _lock =
		new Object();
	
	/** Launch parameters. */
	volatile KernelLaunchParameters _launchparms;
	
	/** The suite manager. */
	volatile KernelSuiteManager _suitemanager;
	
	/** The thread manager. */
	volatile KernelThreadManager _threadmanager;
	
	/** System class path manager. */
	volatile KernelSystemClassPath _sysclasspath;
	
	/**
	 * Builds the target kernel.
	 *
	 * @return The resulting kernel.
	 * @throws IllegalArgumentException If the kernel was not correctly
	 * initialized.
	 * @since 2016/11/02
	 */
	public Kernel build()
		throws IllegalArgumentException
	{
		// The data is locked by the constructor
		return new Kernel(this);
	}
	
	/**
	 * Sets the launch parameters which are used to initialize the kernel.
	 *
	 * @param __lp The launch parameters used for the kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/04
	 */
	public void launchParameters(KernelLaunchParameters __lp)
		throws NullPointerException
	{
		// Check
		if (__lp == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this._lock)
		{
			this._launchparms = __lp;
		}
	}
	
	/**
	 * Sets the suite manager to use.
	 *
	 * @param __sm The suite manager to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/02
	 */
	public void suiteManager(KernelSuiteManager __sm)
		throws NullPointerException
	{
		// Check
		if (__sm == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this._lock)
		{
			this._suitemanager = __sm;
		}
	}
	
	/**
	 * Sets the system class path.
	 *
	 * @param __cp The class path that the system uses.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	public void systemClassPath(KernelSystemClassPath __cp)
		throws NullPointerException
	{
		// Check
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this._lock)
		{
			this._sysclasspath = __cp;
		}
	}
	
	/**
	 * Sets the interface which manages threads which are ran by the kernel.
	 *
	 * @param __tm The thread manager used by the kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/02
	 */
	public void threadManager(KernelThreadManager __tm)
		throws NullPointerException
	{
		// Check
		if (__tm == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this._lock)
		{
			this._threadmanager = __tm;
		}
	}
}

