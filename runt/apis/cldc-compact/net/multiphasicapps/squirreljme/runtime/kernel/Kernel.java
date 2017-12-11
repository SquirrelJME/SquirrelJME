// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

/**
 * This class represents the micro-kernel which manages the entire SquirrelJME
 * system and all of the needed IPC and running tasks/threads.
 *
 * There must be no way for an instance of this class to be obtained by any
 * client library, this means that once the kernel is initialized and passed to
 * an API bridge the pointer should be tossed out. It can however be
 * initialized as normal.
 *
 * @since 2017/12/08
 */
public abstract class Kernel
{
	/** The kernel's task. */
	protected final KernelTask kerneltask;
	
	/**
	 * Initializes the base kernel.
	 *
	 * @since 2017/12/10
	 */
	protected Kernel()
		throws NullPointerException
	{
		// Initialize the kernel task
		this.kerneltask = this.initializeTask(KernelTask.class);
	}
	
	/**
	 * Initializes the task which is used to represent the kernel itself, this
	 * should only be called once.
	 *
	 * @param __cl This parameter is ignored.
	 * @return The task representing the kernel itself.
	 * @since 2017/12/11
	 */
	protected abstract KernelTask initializeTask(Class<KernelTask> __cl);
	
	/**
	 * Creates a new task for the given program.
	 *
	 * @param __p The program to use as the basis for the new task.
	 * @return The newly created task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/11
	 */
	protected abstract KernelTask initializeTask(KernelProgram __p)
		throws NullPointerException;
	
	/**
	 * This returns the kernel task.
	 *
	 * @return The kernel task.
	 * @since 2017/12/11
	 */
	public final KernelTask kernelTask()
	{
		return this.kerneltask;
	}
}

