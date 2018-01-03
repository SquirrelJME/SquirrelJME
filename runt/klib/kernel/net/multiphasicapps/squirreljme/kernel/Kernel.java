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

import java.util.Map;
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
	/** Tasks which exist within the kernel. */
	private Map<Integer, KernelTask> _tasks =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the base kernel.
	 *
	 * @since 2018/01/02
	 */
	protected Kernel()
	{
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
			// {@squirreljme.error AP0g The specified task does not exist.
			// (The task index)}
			KernelTask rv = tasks.get(__id);
			if (rv == null)
				throw new NoSuchKernelTaskException(
					String.format("AP0g %d", __id));
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
}

