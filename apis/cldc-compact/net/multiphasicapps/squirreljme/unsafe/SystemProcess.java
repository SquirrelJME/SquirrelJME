// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This class is used to manage the process in which SquirrelJME is running,
 * this information is needed for some VM functions.
 *
 * @see __Ext_systemprocess__
 * @since 2017/08/10
 */
public class SystemProcess
{
	/**
	 * Not used.
	 *
	 * @since 2017/08/10
	 */
	private SystemProcess()
	{
	}
	
	/**
	 * This returns the number of CPU threads which are available for usage.
	 *
	 * @return The total number of available CPU threads.
	 * @since 2017/08/29
	 */
	public static int cpuThreads()
	{
		return __Ext_systemprocess__.cpuThreads();
	}
	
	/**
	 * Creates a new thread, but one which is considered a daemon thread which
	 * is automatically killed when exit is called. This is required because
	 * CLDC has no concept of daemon threads.
	 *
	 * @param __r The method to call when the thread runs.
	 * @param __n The name of the thread.
	 * @return The newly created daemon thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/24
	 */
	public static Thread createDaemonThread(Runnable __r, String __n)
		throws NullPointerException
	{
		if (__r == null || __n == null)
			throw new NullPointerException("NARG");
		
		return __Ext_systemprocess__.createDaemonThread(__r, __n);
	}
	
	/**
	 * Returns the number of milliseconds that have passed in the UTC
	 * timezone since the Java epoch.
	 *
	 * @return The amount of milliseconds that have passed.
	 * @since 2016/08/07
	 */
	public static long currentTimeMillis()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This exits the virtual machine using the specifed exit code.
	 *
	 * @param __e The exit code to use.
	 * @since 2016/08/07
	 */
	public static void exit(int __e)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Is the current thread associated with the launcher?
	 *
	 * @return {@code true} if this is the internal sub-process which is deemed
	 * to be the kernel, otherwise this returns {@code false}.
	 * @since 2016/10/11
	 */
	public static boolean isLauncher()
	{
		return __Ext_systemprocess__.isLauncher();
	}
	
	/**
	 * Returns the identification number of the current midlet.
	 *
	 * @return The unique midlet number, a value of zero represents the
	 * kernel.
	 * @since 2016/10/13
	 */
	public static int midletId()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the amount of time which has passed on an unspecified
	 * monotonic clock.
	 *
	 * @return The number of monotonic nanoseconds which have passed.
	 * @since 2016/08/07
	 */
	public static long nanoTime()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This returns the process ID of the current sub-process.
	 *
	 * @return The sub-process ID.
	 * @since 2017/08/10
	 */
	public static int processId()
	{
		throw new todo.TODO();
	}
}

