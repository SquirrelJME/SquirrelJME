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
 * This manager is implemented by operating system support code and is used
 * to manage threads between the kernel and the native system.
 *
 * @since 2016/11/02
 */
public interface KernelThreadManager
{
	/**
	 * Runs the kernel loop which runs is capable of running the threads that
	 * are being used on the system.
	 *
	 * @throws InterruptedException If the kernel run loop was interrupted.
	 * @since 2016/11/03
	 */
	public abstract void runThreads()
		throws InterruptedException;
	
	/**
	 * Sets the listener for when the state of threads change.
	 *
	 * @param __t The listener to use when thread state changes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/02
	 */
	public abstract void setThreadListener(KernelThreadListener __t)
		throws NullPointerException;
}

