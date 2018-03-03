// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.system.MnemonicCall;
import cc.squirreljme.runtime.cldc.system.SystemCall;
import cc.squirreljme.runtime.cldc.system.SystemFunction;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.cldc.task.SystemTaskFlag;
import cc.squirreljme.runtime.cldc.task.SystemTaskMetric;
import cc.squirreljme.runtime.cldc.task.SystemTaskStatus;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This describes a task which is currently running on the system. Each task
 * has a starting point which is a {@link javax.microedition.midlet.MIDlet}.
 *
 * System tasks cannot be started or stopped.
 *
 * @since 2016/06/24
 */
public final class Task
{
	/** The actual chore being wrapped. */
	private final SystemTask _task;
	
	/**
	 * Initializes the task.
	 *
	 * @param __task The chore to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	Task(SystemTask __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		this._task = __task;
	}
	
	/**
	 * Checks if this task is the same as another task, tasks are equal if
	 * they share the same name.
	 *
	 * @param __o The other object.
	 * @return {@code true} if this is the same task.
	 * @sicne 2016/06/24
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be another task
		if (!(__o instanceof Task))
			return false;
		
		// Check
		Task o = (Task)__o;
		return Objects.equals(this.getSuite(), o.getSuite()) &&
			Objects.equals(this.getName(), o.getName());
	}
	
	/**
	 * Returns the estimated number of bytes that a task is using.
	 *
	 * @return The number of bytes the task is estimated to be using.
	 * @since 2016/06/24
	 */
	public int getHeapUse()
	{
		// Make sure the amount of memory used does not overflow ever
		long rv = this._task.metric(SystemTaskMetric.MEMORY_USED);
		if (rv < 0L)
			return 0;
		else if (rv > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return (int)rv;
	}
	
	/**
	 * Returns the name of the current task.
	 *
	 * @return The name of the task, system tasks always return {@code null}.
	 * @since 2016/06/24
	 */
	public String getName()
	{
		// System tasks have no name
		if (this.isSystemTask())
			return null;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the priority of this task.
	 *
	 * @return The task priority.
	 * @since 2016/06/24
	 */
	public TaskPriority getPriority()
	{
		long rv = this._task.metric(SystemTaskMetric.PRIORITY);
		if (rv < 0L)
			return TaskPriority.MAX;
		else if (rv > 0L)
			return TaskPriority.MIN;
		return TaskPriority.NORM;
	}
	
	/**
	 * Returns the status of this task.
	 *
	 * @return The task status.
	 * @since 2016/06/24
	 */
	public TaskStatus getStatus()
	{
		SystemTaskStatus status = this._task.status();
		switch (status)
		{
			case EXITED_FATAL:
				return TaskStatus.EXITED_FATAL;

			case EXITED_REGULAR:
				return TaskStatus.EXITED_REGULAR;

			case EXITED_TERMINATED:
				return TaskStatus.EXITED_TERMINATED;

			case RUNNING:
				return TaskStatus.RUNNING;

			case START_FAILED:
				return TaskStatus.START_FAILED;

			case STARTING:
				return TaskStatus.STARTING;
			
				// {@squirreljme.error DG06 Task is in an invalid status
				// state. (The status)}
			default:
				throw new IllegalStateException(
					String.format("DG04 %d", status));
		}
	}
	
	/**
	 * Returns the suite that this task belongs to.
	 *
	 * @return The owning suite.
	 * @since 2016/06/24
	 */
	public Suite getSuite()
	{
		// All system tasks are hidden behind the system suite
		if (this.isSystemTask())
			return Suite.SYSTEM_SUITE;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the hash code of this task, the hashcode is derived from the
	 * suite and the name.
	 *
	 * @return The hash code.
	 * @since 2016/06/24
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.getSuite()) ^
			Objects.hashCode(this.getName());
	}
	
	/**
	 * Returns {@code true} if this is a system task.
	 *
	 * @return {@code true} if a system task.
	 * @since 2016/06/24
	 */
	public boolean isSystemTask()
	{
		return (0 != (this._task.flags() & SystemTaskFlag.SYSTEM));
	}
	
	/**
	 * Returns the system task of this task.
	 *
	 * @return The system task.
	 * @since 2017/12/08
	 */
	final SystemTask __task()
	{
		return this._task;
	}
}

