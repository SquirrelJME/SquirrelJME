// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTaskFlag;

/**
 * This class is used by the kernel to manage the current tasks which are
 * running.
 *
 * @since 2017/12/27
 */
public abstract class KernelTasks
{
	/** Reference to the owning kernel. */
	protected final Reference<Kernel> kernel;
	
	/** This task represents the kernel itself. */
	protected final KernelTask kerneltask;
	
	/** Tasks which are currently available. */
	private final List<KernelTask> _tasks =
		new ArrayList<>();
	
	/**
	 * Initializes the base kernel task manager.
	 *
	 * @param __rk Reference to the owning kernel.
	 * @param __st The kernel's task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/27
	 */
	public KernelTasks(Reference<Kernel> __rk, KernelTask __st)
		throws NullPointerException
	{
		if (__rk == null || __st == null)
			throw new NullPointerException("NARG");
		
		this.kernel = __rk;
		
		// Always remember the system's task
		this.kerneltask = __st;
		this._tasks.add(__st);
	}
	
	/**
	 * Launches the specified program.
	 *
	 * @param __id The ID of the task to be launched.
	 * @param __cp The classpath used by the program, which includes all the
	 * dependencies.
	 * @param __program The main program to be launched.
	 * @param __mainclass The main class of the task.
	 * @param __perms The simple permissions for the task.
	 * @param __properties System properties to define.
	 * @return The resulting task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	protected abstract KernelTask accessLaunch(int __id, KernelProgram[] __cp,
		KernelProgram __program, String __mainclass, int __perms,
		Map<String, String> __properties)
		throws NullPointerException;
	
	/**
	 * Launches the specified program.
	 *
	 * @param __by The task launching the program.
	 * @param __program The program to launch.
	 * @param __mainclass The main class to enter via the midlet.
	 * @param __perms The permissions used for the new task.
	 * @param __props System properties to set in key/value pairs.
	 * @return The resulting task which has been executed.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the current task is not permitted to
	 * launch programs.
	 * @since 2017/12/31
	 */
	public final KernelTask launch(KernelTask __by, KernelProgram __program,
		String __mainclass, int __perms, String... __props)
		throws NullPointerException, SecurityException
	{
		if (__by == null || __program == null || __mainclass == null)
			throw new todo.TODO();
		
		// {@squirreljme.error AP09 The specified task is not permitted to
		// launch new tasks. (The task launching a new task)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.LAUNCH_TASK))
			throw new SecurityException(
				String.format("AP09 %s", __by));
		
		// Never grant more permissions than the launching task
		__perms &= __by.__simplePermissions();
		
		// Fill in optional extra properties to defines
		Map<String, String> properties = new LinkedHashMap<>();
		if (__props != null)
			for (int i = 0, n = __props.length; i < n; i += 2)
			{
				String key = __props[i],
					val = __props[i + 1];
				if (key != null && val != null)
					properties.put(__props[i], __props[i + 1]);
			}
		
		// Determine the classpath of the program to launch
		KernelProgram[] classpath = __kernel().__programs().__classPath(
			__program);
		
		// Lock
		List<KernelTask> tasks = this._tasks;
		synchronized (tasks)
		{
			// Determine the index to use for the new task
			int nextid = 1;
			for (int i = 0, n = tasks.size(); i < n; i++)
			{
				int tid = tasks.get(i).index();
				if (tid >= nextid)
					nextid = tid + 1;
			}
			
			// {@squirreljme.error AP0c Task launch returned null task, which
			// is not valid.}
			KernelTask rv = this.accessLaunch(nextid, classpath, __program,
				__mainclass, __perms, properties);
			if (rv == null)
				throw new RuntimeException("AP0c");
			
			tasks.add(rv);
			return rv;
		}
	}
	
	/**
	 * Lists the tasks which are currently available.
	 *
	 * @param __by The task which is performing the list get.
	 * @param __incsys If {@code true} then system tasks are included.
	 * @return The list of available tasks.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the specified task cannot obtain the task
	 * list.
	 * @since 2017/12/27
	 */
	public final KernelTask[] list(KernelTask __by, boolean __incsys)
		throws NullPointerException, SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0k The specified task is not permitted to
		// list available tasks. (The task requesting the task list)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.LIST_TASKS))
			throw new SecurityException(
				String.format("ZZ0k %s", __by));
		
		List<KernelTask> rv = new ArrayList<>();
		
		// Lock
		List<KernelTask> tasks = this._tasks;
		synchronized (tasks)
		{
			for (KernelTask t : tasks)
			{
				boolean issys = (0 != (t.flags(__by) & SystemTaskFlag.SYSTEM));
				if (__incsys || (!__incsys && !issys))
					rv.add(t);
			}
		}
		
		return rv.<KernelTask>toArray(new KernelTask[rv.size()]);
	}
	
	/**
	 * Returns the owning kernel.
	 *
	 * @return The owning kernel.
	 * @throws RuntimeException If the kernel was garbage collected.
	 * @since 2017/12/31
	 */
	final Kernel __kernel()
		throws RuntimeException
	{
		// {@squirreljme.error AP0a The kernel was garbage collected.}
		Kernel rv = this.kernel.get();
		if (rv == null)
			throw new RuntimeException("AP0a");
		return rv;
	}
	
	/**
	 * This is called when the client task has been terminated.
	 *
	 * @param __t The task which was terminated.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	final void __terminated(KernelTask __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		List<KernelTask> tasks = this._tasks;
		synchronized (tasks)
		{
			for (int i = 0, n = tasks.size(); i < n; i++)
				if (tasks.get(i) == __t)
				{
					tasks.remove(i);
					break;
				}
		}
	}
}

