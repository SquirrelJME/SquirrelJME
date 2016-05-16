// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.perm;

import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;

/**
 * This is the permission manager which manages and grants/denies permission
 * for processes to do things.
 *
 * @since 2016/05/16
 */
public final class PermissionManager
{
	/** The process which owns this manager. */
	protected final KernelProcess owner;
	
	/** The kernel this runs under. */
	protected final Kernel kernel;
	
	/**
	 * Initializes the permission manager.
	 *
	 * @param __kp The owning process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/16
	 */
	public PermissionManager(KernelProcess __kp)
		throws NullPointerException
	{
		// Check
		if (__kp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __kp;
		this.kernel = __kp.kernel();
	}
	
	/**
	 * Checks if the current process can access the given process and if it
	 * does it is returned.
	 *
	 * @return The current process.
	 * @throws SecurityException If the current process is not permitted to
	 * access the process this manager manages.
	 * @since 2016/05/16
	 */
	public KernelProcess accessProcess()
		throws SecurityException
	{
		// The current process
		KernelProcess curkp = this.kernel.currentProcess();
		
		// {@squirreljme.error AY05 Current process is not known, all
		// permissions are denied.}
		if (curkp == null)
			throw new SecurityException("AY05");
		
		// The kernel can do whatever it wants
		if (curkp.isKernelProcess())
			return curkp;
		
		// {@squirreljme.error AY04 User-space processes do not have any
		// permissions when accessing other processes.}
		if (curkp != owner)
			throw new SecurityException("AY04");
		
		// Ok
		return curkp;
	}
	
	/**
	 * Can read events from the event queue?
	 *
	 * @throws SecurityException If events cannot be read from the event queue.
	 * @since 2016/05/16
	 */
	public void eventRead()
	{
		// Process access
		KernelProcess curkp = accessProcess();
	}
	
	/**
	 * Can write events to the event queue?
	 *
	 * @throws SecurityException If events cannot be pushed to the event queue.
	 * @since 2016/05/16
	 */
	public void eventWrite()
	{
		// Process access
		KernelProcess curkp = accessProcess();
	}
}

