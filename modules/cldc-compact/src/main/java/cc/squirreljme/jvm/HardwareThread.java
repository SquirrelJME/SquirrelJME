// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is the manager for the hardware threads which exist within the VM.
 *
 * @since 2020/04/28
 */
public final class HardwareThread
{
	/** The unique identifier for this thread, this is a unique value. */
	protected final int threadId;
	
	/** The task ID this thread falls under. */
	protected final int taskId;
	
	/**
	 * Initializes the hardware thread information.
	 *
	 * @param __thread The thread ID.
	 * @param __task The task this hardware thread is associated with.
	 * @since 2020/04/28
	 */
	HardwareThread(int __thread, int __task)
	{
		this.threadId = __thread;
		this.taskId = __task;
	}
	
	/**
	 * Creates a new hardware thread owned by the given task.
	 *
	 * @param __main Is this a main thread intended to become a new process?
	 * @param __taskId The owning task ID of the thread, if this is the main
	 * thread of a new process this must be zero.
	 * @return The newly created hardware thread.
	 * @throws IllegalArgumentException If this is the main thread and the
	 * task ID is zero.
	 * @throws OutOfMemoryError If a thread could not be created.
	 * @since 2020/05/01
	 */
	public static HardwareThread createThread(boolean __main, int __taskId)
		throws IllegalArgumentException, OutOfMemoryError
	{
		// {@squirreljme.error ZZ45 New main threads must have a task ID that
		// is zero.}
		if (!__main && __taskId != 0)
			throw new IllegalArgumentException("ZZ45");
		
		// {@squirreljme.error ZZ46 Could not create a new hardware thread.}
		int threadId = Assembly.sysCallV(SystemCallIndex.HW_THREAD,
			HardwareThreadControl.CONTROL_CREATE_THREAD);
		if (threadId == 0 || SystemCall.hasError(SystemCallIndex.HW_THREAD))
			throw new OutOfMemoryError("ZZ46");
		
		// The task ID will either be the one specified or it will be the
		// thread's hardware ID for a one to one mapping
		int useTaskId = (__main ? threadId : __taskId);
		
		// Store this thread and also try to update the task ID of it so it
		// is more correct regarding the thread.
		HardwareThread hw = new HardwareThread(threadId, useTaskId);
		Assembly.sysCallV(SystemCallIndex.HW_THREAD,
			HardwareThreadControl.CONTROL_THREAD_SET_TASKID,
			threadId, useTaskId);
		
		// {@squirreljme.error ZZ47 Could not set initial task ID of new
		// thread.}
		if (SystemCall.hasError(SystemCallIndex.HW_THREAD))
			throw new IllegalThreadStateException("ZZ47");
		
		// If this is a main task we are going to have to initialize a context
		// to store thread information and such inside of it (such as classes
		// or other things)
		if (__main)
		{
			// Initialize a new context
			Assembly.sysCallV(SystemCallIndex.HW_THREAD,
				HardwareThreadControl.CONTROL_INITIALIZE_CONTEXT, useTaskId);
			
			// {@squirreljme.error ZZ48 Could not initialize a context for
			// the main thread.}
			if (SystemCall.hasError(SystemCallIndex.HW_THREAD))
				throw new IllegalThreadStateException("ZZ48");
		}
		
		// Otherwise we want to use the context that is part of the main task
		else
		{
			// Use pre-existing context, which will be the main thread/task
			Assembly.sysCallV(SystemCallIndex.HW_THREAD,
				HardwareThreadControl.CONTROL_SET_CONTEXT,
				threadId, useTaskId);
				
			// {@squirreljme.error ZZ49 Could not set the new hardware thread
			// context to be that of the main thread.}
			if (SystemCall.hasError(SystemCallIndex.HW_THREAD))
				throw new IllegalThreadStateException("ZZ49");
		}
		
		// Hardware threads are very low level, so there is not much to
		// initialize here
		return hw;
	}
}
