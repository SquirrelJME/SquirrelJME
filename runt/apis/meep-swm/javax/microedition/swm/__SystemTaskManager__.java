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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.multiphasicapps.squirreljme.runtime.cldc.APIAccessor;
import net.multiphasicapps.squirreljme.runtime.cldc.high.ChoreManager;

/**
 * This is the task manager which interfaces with the CLDC system support
 * methods to provide access to tasks and such.
 *
 * @since 2017/12/07
 */
final class __SystemTaskManager__
	implements TaskManager
{
	/** Internal lock for chore management. */
	protected final Object lock =
		new Object();
	
	/** The tasks which are currently available. */
	private final List<Task> _tasks =
		new ArrayList<>();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public void addTaskListener(TaskListener __tl)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public Task getCurrentTask()
	{
		return this.__byId(APIAccessor.chores().currentId());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public List<Task> getTaskList(boolean __incsys)
	{
		List<Task> rv = new ArrayList<>();
		
		// First get the raw chore IDs
		ChoreManager chores = APIAccessor.chores();
		int[] ids = chores.list(__incsys);
		
		// Need to inquiry multiple tasks at a time
		synchronized (this.lock)
		{
			this.__byIdBulk(rv, ids);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public void removeTaskListener(TaskListener __tl)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public boolean setForeground(Task __t)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public boolean setPriority(Task __t, TaskPriority __p)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public Task startTask(Suite __s, String __cn)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__s == null || __cn == null)
			throw new NullPointerException("NARG");
		
		SuiteType type = __s.getSuiteType();
		if (type == SuiteType.SYSTEM)
		{
			if (!getCurrentTask().isSystemTask())
				throw new 
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public boolean stopTask(Task __t)
		throws IllegalArgumentException, IllegalStateException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Searches for the task by the given ID.
	 *
	 * @param __id The ID of the task to get.
	 * @return The task for the given ID.
	 * @since 2017/12/07
	 */
	private final Task __byId(int __id)
	{
		List<Task> tasks = this._tasks;
		synchronized (this.lock)
		{
			// Pre-created task?
			Task rv;
			for (int i = 0, n = tasks.size(); i < n; i++)
				if ((rv = tasks.get(i)).__id() == __id)
					return rv;
			
			// Create new task representation
			rv = new Task(__id);
			tasks.add(rv);
			return rv;
		}
	}
	
	/**
	 * Searches for multiple task identifiers in bulk.
	 *
	 * @param __out The collection where tasks are written to.
	 * @param __ids The IDs to search for.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/07
	 */
	private final void __byIdBulk(Collection<Task> __out, int... __ids)
		throws NullPointerException
	{
		if (__out == null || __ids == null)
			throw new NullPointerException("NARG");
		
		// Bulk lookup
		List<Task> tasks = this._tasks;
		synchronized (this.lock)
		{
			for (int i = 0, n = __ids.length, o = tasks.size(); i < n; i++)
			{
				// Search for task
				int id = __ids[i];
				Task rv = null;
				for (int j = 0; j < o; j++)
				{
					Task use;
					if ((use = tasks.get(j)).__id() == id)
					{
						rv = use;
						break;
					}
				}
				
				// Initialize task if missing
				if (rv == null)
				{
					rv = new Task(id);
					tasks.add(rv);
				}
				
				// Return it
				__out.add(rv);
			}
		}
	}
}

