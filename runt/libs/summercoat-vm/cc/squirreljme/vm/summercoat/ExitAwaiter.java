// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMException;

/**
 * This class runs a running task and then just awaits until all all tasks
 * have been terminated before returning the exit code of the main process
 * even if it has already exited. This is so that 
 *
 * @since 2019/01/02
 */
public final class ExitAwaiter
	implements VirtualMachine
{
	/** Object containing all the task statuses, for termination checks. */
	protected final TaskStatuses statuses;
	
	/** The main task being run. */
	protected final TaskStatus maintask;
	
	/**
	 * Initializes the exit awaiter, when the running task ends it will wait
	 * until the root machine has no running tasks.
	 *
	 * @param __ts The statuses for each task, to determine if any are running.
	 * @param __t The main task status.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/02
	 */
	public ExitAwaiter(TaskStatuses __ts, TaskStatus __t)
		throws NullPointerException
	{
		if (__ts == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.statuses = __ts;
		this.maintask = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/02
	 */
	@Override
	public final int runVm()
		throws VMException
	{
		// Wait for all tasks to complete
		for (TaskStatuses statuses = this.statuses;;)
			try
			{
				if (statuses.awaitFinished(0))
					break;
			}
			catch (InterruptedException e)
			{
				// Do not care if we get interrupted, just try again
			}
		
		// Return the exit code for the main task
		return this.maintask._exitcode;
	}
}

