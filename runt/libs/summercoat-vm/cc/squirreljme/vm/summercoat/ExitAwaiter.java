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
	/** The root machine to check for all tasks being terminated. */
	protected final RootMachine machine;
	
	/** The main task to run. */
	protected final RunningTask task;
	
	/**
	 * Initializes the exit awaiter, when the running task ends it will wait
	 * until the root machine has no running tasks.
	 *
	 * @param __rm The machine with the tasks.
	 * @param __t The task to run.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/02
	 */
	public ExitAwaiter(RootMachine __rm, RunningTask __t)
		throws NullPointerException
	{
		if (__rm == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.machine = __rm;
		this.task = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/02
	 */
	@Override
	public final int runVm()
		throws VMException
	{
		throw new todo.TODO();
	}
}

