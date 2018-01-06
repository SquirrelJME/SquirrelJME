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

import java.lang.ref.Reference;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelTask;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTaskFlag;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTaskMetric;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTaskStatus;

/**
 * This task represents the kernel itself.
 *
 * @since 2018/01/03
 */
public class JavaSystemTask
	extends KernelTask
{
	/**
	 * Initializes the system task.
	 *
	 * @param __k The owning kernel.
	 * @since 2018/01/03
	 */
	public JavaSystemTask(Reference<Kernel> __k)
	{
		super(__k, 0, null, null, null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public final int flags()
	{
		return SystemTaskFlag.SYSTEM |
			SystemTaskStatus.RUNNING;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public final long metric(int __m)
	{
		switch (__m)
		{
			case SystemTaskMetric.PRIORITY:
				return -100;
				
			case SystemTaskMetric.MEMORY_USED:
				return this.metric(SystemTaskMetric.MEMORY_TOTAL) -
					this.metric(SystemTaskMetric.MEMORY_FREE);
				
			case SystemTaskMetric.MEMORY_FREE:
				return Runtime.getRuntime().freeMemory();
				
			case SystemTaskMetric.MEMORY_TOTAL:
				return Runtime.getRuntime().totalMemory();
				
			default:
				return Long.MIN_VALUE;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public final void restart()
	{
		throw new todo.TODO();
	}
}

