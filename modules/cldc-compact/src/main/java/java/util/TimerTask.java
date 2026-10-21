// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This represents a tasks which can be run within a timer.
 *
 * @since 2018/12/11
 */
@Api
public abstract class TimerTask
	implements Runnable
{
	/**
	 * Initializes the base timer task.
	 *
	 * @since 2018/12/11
	 */
	@Api
	protected TimerTask()
	{
	}
	
	/**
	 * Cancels this task so that it no longer runs.
	 *
	 * @return This will return true if a future execution was canceled.
	 * @since 2018/12/11
	 */
	@Api
	public boolean cancel()
	{
		return __PeriodicTimers__.__instance().__cancel(this);
	}
	
	/**
	 * Returns the scheduled execution time.
	 *
	 * If this task has not been scheduled, this value is undefined.
	 *
	 * @return The scheduled execution time.
	 * @since 2018/12/11
	 */
	@Api
	public long scheduledExecutionTime()
	{
		return __PeriodicTimers__.__instance()
			.__scheduledExecutionTime(this);
	}
}

