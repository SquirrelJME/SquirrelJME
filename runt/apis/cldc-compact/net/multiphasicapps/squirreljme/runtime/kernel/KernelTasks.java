// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by the kernel to manage the current tasks which are
 * running.
 *
 * @since 2017/12/27
 */
public abstract class KernelTasks
{
	/** This task represents the kernel itself. */
	protected final KernelTask kerneltask;
	
	/** Tasks which are currently available. */
	private final List<KernelTask> _tasks =
		new ArrayList<>();
	
	/**
	 * Initializes the base kernel task manager.
	 *
	 * @return The kernel's task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/27
	 */
	public KernelTasks(KernelTask __st)
		throws NullPointerException
	{
		if (__st == null)
			throw new NullPointerException("NARG");
		
		// Always remember the system's task
		this.kerneltask = __st;
		this._tasks.add(__st);
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
				boolean issys = (0 != (t.flags(__by) & KernelTaskFlag.SYSTEM));
				if (__incsys || (!__incsys && !issys))
					rv.add(t);
			}
		}
		
		return rv.<KernelTask>toArray(new KernelTask[rv.size()]);
	}
}

