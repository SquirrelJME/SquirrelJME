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

