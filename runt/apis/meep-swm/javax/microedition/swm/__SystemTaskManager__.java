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
import java.util.Map;
import java.util.WeakHashMap;
import net.multiphasicapps.squirreljme.runtime.cldc.APIAccessor;
import net.multiphasicapps.squirreljme.runtime.cldc.chore.Chore;
import net.multiphasicapps.squirreljme.runtime.cldc.chore.Chores;

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
	
	/** The mapping of chores to tasks. */
	private final Map<Chore, Reference<Task>> _tasks =
		new WeakHashMap<>();
	
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
		throw new todo.TODO();
		/*
		return this.__byId(APIAccessor.chores().currentId());
		*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/07
	 */
	@Override
	public List<Task> getTaskList(boolean __incsys)
	{
		ArrayList<Task> rv = new ArrayList<>();
		
		// Lock so that the task list is always up to date
		Map<Chore, Reference<Task>> tasks = this._tasks;
		synchronized (this.lock)
		{
			// As a slight optimization, pre-allocate the returned list
			Chore[] chores = APIAccessor.chores().list(__incsys);
			rv.ensureCapacity(chores.length);
			
			for (Chore c : chores)
			{
				// Ignore system tasks
				if (!__incsys && c.isSystem())
					continue;
				
				Reference<Task> ref = tasks.get(c);
				Task task;
				
				if (ref == null || null == (task = ref.get()))
					tasks.put(c, new WeakReference<>((task = new Task(c))));
				
				rv.add(task);
			}
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
		
		throw new todo.TODO();
		/*
		SuiteType type = __s.getSuiteType();
		if (type == SuiteType.SYSTEM)
		{
			throw new todo.TODO();
		}
		
		throw new todo.TODO();
		*/
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
}

