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

import cc.squirreljme.runtime.swm.DependencyInfo;
import cc.squirreljme.runtime.swm.MatchResult;
import cc.squirreljme.runtime.swm.ProvidedInfo;
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
		
		// Get all the suites that are available, since we need to determine
		// dependencies and such
		List<Suite> all = __SystemSuiteManager__.__allSuites();
		
		// Determine the suites which need to be loaded into the classpath
		// in order to run the given suite
		Suite[] depends = __SystemSuiteManager__.__matchDependencies(
			__s.__suiteInfo().dependencies(), false);
		
		// Debug
		todo.DEBUG.note("Suites: %s", Arrays.<Suite>asList(depends));
		
		// Setup new task internally
		if (true)
			throw new todo.TODO();
		
		// Build task object around given task
		if (true)
			throw new todo.TODO();
		
		throw new todo.TODO();
		/*
		// {@squirreljme.error DG09 Cannot launch the specified program
		// because it is of the system suite.}
		Library program = __s.__library();
		if (program == null)
			throw new IllegalArgumentException(String.format("DG09 %s %s",
				__s.getName(), __cn));
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
}

