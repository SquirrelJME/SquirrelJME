// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This interface is used as a factory and is used to initialize the various
 * parts of the kernel as needed during initialization.
 *
 * @since 2017/12/27
 */
public interface KernelInitializerFactory
{
	/**
	 * Initializes the task which is used to represent the kernel itself.
	 *
	 * @param __k The owning kernel.
	 * @return The task representing the kernel itself.
	 * @since 2017/12/11
	 */
	public abstract KernelTask initializeKernelTask(Kernel __k);
	
	/**
	 * This initializes the program manager.
	 *
	 * @param __k The owning kernel.
	 * @return The newly initialized program manager.
	 * @since 2017/12/14
	 */
	public abstract KernelPrograms initializePrograms(Kernel __k);
	
	/**
	 * This initializes the task manager.
	 *
	 * @param __k The owning kernel.
	 * @param __st The kernel's task.
	 * @return The task manager.
	 * @since 2017/12/27
	 */
	public abstract KernelTasks initializeTasks(Kernel __k, KernelTask __st);
}

