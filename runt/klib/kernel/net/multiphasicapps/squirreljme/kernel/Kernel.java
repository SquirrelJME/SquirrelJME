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
	/** Program manager. */
	@Deprecated
	protected final KernelPrograms kernelprograms;
	
	/** Task manager. */
	@Deprecated
	protected final KernelTasks kerneltasks;
	
	/**
	 * Initializes the base kernel.
	 *
	 * @param __kt The output array which is placed the kernel task.
	 * @param __kif Initialization factory.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/10
	 */
	protected Kernel(KernelTask[] __kt, KernelInitializerFactory __kif)
		throws NullPointerException
	{
		if (__kt == null || __kif == null)
			throw new NullPointerException("NARG");
		
		// Initialize the base kernel task
		KernelTask kerneltask = __kif.initializeKernelTask(this);
		__kt[0] = kerneltask;
		
		// Initialize the kernel
		this.kerneltasks = __kif.initializeTasks(this, kerneltask);
		this.kernelprograms = __kif.initializePrograms(this);
	}
	
	/**
	 * Requests that the system map the specified service class to a default
	 * provided service name.
	 *
	 * @param __s The service to map.
	 * @return The default service to map to or {@code null} if there is no
	 * mapping for the given service.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public abstract String mapService(String __sv)
		throws NullPointerException;
	
	/**
	 * Specifies that the specified thread should become a daemon thread.
	 *
	 * @param __t The thread to set as a daemon.
	 * @throws IllegalThreadStateException If the thread has already been
	 * started or is already a daemon thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	@Deprecated
	public abstract void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException;
	
	/**
	 * Returns the program manager.
	 *
	 * @param __by The task requesting the manager.
	 * @return The program manager.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the task is not permitted access to the
	 * program manager.
	 * @since 2017/12/14
	 */
	@Deprecated
	public final KernelPrograms programs(KernelTask __by)
		throws NullPointerException, SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0g The specified task is not permitted to get
		// the program manager instance. (The task requesting the program
		// manager)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.GET_PROGRAMS_INSTANCE))
			throw new SecurityException(
				String.format("ZZ0g %s", __by));
		
		return this.kernelprograms;
	}
	
	/**
	 * Returns the task manager.
	 *
	 * @param __by The thread requesting the manager.
	 * @return The task manager.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the task is not permitted to access the
	 * task manager.
	 * @since 2017/12/27
	 */
	@Deprecated
	public final KernelTasks tasks(KernelTask __by)
		throws NullPointerException, SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0j The specified task is not permitted to get
		// the task manager instance. (The task requesting the task manager)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.GET_TASKS_INSTANCE))
			throw new SecurityException(
				String.format("ZZ0j %s", __by));
		
		return this.kerneltasks;
	}
	
	/**
	 * Returns the kernel program manager with no access check.
	 *
	 * @return The kernel program manager.
	 * @since 2017/12/31
	 */
	@Deprecated
	final KernelPrograms __programs()
	{
		return this.kernelprograms;
	}
}

