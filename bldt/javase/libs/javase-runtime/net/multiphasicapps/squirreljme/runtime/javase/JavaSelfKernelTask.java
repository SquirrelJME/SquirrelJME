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
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTaskFlag;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTaskMetric;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTaskStatus;

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
		super(0, ~0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	protected int accessFlags()
	{
		return KernelTaskFlag.SYSTEM |
			KernelTaskStatus.RUNNING;
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
			case KernelTaskMetric.PRIORITY:
				return -100;
				
			case KernelTaskMetric.MEMORY_USED:
				return this.accessMetric(KernelTaskMetric.MEMORY_TOTAL) -
					this.accessMetric(KernelTaskMetric.MEMORY_FREE);
				
			case KernelTaskMetric.MEMORY_FREE:
				return Runtime.getRuntime().freeMemory();
				
			case KernelTaskMetric.MEMORY_TOTAL:
				return Runtime.getRuntime().totalMemory();
				
			default:
				return Long.MIN_VALUE;
		}
	}
}

