// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.server;

import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgram;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;

/**
 * This class wraps the system task and provides access to kernel tasks from
 * within the kernel itself.
 *
 * @since 2017/12/27
 */
public final class DirectTask
	implements SystemTask
{
	/** The task of the current process. */
	protected final KernelTask current;
	
	/** The wrapped task to access. */
	protected final KernelTask wrapped;
	
	/**
	 * Initializes the wrapped task.
	 *
	 * @param __current The current execution task.
	 * @param __wrapped The task to provide access to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/27
	 */
	public DirectTask(KernelTask __current, KernelTask __wrapped)
		throws NullPointerException
	{
		if (__current == null || __wrapped == null)
			throw new NullPointerException("NARG");
		
		this.current = __current;
		this.wrapped = __wrapped;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public int flags()
	{
		return this.wrapped.flags(this.current);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public String mainClass()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public long metric(int __m)
	{
		return this.wrapped.metric(this.current, __m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public SystemProgram program()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public void restart()
	{
		throw new todo.TODO();
	}
}

