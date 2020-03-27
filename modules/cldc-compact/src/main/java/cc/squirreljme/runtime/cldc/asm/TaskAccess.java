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
@Deprecated
public final class TaskAccess
{
	/** The entry point is not valid. */
	@Deprecated
	public static final int ERROR_INVALID_ENTRY =
		-2;
	
	/** Library in the classpath is missing. */
	@Deprecated
	public static final int ERROR_MISSING_LIBRARY =
		-3;
	
	/** Exit code indicating bad task things. */
	@Deprecated
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
	@Deprecated
	public static final native int currentThread();
	
	/**
	 * Sets the priority of the thread.
	 *
	 * @param __tid The thread ID.
	 * @param __p The priority.
	 * @since 2018/12/07
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	@Deprecated
	public static final native void setThreadPriority(int __tid, int __p);
	
	/**
	 * Signals a hardware interrupt on the given thread.
	 *
	 * @param __tid The thread to signal.
	 * @since 2018/11/21
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	@Deprecated
	public static final native void signalInterrupt(int __tid);
	
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
	@Deprecated
	public static final native int startTask(String[] __cp, String __main,
		String[] __args);
	
	/**
	 * Starts the specified task.
	 *
	 * @param __cp The classpath used.
	 * @param __main The main entry point.
	 * @param __args Arguments to start the task with.
	 * @param __sprops System properties in key/value pairs to pass to the
	 * target environment.
	 * @param __stdout Callback to receive standard output data, may be
	 * {@code null} to ignore.
	 * @param __stderr Callback to receive standard error data, may be
	 * {@code null} to ignore.
	 * @return The task identifier or a negative number if the task could
	 * not start.
	 * @since 2019/02/02
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV)
	@Deprecated
	public static final native int startTask(String[] __cp, String __main,
		String[] __args, String[] __sprops, ConsoleCallback __stdout,
		ConsoleCallback __stderr);
	
	/**
	 * Starts the given thread.
	 *
	 * @param __t The thread which is to run, the execution point of the
	 * thread is the {@code Thread.__start()} method.
	 * @param __n The name hint of this thread.
	 * @return The thread ID.
	 * @since 2018/11/17
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	@Deprecated
	public static final native int startThread(Thread __t, String __n);
	
	/**
	 * Returns the status of the target task.
	 *
	 * @param __tid The task to get the status of.
	 * @return The status for the given task.
	 * @since 2018/11/04
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	@Deprecated
	public static final native int taskStatus(int __tid);
}

