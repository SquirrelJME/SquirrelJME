// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.kernel.event.EventQueue;

/**
 * This represents a process within the kernel. A process owns a number of
 * {@link Thread}s and also is used for when the system is in kernel code that
 * permissions are checked and such for the current process before an
 * operation is performed.
 *
 * This class acts as a {@link SecurityManager} to running threads.
 *
 * @since 2016/05/16
 */
public final class KernelProcess
{
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** Is this the kernel process? */
	protected final boolean iskernel;
	
	/** The process event queue. */
	protected final EventQueue eventqueue;
	
	/**
	 * Initializes the kernel process.
	 *
	 * @param __k The owning kernel.
	 * @param __ik Is this the kernel process.
	 * @throws NullPointerException
	 * @since 2016/05/16
	 */
	KernelProcess(Kernel __k, boolean __ik)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		kernel = __k;
		iskernel = __ik;
		
		// Setup event queue
		eventqueue = new EventQueue(this);
	}
	
	/**
	 * Checks if the given permission is permitted by the current process on
	 * behalf of this process.
	 *
	 * @param __p The permission to check.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the action is not permitted.
	 * @since 2016/05/16
	 */
	public final void checkPermission(String __p)
		throws NullPointerException, SecurityException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// The current process
		KernelProcess curkp = this.kernel.currentProcess();
		
		// The kernel can do whatever it wants
		if (curkp.isKernelProcess())
			return;
		
		// {@squirreljme.error AY04 User-space processes do not have any
		// permissions when accessing other processes.}
		if (curkp != this)
			throw new SecurityException("AY04");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if this is the kernel process.
	 *
	 * @return {@code true} if this is the kernel process, otherwise it is
	 * a user-space process.
	 * @since 2016/05/16
	 */
	public final boolean isKernelProcess()
	{
		return this.iskernel;
	}
	
	/**
	 * Adds a thread to the current process.
	 *
	 * @param __t The thread to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/16
	 */
	final void __addThread(Thread __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

