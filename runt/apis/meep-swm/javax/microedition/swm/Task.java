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
import net.multiphasicapps.squirreljme.runtime.cldc.high.ChoreManager;

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
	/** Thread identifier. */
	protected final int id;
	
	/** Has the system task flag been cached? */
	private volatile boolean _cachedissystem;
	
	/** Is this a system task? */
	private volatile boolean _issystem;
	
	/**
	 * The constructor of this class is assumed to be internal use.
	 *
	 * @param __id The ID of the task.
	 * @since 2016/06/24
	 */
	Task(int __id)
	{
		this.id = __id;
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
	}
	
	/**
	 * Returns the suite that this task belongs to.
	 *
	 * @return The owning suite.
	 * @since 2016/06/24
	 */
	public Suite getSuite()
	{
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
		// Has this flag been cached? Use that
		if (this._cachedissystem)
			return this._issystem;
		
		// Determine if this is a system chore
		boolean issystem = APIAccessor.chores().isSystem(this.id);
		
		// Set as cache
		this._issystem = issystem;
		this._cachedissystem = true;
		return issystem;
	}
	
	/**
	 * Returns the ID of this task.
	 *
	 * @return The internal task ID.
	 * @since 2017/12/07
	 */
	final int __id()
	{
		return this.id;
	}
}

