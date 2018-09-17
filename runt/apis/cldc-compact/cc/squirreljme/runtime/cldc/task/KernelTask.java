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
import java.io.InputStream;

/**
 * Virtual system task interface which priov.
 *
 * @since 2018/03/16
 */
@Deprecated
public final class KernelTask
	implements SystemTask
{
	/** The single instance of the kernel task. */
	@Deprecated
	public static final KernelTask INSTANCE =
		new KernelTask();
	
	/**
	 * Internally initialized.
	 *
	 * @since 2018/03/16
	 */
	@Deprecated
	private KernelTask()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof SystemTask))
			return false;
		
		return 0 == ((SystemTask)__o).index();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final int flags()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final int hashCode()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final int index()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final Library[] libraryClassPath()
		throws SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final String mainClass()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final long metric(SystemTaskMetric __m)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final void restart()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final SystemTaskStatus status()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/16
	 */
	@Override
	@Deprecated
	public final SystemTrustGroup trustGroup()
	{
		throw new todo.TODO();
	}
}

