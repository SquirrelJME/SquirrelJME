// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;

/**
 * This represents a task which is provided by a standard process.
 *
 * @since 2017/12/11
 */
public final class JavaProcessKernelTask
	extends KernelTask
{
	/** The process to watch. */
	protected final Process process;
	
	/**
	 * Initializes the process task.
	 *
	 * @param __dx The task index.
	 * @since 2017/12/27
	 */
	public JavaProcessKernelTask(int __dx, Process __proc)
		throws NullPointerException
	{
		super(__dx);
		
		if (__proc == null)
			throw new NullPointerException("NARG");
		
		this.process = __proc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	protected int accessFlags()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	protected long accessMetric(int __m)
	{
		throw new todo.TODO();
	}
}

