// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.asm.TaskAccess;
import java.io.PrintStream;

/**
 * This represents a task which can be run.
 *
 * @since 2018/11/04
 */
public final class SpringTask
	implements Runnable
{
	/** The machine for this task. */
	protected final SpringMachine machine;
	
	/** The ID. */
	protected final int tid;
	
	/** Exit code. */
	volatile int _exitcode =
		Integer.MIN_VALUE;
	
	/**
	 * Initializes the task.
	 *
	 * @param __tid The task ID.
	 * @param __m The machine.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/04
	 */
	public SpringTask(int __tid, SpringMachine __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		this.machine = __m;
		this.tid = __tid;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public final void run()
	{
		this._exitcode = this.machine.runVm();
	}
}

