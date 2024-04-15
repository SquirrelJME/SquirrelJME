// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This represents a task which can be run.
 *
 * @since 2018/11/04
 */
@Deprecated
public final class SpringTask
	implements Runnable
{
	/** The machine for this task. */
	@Deprecated
	protected final SpringMachine machine;
	
	/** The ID. */
	@Deprecated
	protected final int tid;
	
	/**
	 * Initializes the task.
	 *
	 * @param __tid The task ID.
	 * @param __m The machine.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/04
	 */
	@Deprecated
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
	@Deprecated
	public final void run()
	{
		// Just straight run the VM
		this.machine.runVm();
	}
}

