// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This class acts as the base for classes which need to call the kernel to
 * perform a task.
 *
 * @since 2017/12/10
 */
public abstract class SystemCaller
{
	/**
	 * This installs the specified byte array as a new program or as an update
	 * to an existing one on the system.
	 *
	 * @param __b The bytes which make up the JAR.
	 * @param __o The offset into the array.
	 * @param __l The length of the array.
	 * @return The program which was installed.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public abstract SystemProgramInstallReport installProgram(
		byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Attempts to launch the specified program with the specified entry
	 * point. If a task with the same program and entry point is already
	 * running the it will be restarted.
	 *
	 * @param __p The program to launch.
	 * @param __main The main entry point for the task.
	 * @param __perms The permissions to use for the new task.
	 * @param __props System properties in key/value pairs to define, this
	 * argument is optional.
	 * @return The system task for the launched task, if a task was restarted
	 * then it will return the same task identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public abstract SystemTask launchTask(SystemProgram __program,
		String __mainclass, int __perms, String... __props)
		throws NullPointerException;
	
	/**
	 * Lists programs which are available on the system.
	 *
	 * @param __typemask A mask which is used to filter programs of a given
	 * type.
	 * @return Programs which match the specified mask and exist.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/11
	 */
	public abstract SystemProgram[] listPrograms(int __typemask)
		throws SecurityException;
	
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
	 * Locates the specified program by the given index.
	 *
	 * @param __dx The index of the program to get.
	 * @return The program for the given index or {@code null} if not found.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/27
	 */
	public final SystemProgram programByIndex(int __dx)
		throws SecurityException
	{
		for (SystemProgram p : this.listPrograms(~0))
			if (p.index() == __dx)
				return p;
		
		// Not found
		return null;
	}
}

