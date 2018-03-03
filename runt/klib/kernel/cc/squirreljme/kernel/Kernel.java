// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

import cc.squirreljme.runtime.cldc.OperatingSystemType;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class represents the micro-kernel which manages the entire SquirrelJME
 * system and all of the needed IPC and running tasks/threads.
 *
 * There must be no way for an instance of this class to be obtained by any
 * client library, this means that once the kernel is initialized and passed to
 * an API bridge the pointer should be tossed out. It can however be
 * initialized as normal.
 *
 * @since 2017/12/08
 */
public abstract class Kernel
{
	/** The task which represents the kernel itself. */
	protected final KernelTask systemtask;
	
	/** The manager for services. */
	protected final ServiceManager services;
	
	/** Trust group for the system. */
	private final KernelTrustGroup _systemtrustgroup =
		new KernelTrustGroup();
	
	/** Tasks which exist within the kernel. */
	private final Map<Integer, KernelTask> _tasks =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the base kernel.
	 *
	 * @param __config The configuration to use when initializing the kernel.
	 * @since 2018/01/02
	 */
	protected Kernel(KernelConfiguration __config)
		throws NullPointerException
	{
		if (__config == null)
			throw new NullPointerException("NARG");
		
		// Initialize the service manager
		this.services = new ServiceManager(__config);
		
		// Initialize the system task, which acts as a special task
		KernelTask systemtask = __config.systemTask(new WeakReference<>(this));
		
		// {@squirreljme.error AP03 System task initialized with the wrong
		// properties.}
		if (systemtask == null || systemtask.index() != 0)
			throw new RuntimeException("AP03");
		
		// Need to remember this task
		_tasks.put(0, systemtask);
		this.systemtask = systemtask;
	}
	
	/**
	 * Returns the operating system type that SquirrelJME is running on.
	 *
	 * @return The operating system type SquirrelJME is running on.
	 * @since 2018/01/13
	 */
	public abstract OperatingSystemType operatingSystemType();
	
	/**
	 * Returns the task which represents the kernel.
	 *
	 * @return The task representing the kernel.
	 * @since 2018/03/01
	 */
	public final KernelTask systemTask()
	{
		return this.systemtask;
	}
	
	/**
	 * Returns the task associated with the given index.
	 *
	 * @param __id The index of the task to get.
	 * @return The task associated with the given index.
	 * @throws NoSuchKernelTaskException If no such task exists by that index.
	 * @since 2018/01/02
	 */
	public final KernelTask taskByIndex(int __id)
		throws NoSuchKernelTaskException
	{
		Map<Integer, KernelTask> tasks = this._tasks;
		synchronized (tasks)
		{
			// {@squirreljme.error AP05 The specified task does not exist.
			// (The task index)}
			KernelTask rv = tasks.get(__id);
			if (rv == null)
				throw new NoSuchKernelTaskException(
					String.format("AP05 %d", __id));
			return rv;
		}
	}
	
	/**
	 * Launches the specified task.
	 *
	 * @param __l The properties of the task to launch.
	 * @return The launched task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	public final KernelTask taskLaunch(KernelTaskLaunch __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Lists tasks.
	 *
	 * @param __incsys Should system tasks be included?
	 * @return The list of tasks.
	 * @since 2018/01/06
	 */
	public final KernelTask[] taskList(boolean __incsys)
	{
		Map<Integer, KernelTask> tasks = this._tasks;
		synchronized (tasks)
		{
			List<KernelTask> rv = new ArrayList<>(tasks.size());
			
			for (KernelTask t : tasks.values())
				rv.add(t);
			
			return rv.<KernelTask>toArray(new KernelTask[tasks.size()]);
		}
	}
	
	/**
	 * Returns the system trust group.
	 *
	 * @return The system trust group.
	 * @since 2018/01/11
	 */
	final KernelTrustGroup __systemTrustGroup()
	{
		return this._systemtrustgroup;
	}
}

