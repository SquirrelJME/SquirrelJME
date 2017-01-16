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
 * This manager is implemented by operating system support code and is used
 * to manage threads between the kernel and the native system.
 *
 * @since 2016/11/02
 */
public interface KernelThreadManager
{
	/**
	 * Creates a process which is owned by the given kernel.
	 *
	 * @param __k The kernel owning the process.
	 * @param __cp The class path used.
	 * @return The newly created process.
	 * @throws NullPointerException On null arguments.
	 * @throws ProcessCreationException If the process could not be created.
	 * @since 2017/01/03
	 */
	public abstract KernelProcess createProcess(Kernel __k,
		SuiteDataAccessor[] __cp)
		throws NullPointerException, ProcessCreationException;
	
	/**
	 * Creates a thread which is owned by the given process.
	 *
	 * @param __kp The process that owns the created thread.
	 * @param __mc The main class
	 * @param __mm The main method.
	 * @param __args Arguments to the thread, only boxed types and {@code null}
	 * are permitted.
	 * @return The newly created thread.
	 * @since 2017/01/16
	 */
	public abstract KernelThread createThread(KernelProcess __kp, String __mc,
		String __m, Object... __args)
		throws NullPointerException, ThreadCreationException;
	
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

