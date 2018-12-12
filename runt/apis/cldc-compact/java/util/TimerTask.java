// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This represents a tasks which can be run within a timer.
 *
 * @since 2018/12/11
 */
public abstract class TimerTask
	implements Runnable
{
	/** Indicates the task has been cancelled. */
	volatile boolean _cancel;
	
	/** The scheduled time for the task, undefined at first. */
	volatile long _schedtime =
		Long.MIN_VALUE;
	
	/** Was this scheduled? */
	volatile boolean _scheduled;
	
	/** Is this a repeated execution? */
	volatile boolean _repeated;
	
	/**
	 * Initializes the base timer task.
	 *
	 * @since 2018/12/11
	 */
	protected TimerTask()
	{
	}
	
	/**
	 * Cancels this task so that it no longer runs.
	 *
	 * @return This will return true if a future execution was canceled.
	 * @since 2018/12/11
	 */
	public boolean cancel()
	{
		// Was already canceled
		if (this._cancel)
			return false;
		
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
	public long scheduledExcutionTime()
	{
		return this._schedtime;
	}
}

