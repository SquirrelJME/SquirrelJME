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
 * This class provides access to the system's default system call interface
 * which interacts with the kernel.
 *
 * @since 2017/12/10
 */
@Deprecated
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
	 * Checks that the specified permission is valid.
	 *
	 * @param __cl The class type of the permission.
	 * @param __n The name of the permission.
	 * @param __a The actions in the permission.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/11
	 */
	public void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		if (__cl == null || __n == null || __a == null)
			throw new NullPointerException("NARG");
		
		SystemCall._CALLER.checkPermission(__cl, __n, __a);
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
	 * Obtains the specified environment variable.
	 *
	 * This will always return {@code null} when not invoked by the kernel.
	 *
	 * @param __v The variable to obtain.
	 * @return The value of the variable or {@code null} if it has not been
	 * set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/13
	 */
	public static final String getEnv(String __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		return SystemCall._CALLER.getEnv(__v);
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
		return SystemCall._CALLER.listTasks(__incsys);
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
	 * Returns the operating system type that SquirrelJME is running on.
	 *
	 * @return The operating system type SquirrelJME is running on.
	 * @since 2018/01/13
	 */
	public static final OperatingSystemType operatingSystemType()
	{
		return SystemCall._CALLER.operatingSystemType();
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
	 * Returns the kernel service for the given class.
	 *
	 * @param <C> The class of the service.
	 * @param __cl The class of the service.
	 * @return The instance of the service client.
	 * @throws NoSuchServiceException If no service is available.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	public static final <C> C service(Class<C> __cl)
		throws NoSuchServiceException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return SystemCall._CALLER.service(__cl);
	}
	
	/**
	 * Specifies that the specified thread should become a daemon thread.
	 *
	 * @param __t The thread to set as a daemon.
	 * @throws IllegalThreadStateException If the thread has already been
	 * started or is already a daemon thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public static final void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// The packet interface needs this class to set daemon threads
		// before the system caller is initialized.
		SystemCaller caller = SystemCall._CALLER;
		if (caller == null)
		{
			// {@squirreljme.error ZZ01 Cannot set daemon thread because
			// the system caller has not been set yet.}
			String altname = System.getProperty(
				DaemonThreadSetter.class.getName());
			if (altname == null)
				throw new IllegalThreadStateException("ZZ01");
			
			try
			{
				// Setup class instance
				Class<?> cl = Class.forName(altname);
				DaemonThreadSetter dts = (DaemonThreadSetter)cl.newInstance();
				
				// Call that instead
				dts.setDaemonThread(__t);
				return;
			}
			
			// {@squirreljme.error ZZ02 Could not set the daemon thread using
			// the alternative means.}
			catch (ClassCastException|ClassNotFoundException|
				IllegalAccessException|InstantiationException e)
			{
				IllegalThreadStateException t =
					new IllegalThreadStateException("ZZ02");
				t.initCause(e);
				throw t;
			}
		}
		
		caller.setDaemonThread(__t);
	}
	
	/**
	 * Returns the current task id.
	 *
	 * @return The task id.
	 * @since 2018/01/18
	 */
	public static final int taskId()
	{
		return SystemCall._CALLER.taskId();
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

