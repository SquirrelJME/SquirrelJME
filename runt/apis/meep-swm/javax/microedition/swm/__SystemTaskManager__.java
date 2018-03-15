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

import cc.squirreljme.runtime.cldc.library.Library;
import cc.squirreljme.runtime.cldc.service.ServiceAccessor;
import cc.squirreljme.runtime.cldc.system.EasyCall;
import cc.squirreljme.runtime.cldc.system.SystemCall;
import cc.squirreljme.runtime.cldc.system.SystemFunction;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.cldc.task.WrappedTask;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

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
	
	/** The mapping of system tasks to tasks. */
	private final Map<SystemTask, Reference<Task>> _tasks =
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
		// Lock so that the task list is always up to date
		Task[] rv;
		synchronized (this.lock)
		{
			IntegerArray tids = SystemCall.EASY.taskList(__incsys);
			int n = tids.length();
			
			// Wrap all the tasks
			rv = new Task[n];
			for (int i = 0; i < n; i++)
				rv[i] = this.__ofTask(new WrappedTask(tids.get(i)));
		}
		
		// Wrap array instead of creating a new list for speed
		return Arrays.<Task>asList(rv);
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
		
		// {@squirreljme.error DG09 Cannot launch the specified program
		// because it is of the system suite.}
		Library program = __s.__library();
		if (program == null)
			throw new IllegalArgumentException(String.format("DG09 %s %s",
				__s.getName(), __cn));
		throw new todo.TODO();
		/*
		return this.__ofTask(SystemCall.launchTask(program, __cn, ~0));
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
	
	/**
	 * Returns a wrapped task for the given system task.
	 *
	 * @param __t The task to wrap.
	 * @return The wrapped task for the system task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/10
	 */
	final Task __ofTask(SystemTask __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Use pre-existing task or rewrap
		Map<SystemTask, Reference<Task>> tasks = this._tasks;
		synchronized (this.lock)
		{
			Reference<Task> ref = tasks.get(__t);
			Task rv;
			
			if (ref == null || null == (rv = ref.get()))
				tasks.put(__t, new WeakReference<>(
					(rv = new Task(__t))));
			
			return rv;
		}
	}
}

