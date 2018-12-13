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
 * The timer class is used to schedule events for the future which may
 * repeatedly happen or may happen once.
 *
 * This class is thread safe and multiple threads may interact with this
 * class.
 *
 * This class is not real-time and offers no gaurantee that tasks will execute
 * on time.
 *
 * All instances of this class create a background thread.
 *
 * @since 2018/12/11
 */
public class Timer
{
	/** The thread which runs the task of running things. */
	final __TimerThread__ _thread;
	
	/**
	 * Initializes a timer.
	 *
	 * @since 2018/12/11
	 */
	public Timer()
	{
		this("TimerThread");
	}
	
	/**
	 * Initializes a timer with a thread using the given name.
	 *
	 * @param __s The name of the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	public Timer(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Setup thread and start it
		__TimerThread__ thread;
		this._thread = (thread = new __TimerThread__(__s));
		thread.start();
	}
	
	/**
	 * Cancels the timer and all of its events.
	 *
	 * @since 2018/12/11
	 */
	public void cancel()
	{
		__TimerThread__ thread = this._thread;
		synchronized (thread)
		{
			// Cancel and interrupt the thread so it checks and wakes up
			if (!thread._cancel)
			{
				thread._cancel = true;
				thread.interrupt();
			}
		}
	}
	
	/**
	 * Purges all of the cancelled tasks so that they become garbage collected.
	 *
	 * @since 2018/12/11
	 */
	public void purge()
	{
		// Lock to prevent adds
		__TimerThread__ thread = this._thread;
		synchronized (thread)
		{
			// Remove every task which has been cancelled
			for (Iterator<TimerTask> it = thread._tasks.iterator();
				it.hasNext();)
				if (it.next()._cancel)
					it.remove();
		}
	}
	
	/**
	 * Schedules a task to run once at the given time.
	 *
	 * @param __task The task to run.
	 * @param __time The time when the task should run.
	 * @throws IllegalArgumentException If the date is negative.
	 * @throws IllegalStateException If a task was already scheduled, a task
	 * was cancelled, or this timer was cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	public void schedule(TimerTask __task, Date __time)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this._thread.__schedule(__task, __time, false, false, 0);
	}
	
	/**
	 * Schedules a task to run multiple times starting at the given date and
	 * executing every period.
	 *
	 * @param __task The task to run.
	 * @param __time The time when the task should run.
	 * @param __period The duration of time between each invocation.
	 * @throws IllegalArgumentException If the date is negative or the period
	 * is zero or negative.
	 * @throws IllegalStateException If a task was already scheduled, a task
	 * was cancelled, or this timer was cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	public void schedule(TimerTask __task, Date __time, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this._thread.__schedule(__task, __time, true, false, __period);
	}
	
	/**
	 * Schedules a task to run once at the given time.
	 *
	 * @param __task The task to run.
	 * @param __delay The delay before this task runs.
	 * @throws IllegalArgumentException If the delay is negative.
	 * @throws IllegalStateException If a task was already scheduled, a task
	 * was cancelled, or this timer was cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	public void schedule(TimerTask __task, long __delay)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this._thread.__schedule(__task, __delay, false, false, 0);
	}
	
	/**
	 * Schedules a task to run once at the given time repeating for the given
	 * period.
	 *
	 * @param __task The task to run.
	 * @param __delay The delay before this task runs.
	 * @param __period The delay before each subsequence execution.
	 * @throws IllegalArgumentException If the delay is negative or the period
	 * is zero or negative.
	 * @throws IllegalStateException If a task was already scheduled, a task
	 * was cancelled, or this timer was cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	public void schedule(TimerTask __task, long __delay, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this._thread.__schedule(__task, __delay, true, false, __period);
	}
	
	/**
	 * Schedules a task to run multiple times starting at the given date and
	 * executing every period, the tasks are scheduled again at the start of
	 * each execution rather than the end.
	 *
	 * @param __task The task to run.
	 * @param __time The time when the task should run.
	 * @param __period The duration of time between each invocation.
	 * @throws IllegalArgumentException If the date is negative or the period
	 * is zero or negative.
	 * @throws IllegalStateException If a task was already scheduled, a task
	 * was cancelled, or this timer was cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	public void scheduleAtFixedRate(TimerTask __task, Date __first,
		long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this._thread.__schedule(__task, __first, true, true, __period);
	}
	
	/**
	 * Schedules a task to run once at the given time repeating for the given
	 * period, execution is scheduled from the start of execution.
	 *
	 * @param __task The task to run.
	 * @param __delay The delay before this task runs.
	 * @param __period The delay before each subsequence execution.
	 * @throws IllegalArgumentException If the delay is negative or the period
	 * is zero or negative.
	 * @throws IllegalStateException If a task was already scheduled, a task
	 * was cancelled, or this timer was cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	public void scheduleAtFixedRate(TimerTask __task, long __delay,
		long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		this._thread.__schedule(__task, __delay, true, true, __period);
	}
}

