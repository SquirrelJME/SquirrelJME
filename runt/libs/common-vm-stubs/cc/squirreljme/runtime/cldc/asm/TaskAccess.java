// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * This class provides access to tasks which are running.
 *
 * @since 2018/11/04
 */
public final class TaskAccess
{
	/** The entry point is not valid. */
	public static final int ERROR_INVALID_ENTRY =
		-2;
	
	/** Library in the classpath is missing. */
	public static final int ERROR_MISSING_LIBRARY =
		-3;
	
	/** Exit code indicating bad task things. */
	public static final int EXIT_CODE_FATAL_EXCEPTION =
		127;
	
	/**
	 * Not used.
	 *
	 * @since 2018/11/04
	 */
	private TaskAccess()
	{
	}
	
	/**
	 * Returns the ID of the current thread.
	 *
	 * @return The current thread ID.
	 * @since 2018/11/20
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final int currentThread()
	{
		return (int)Thread.currentThread().getId();
	}
	
	/**
	 * Sets the priority of the thread.
	 *
	 * @param __tid The thread ID.
	 * @param __p The priority.
	 * @since 2018/12/07
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final void setThreadPriority(int __tid, int __p)
	{
	}
	
	/**
	 * Signals a hardware interrupt on the given thread.
	 *
	 * @param __tid The thread to signal.
	 * @since 2018/11/21
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final void signalInterrupt(int __tid)
	{
		// {@squirreljme.error AJ22 Cannot interrupt threads using this method
		// as it is not implemented.}
		throw new Error("AJ22");
	}
	
	/**
	 * Causes the thread to sleep for the given milliseconds and nanoseconds.
	 *
	 * If both values are zero this means to yield instead.
	 *
	 * @param __ms The milliseconds to sleep for.
	 * @param __ns The nanoseconds to sleep for, in the range of 0-999999.
	 * @return {@code true} if the thread was interrupted, otherwise
	 * {@code false}.
	 * @since 2018/11/04
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final boolean sleep(long __ms, int __ns)
	{
		try
		{
			if (__ms == 0 && __ns == 0)
				Thread.yield();
			else
				Thread.sleep(__ms, __ns);
			
			return true;
		}
		catch (InterruptedException e)
		{
			return false;
		}
	}
	
	/**
	 * Starts the specified task.
	 *
	 * @param __cp The classpath used.
	 * @param __main The main entry point.
	 * @param __args Arguments to start the task with.
	 * @return The task identifier or a negative number if the task could
	 * not start.
	 * @since 2018/11/04
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final int startTask(String[] __cp, String __main,
		String[] __args)
	{
		return -1;
	}
	
	/**
	 * Starts the given thread.
	 *
	 * @param __t The thread which is to run, the execution point of the
	 * thread is the {@link Thread#__start()} method.
	 * @param __n The name hint of this thread.
	 * @return The thread ID.
	 * @since 2018/11/17
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final int startThread(Thread __t, String __n)
	{
		__t.start();
		return (int)__t.getId();
	}
	
	/**
	 * Returns the status of the target task.
	 *
	 * @param __tid The task to get the status of.
	 * @return The status for the given task.
	 * @since 2018/11/04
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final int taskStatus(int __tid)
	{
		return -1;
	}
}

