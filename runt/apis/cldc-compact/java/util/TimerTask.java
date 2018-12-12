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

public abstract class TimerTask
	implements Runnable
{
	/** Indicates the task has been cancelled. */
	volatile boolean _cancel;
	
	/** The scheduled time for the task, undefined at first. */
	volatile long _schedtime =
		Long.MIN_VALUE;
	
	protected TimerTask()
	{
		throw new todo.TODO();
	}
	
	public boolean cancel()
	{
		throw new todo.TODO();
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

