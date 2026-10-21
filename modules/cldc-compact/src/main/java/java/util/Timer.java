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
import org.jetbrains.annotations.Async;

/**
 * The timer class is used to schedule events for the future which may
 * repeatedly happen or may happen once.
 *
 * This class is thread safe and multiple threads may interact with this
 * class.
 *
 * This class is not real-time and offers no guarantee that tasks will execute
 * on time.
 * 
 * In SquirrelJME, to reduce thread contention and optimize for embedded
 * and cooperative threaded use, this utilizes only a single thread for all
 * timer instances.
 *
 * @since 2018/12/11
 */
@Api
public class Timer
{
	/** The name of this timer. */
	final String _name;
	
	/**
	 * Initializes a timer.
	 *
	 * @since 2018/12/11
	 */
	@Api
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
	@Api
	public Timer(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Note that this does not set up a thread at all and just waits until
		// an actual schedule occurs. There are a number of Java ME titles
		// which make a bunch of timers and thus never actually schedule
		// them, thus wasting thread space.
		
		// Store the name
		this._name = __s;
	}
	
	/**
	 * Cancels the timer and all of its events.
	 *
	 * @since 2018/12/11
	 */
	@Api
	public void cancel()
	{
		__PeriodicTimers__.__instance().__cancel(this);
	}
	
	/**
	 * Purges all the cancelled tasks so that they become garbage collected.
	 *
	 * @since 2018/12/11
	 */
	@Api
	public void purge()
	{
		__PeriodicTimers__.__instance().__purge(this);
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
	@Api
	@Async.Schedule
	public void schedule(TimerTask __task, Date __time)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		// Use generic periodic forward
		__PeriodicTimers__.__instance().__schedule(
			new __PeriodicTimer__(this, __task, __time,
				Long.MIN_VALUE,
				false, false, 0));
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
	@Api
	@Async.Schedule
	public void schedule(TimerTask __task, Date __time, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null || __time == null)
			throw new NullPointerException("NARG");
		
		if (__period <= 0)
			throw new IllegalArgumentException("NEGV");
		
		// Use generic periodic forward
		__PeriodicTimers__.__instance().__schedule(
			new __PeriodicTimer__(this, __task, __time,
				Long.MIN_VALUE, true, false, __period));
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
	@Api
	@Async.Schedule
	public void schedule(TimerTask __task, long __delay)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		if (__delay < 0)
			throw new IllegalArgumentException("NEGV");
		
		// Use generic periodic forward
		__PeriodicTimers__.__instance().__schedule(
			new __PeriodicTimer__(this, __task, null, __delay,
				false, false, 0));
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
	@Api
	@Async.Schedule
	public void schedule(TimerTask __task, long __delay, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		if (__delay < 0 || __period <= 0)
			throw new IllegalArgumentException("NEGV");
		
		// Use generic periodic forward
		__PeriodicTimers__.__instance().__schedule(
			new __PeriodicTimer__(this, __task, null, __delay,
				true, false, __period));
	}
	
	/**
	 * Schedules a task to run multiple times starting at the given date and
	 * executing every period, the tasks are scheduled again at the start of
	 * each execution rather than the end.
	 *
	 * @param __task The task to run.
	 * @param __first The time when the task should run.
	 * @param __period The duration of time between each invocation.
	 * @throws IllegalArgumentException If the date is negative or the period
	 * is zero or negative.
	 * @throws IllegalStateException If a task was already scheduled, a task
	 * was cancelled, or this timer was cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	@Api
	@Async.Schedule
	public void scheduleAtFixedRate(TimerTask __task, Date __first,
		long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null || __first == null)
			throw new NullPointerException("NARG");
		
		if (__period <= 0)
			throw new IllegalArgumentException("NEGV");
		
		// Use generic periodic forward
		__PeriodicTimers__.__instance().__schedule(
			new __PeriodicTimer__(this, __task, __first,
				Long.MIN_VALUE, true, true, __period));
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
	@Api
	@Async.Schedule
	public void scheduleAtFixedRate(TimerTask __task, long __delay,
		long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		if (__delay < 0 || __period <= 0)
			throw new IllegalArgumentException("NEGV");
		
		// Use generic periodic forward
		__PeriodicTimers__.__instance().__schedule(
			new __PeriodicTimer__(this, __task, null, __delay,
				true, true, __period));
	}
}

