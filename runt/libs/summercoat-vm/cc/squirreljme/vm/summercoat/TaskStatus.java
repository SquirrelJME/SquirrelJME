// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;

/**
 * This contains the status for a single task and is used to detect when a
 * task is running and if it has ever exited.
 *
 * @since 2019/01/05
 */
public class TaskStatus
{
	/** The monitor or the task statuses, used to signal state changes. */
	protected final Object monitor;
	
	/** The ID of this task. */
	protected final int id;
	
	/** The exit code for this virtual machine. */
	volatile int _exitcode =
		Integer.MIN_VALUE;
	
	/** The state of the task, is initially starting. */
	volatile TaskState _state =
		TaskState.STARTING;
	
	/** Requests that the task be stopped/exited. */
	volatile boolean _requestexit;
	
	/**
	 * The event callback for this task for LCDUI. This is used here because
	 * a task could be placed in the foreground (switched to) in which case
	 * it needs to have UI events sent to it.
	 */
	volatile NativeDisplayEventCallback _eventcallback;
	
	/**
	 * Initializes the task.
	 *
	 * @param __m The monitor to notify when the task changes state.
	 * @param __id The ID of the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/05
	 */
	public TaskStatus(Object __m, int __id)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		this.monitor = __m;
		this.id = __id;
	}
}

