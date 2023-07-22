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
	/** Indicates the task has been cancelled. */
	volatile boolean _cancel;
	
	/** The scheduled time for the task, undefined at first. */
	volatile long _schedtime =
		Long.MIN_VALUE;
	
	/** The last run time. */
	volatile long _lastrun =
		Long.MIN_VALUE;
	
	/** Is this being run? */
	volatile boolean _inrun;
	
	/** Was this scheduled? */
	volatile boolean _scheduled;
	
	/** Is this a repeated execution? */
	volatile boolean _repeated;
	
	/** Fixed repeat? */
	volatile boolean _fixed;
	
	/** The period. */
	volatile long _period;
	
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
		// Was already canceled
		if (this._cancel)
			return false;
		
		this._cancel = true;
		return this._repeated || this._scheduled;
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
		if (this._inrun)
			return this._lastrun;
		return this._schedtime;
	}
}

