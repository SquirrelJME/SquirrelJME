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
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTaskFlag;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTaskMetric;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTaskStatus;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTasks;

/**
 * This task represents the kernel itself, this task is granted all
 * permissions.
 *
 * @since 2017/12/11
 */
public final class JavaSelfKernelTask
	extends KernelTask
{
	/**
	 * Initializes the self task.
	 *
	 * @since 2017/12/14
	 */
	public JavaSelfKernelTask()
	{
		super(null, 0, ~0, null, null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	protected int accessFlags()
	{
		return SystemTaskFlag.SYSTEM |
			SystemTaskStatus.RUNNING;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	protected long accessMetric(int __m)
	{
		switch (__m)
		{
			case SystemTaskMetric.PRIORITY:
				return -100;
				
			case SystemTaskMetric.MEMORY_USED:
				return this.accessMetric(SystemTaskMetric.MEMORY_TOTAL) -
					this.accessMetric(SystemTaskMetric.MEMORY_FREE);
				
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
	 * @since 2018/01/01
	 */
	@Override
	protected void accessTerminated()
	{
		// The kernel is dead at this point, so this never happens
	}
}

