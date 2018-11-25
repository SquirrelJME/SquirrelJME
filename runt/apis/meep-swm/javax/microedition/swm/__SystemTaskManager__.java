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

import cc.squirreljme.runtime.cldc.asm.TaskAccess;
import cc.squirreljme.runtime.swm.DependencyInfo;
import cc.squirreljme.runtime.swm.MatchResult;
import cc.squirreljme.runtime.swm.ProvidedInfo;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the task manager which interfaces with the CLDC system support
 * methods to provide access to tasks and such.
 *
 * @since 2017/12/07
 */
final class __SystemTaskManager__
	implements TaskManager
{
	/** Mapping of task IDs to tasks. */
	static final Map<Integer, Task> _TASKS =
		new HashMap<>();
	
	/** Internal lock for chore management. */
	protected final Object lock =
		new Object();
	
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
		throw new todo.TODO();
		/*
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
		*/
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
	 * @since 2018/10/29
	 */
	@Override
	public Task startTask(Suite __s, String __cn)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__s == null || __cn == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DG0w Cannot start a non-application suite.}
		if (__s.getSuiteType() != SuiteType.APPLICATION)
			throw new IllegalArgumentException("DG0w");
		
		// Get all the suites that are available, since we need to determine
		// dependencies and such
		List<Suite> all = __SystemSuiteManager__.__allSuites();
		
		// Determine the suites which need to be loaded into the classpath
		// in order to run the given suite
		Suite[] depends = __SystemSuiteManager__.__matchDependencies(
			__s.__suiteInfo().dependencies(), false);
		
		// Load suite names since we need to build the class path
		int n = depends.length;
		String[] names = new String[n + 1];
		for (int i = 0; i < n; i++)
			names[i] = depends[i]._name;
		
		// Add our boot suite to the last entry
		names[n] = __s._name;
		
		// Debug
		todo.DEBUG.note("Suites: %s", Arrays.<String>asList(names));
		
		// Setup new task internally
		int tid = TaskAccess.startTask(names, __cn,
			new String[0]);
		if (tid < 0)
		{
			// {@squirreljme.error DG0x Invalid entry point was specified
			// when starting task. (The entry point)}
			if (tid == TaskAccess.ERROR_INVALID_ENTRY)
				throw new IllegalArgumentException("DG0x " + __cn);
			return new Task(tid, __s);
		}
		
		// Otherwise use cached form
		return this.__getTask(tid, __s);
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
	 * Returns a task mapped to the given ID.
	 *
	 * @param __tid The task ID.
	 * @param __s The suite used.
	 * @return The task for the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/04
	 */
	static final Task __getTask(int __tid, Suite __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Lock on the tasks
		Map<Integer, Task> tasks = _TASKS;
		synchronized (tasks)
		{
			// There should not be duplicates
			Integer k = __tid;
			Task rv = tasks.get(k);
			if (rv != null)
				throw new todo.OOPS();
			
			tasks.put(k, (rv = new Task(__tid, __s)));
			return rv;
		}
	}
}

