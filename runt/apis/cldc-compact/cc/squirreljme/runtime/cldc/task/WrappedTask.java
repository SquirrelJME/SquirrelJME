// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.task;

import cc.squirreljme.runtime.cldc.library.Library;
import cc.squirreljme.runtime.cldc.trust.SystemTrustGroup;

/**
 * This wraps a simple task index and provides a wrapper around the task
 * for system call purposes.
 *
 * @since 2018/03/03
 */
@Deprecated
public final class WrappedTask
	implements SystemTask
{
	/** The task index. */
	@Deprecated
	protected final int index;
	
	/**
	 * Initializes the task wrapper.
	 *
	 * @param __dx The task index.
	 * @since 2018/03/03
	 */
	@Deprecated
	public WrappedTask(int __dx)
	{
		this.index = __dx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof SystemTask))
			return false;
		
		return this.index == ((SystemTask)__o).index();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final int flags()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final int hashCode()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final Library[] libraryClassPath()
		throws SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final String mainClass()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final long metric(SystemTaskMetric __m)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final void restart()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final SystemTaskStatus status()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Deprecated
	@Override
	public final SystemTrustGroup trustGroup()
	{
		throw new todo.TODO();
	}
}

