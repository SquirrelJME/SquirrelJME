// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

/**
 * This class acts as the base for classes which need to call the kernel to
 * perform a task.
 *
 * @since 2017/12/10
 */
@Deprecated
public abstract class SystemCaller
{
	/**
	 * Checks that the specified permission is valid.
	 *
	 * @param __cl The class type of the permission.
	 * @param __n The name of the permission.
	 * @param __a The actions in the permission.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/11
	 */
	public abstract void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException;
	
	/**
	 * Obtains the specified environment variable.
	 *
	 * This will always return {@code null} when not invoked by the kernel.
	 *
	 * @param __v The variable to obtain.
	 * @return The value of the variable or {@code null} if it has not been
	 * set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/013
	 */
	public abstract String getEnv(String __v)
		throws NullPointerException;
	
	/**
	 * Lists tasks which are currently running within the system.
	 *
	 * @param __incsys If {@code true} then system tasks are included.
	 * @return Tasks which are currently running within the system.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/27
	 */
	public abstract SystemTask[] listTasks(boolean __incsys)
		throws SecurityException;
	
	/**
	 * Returns the operating system type that SquirrelJME is running on.
	 *
	 * @return The operating system type SquirrelJME is running on.
	 * @since 2018/01/13
	 */
	public abstract OperatingSystemType operatingSystemType();
	
	/**
	 * Returns the kernel service for the given class.
	 *
	 * @param <C> The class of the service.
	 * @param __cl The class of the service.
	 * @return The instance of the service client.
	 * @throws NoSuchServiceException If no service is available.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	public abstract <C> C service(Class<C> __cl)
		throws NoSuchServiceException, NullPointerException;
	
	/**
	 * Specifies that the specified thread should become a daemon thread.
	 *
	 * @param __t The thread to set as a daemon.
	 * @throws IllegalThreadStateException If the thread has already been
	 * started or is already a daemon thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public abstract void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException;
	
	/**
	 * Returns the current task id.
	 *
	 * @return The task id.
	 * @since 2018/01/18
	 */
	public abstract int taskId();
}

