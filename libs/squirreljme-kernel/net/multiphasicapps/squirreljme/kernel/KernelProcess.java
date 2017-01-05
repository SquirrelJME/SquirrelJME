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

/**
 * This represents a single process within the kernel and is used to manage
 * groups of threads within the kernel.
 *
 * @since 2016/11/08
 */
public abstract class KernelProcess
{
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** The class path for this process. */
	private final SuiteDataAccessor[] _classpath;
	
	/**
	 * Initializes the process.
	 *
	 * @param __k The kernel owning the process.
	 * @param __cp The class path used for user space processes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	protected KernelProcess(Kernel __k, SuiteDataAccessor[] __cp)
		throws NullPointerException
	{
		// Check
		if (__k == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		
		// Copy the class path
		__cp = __cp.clone();
		for (SuiteDataAccessor sda : __cp)
			if (sda == null)
				throw new NullPointerException("NARG");
		this._classpath = __cp;
	}
	
	/**
	 * This destroys the specified process so that it no longer exists.
	 *
	 * @since 2016/11/08
	 */
	public final void destroyProcess()
	{
		throw new Error("TODO");
	}
}

