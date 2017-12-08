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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;
import net.multiphasicapps.squirreljme.runtime.cldc.APIAccessor;
import net.multiphasicapps.squirreljme.runtime.cldc.chore.Chore;

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
	private final Chore _chore;
	
	/**
	 * Initializes the task.
	 *
	 * @param __chore The chore to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/24
	 */
	Task(Chore __chore)
		throws NullPointerException
	{
		if (__chore == null)
			throw new NullPointerException("NARG");
		
		this._chore = __chore;
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
		throw new todo.TODO();
		/*
		// Make sure the amount of memory used does not overflow ever
		long rv = APIAccessor.chores().heapUsed(id);
		if (rv < 0L)
			return 0;
		else if (rv > Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		return (int)rv;
		*/
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
		throw new todo.TODO();
		/*
		int id = this.id,
			flags;
		switch ((flags = APIAccessor.chores().flags(id)) &
			ChoreManager.FLAG_PRIORITY_MASK)
		{
			case ChoreManager.PRIORITY_MINIMUM:
				return TaskPriority.MIN;
				
			case ChoreManager.PRIORITY_NORMAL:
				return TaskPriority.NORM;
				
			case ChoreManager.PRIORITY_MAXIMUM:
				return TaskPriority.MAX;
			
				// {@squirreljme.error DG04 The specified task is not valid.
				// (The task ID; The task flags)}
			default:
				throw new IllegalStateException(
					String.format("DG04 %d %d", id, flags));
		}
		*/
	}
	
	/**
	 * Returns the status of this task.
	 *
	 * @return The task status.
	 * @since 2016/06/24
	 */
	public TaskStatus getStatus()
	{
		throw new todo.TODO();
		/*
		int id = this.id,
			flags;
		switch ((flags = APIAccessor.chores().flags(id)) &
			ChoreManager.FLAG_STATUS_MASK)
		{
			case ChoreManager.STATUS_EXITED_FATAL:
				return TaskStatus.EXITED_FATAL;

			case ChoreManager.STATUS_EXITED_REGULAR:
				return TaskStatus.EXITED_REGULAR;

			case ChoreManager.STATUS_EXITED_TERMINATED:
				return TaskStatus.EXITED_TERMINATED;

			case ChoreManager.STATUS_RUNNING:
				return TaskStatus.RUNNING;

			case ChoreManager.STATUS_START_FAILED:
				return TaskStatus.START_FAILED;

			case ChoreManager.STATUS_STARTING:
				return TaskStatus.STARTING;
			
				// {@squirreljme.error DG05 The specified task is not valid.
				// (The task ID; The task flags)}
			default:
				throw new IllegalStateException(
					String.format("DG04 %5 %d", id, flags));
		}
		*/
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
		throw new todo.TODO();
		/*
		// Has this flag been cached? Use that
		if (this._cachedissystem)
			return this._issystem;
		
		// Determine if this is a system chore
		boolean issystem = ((APIAccessor.chores().flags(this.id) &
			ChoreManager.FLAG_SYSTEM) != 0);
		
		// Set as cache
		this._issystem = issystem;
		this._cachedissystem = true;
		return issystem;
		*/
	}
}

