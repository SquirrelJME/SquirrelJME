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

import net.multiphasicapps.squirreljme.runtime.kernel.KernelMailBoxException;
import net.multiphasicapps.squirreljme.runtime.syscall.SystemCaller;
import net.multiphasicapps.squirreljme.runtime.syscall.SystemMailBoxConnection;
import net.multiphasicapps.squirreljme.runtime.syscall.SystemMailBoxListener;
import net.multiphasicapps.squirreljme.runtime.syscall.SystemProgram;
import net.multiphasicapps.squirreljme.runtime.syscall.SystemTask;

/**
 * This class provides access to the system's default system call interface
 * which interacts with the kernel.
 *
 * @since 2017/12/10
 */
public final class SystemCall
{
	/** The caller to use when making system calls. */
	public static final SystemCaller _CALLER =
		__systemCaller();
	
	/**
	 * Not used.
	 *
	 * @since 2017/12/10
	 */
	private SystemCall()
	{
	}
	
	/**
	 * Returns a value within the constraints of
	 * {@link System#currentTimeMillis()}.
	 *
	 * @return The milliseconds since the epoch, in UTC.
	 * @since 2017/11/10
	 */
	public static final long currentTimeMillis()
	{
		throw new todo.TODO();
	}
	/**
	 * This exits the virtual machine using the specifed exit code according
	 * to the specification of {@link Runtime#exit(int)}.
	 *
	 * @param __e The exit code to use.
	 * @since 2016/08/07
	 */
	public static final void exit(int __e)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Specifies that the virtual machine should perform garbage collection.
	 *
	 * @since 2017/11/10
	 */
	public static final void gc()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Attempts to launch the specified program with the specified entry
	 * point. If a task with the same program and entry point is already
	 * running the it will be restarted.
	 *
	 * @param __p The program to launch.
	 * @param __main The main entry point for the task.
	 * @return The system task for the launched task, if a task was restarted
	 * then it will return the same task identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/10
	 */
	public static final SystemTask launchTask(SystemProgram __p, String __main)
		throws NullPointerException
	{
		if (__p == null || __main == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Lists programs which are available on the system.
	 *
	 * @param __typemask A mask which is used to filter programs of a given
	 * type.
	 * @return Programs which match the specified mask and exist.
	 * @since 2017/12/10
	 */
	public static final SystemProgram[] listPrograms(int __typemask)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Lists tasks which currently exist and may be running on the system.
	 *
	 * @param __incsys If {@code true} then system tasks are included.
	 * @return Tasks which currently exist on the system.
	 * @since 2017/12/10
	 */
	public static final SystemTask[] listTasks(boolean __incsys)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Connects to a remote mailbox that is listening. 
	 *
	 * @param __remote The remote program to connect to, if {@code null} then
	 * any program is connected to.
	 * @param __svname The name of the server to connect to.
	 * @param __sv The encoded version of the server to connect to, the server
	 * must have a version that is at least this value.
	 * @param __am If {@code true} then authorized mode to use.
	 * @throws IllegalArgumentException If the remote midlet is malformed or
	 * the server name and/or version are malformed.
	 * @throws NullPointerException If no server was specified.
	 * @throws KernelMailBoxException If the server does not exist or if the
	 * remote destination is closed.
	 * @since 2016/10/13
	 */
	public final SystemMailBoxConnection connect(String __remote,
		String __svname, int __sv, boolean __am)
		throws ArrayIndexOutOfBoundsException,
			IllegalArgumentException, NullPointerException,
			KernelMailBoxException
	{
		if (__svname == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Sets up a listening mailbox that waits for incoming mailbox connections.
	 *
	 * @param __name The name of the server.
	 * @param __v The integer encoded version number.
	 * @param __am If {@code true} then authorization mode is used.
	 * @return The descriptor of the mailbox destination.
	 * @throws IllegalArgumentException If the version number is not correct.
	 * @throws NullPointerException On null arguments.
	 * @throws KernelMailBoxException If the mailbox was closed.
	 * @since 2016/10/13
	 */
	public final SystemMailBoxListener mailboxListen(String __name, int __v,
		boolean __am)
		throws IllegalArgumentException, NullPointerException,
			KernelMailBoxException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Requests that the system map the specified service class to a default
	 * provided service name.
	 *
	 * @param __s The service to map.
	 * @return The default service to map to or {@code null} if there is no
	 * mapping for the given service.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/10
	 */
	public static final String mapService(String __sv)
		throws NullPointerException
	{
		if (__sv == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns a value within the constraints of {@link System#nanoTime()}.
	 *
	 * @return The number of nanoseconds which have passed.
	 * @since 2017/11/10
	 */
	public static final long nanoTime()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Pipe a single byte to the output or error stream.
	 *
	 * @param __err If {@code true} then output is printed to standard error,
	 * otherwise standard output is written to.
	 * @param __b The byte to write.
	 * @since 2017/12/10
	 */
	public static final void pipeOutput(boolean __err, byte __b)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Pipe multiple bytes to the output or error stream.
	 *
	 * @param __err If {@code true} then output is printed to standard error,
	 * otherwise standard output is written to.
	 * @param __b The bytes to write.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/10
	 */
	public static final void pipeOutput(boolean __err, byte[] __b, int __o,
		int __l)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This returns the instance of the system caller which is to be used.
	 *
	 * @return The system caller interface.
	 * @since 2017/12/10
	 */
	private static final SystemCaller __systemCaller()
	{
		return SystemCall._CALLER;
	}
}

