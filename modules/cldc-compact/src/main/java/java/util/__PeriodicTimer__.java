// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Stores state for a single periodic timer to be used
 * with {@link __PeriodicTimers__}, this is generally just for simplifying the
 * versatility of timers without causing an unmaintainable mess to occur.
 *
 * @since 2026/10/10
 */
final class __PeriodicTimer__
{
	/** The owning timer. */
	final Timer _timer;
	
	/** The owning task. */
	final TimerTask _task;
	
	/** The initialized delay to start from now. */
	final long _initDelay;
	
	/** The initialized date to start. */
	final Date _initFirst;
	
	/** Fixed delays from execution? */
	final boolean _initFixed;
	
	/** The period between each repetition. */
	final long _initPeriod;
	
	/** Repeat the task? */
	final boolean _initRepeat;
	
	/** Is this timer cancelled? */
	private volatile boolean _isCancelled;
	
	/**
	 * Initializes the periodic timer information.
	 *
	 * @param __task The task to run.
	 * @param __first The time when the task should run, this is mutually
	 * exclusive with {@code __delay} and must be {@code null} if a delay
	 * is set.
	 * @param __delay The delay before the first invocation, this is mutually
	 * exclusive with {@code __first} and must be {@link Long#MIN_VALUE} if
	 * a date is set.
	 * @param __rep Repeat the task?
	 * @param __fixed Fixed delays from execution?
	 * @param __period The period between each repetition.
	 * @throws IllegalArgumentException If the date is negative or the
	 * period is zero or negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/10
	 */
	__PeriodicTimer__(Timer __timer, TimerTask __task,
		Date __first, long __delay,
		boolean __rep, boolean __fixed, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__timer == null || __task == null)
			throw new NullPointerException("NARG");
		
		if ((__first == null) != (__delay == Long.MIN_VALUE))
			throw new IllegalArgumentException("ILLV");
		
		this._timer = __timer;
		this._task = __task;
		this._initFirst = __first;
		this._initDelay = __delay;
		this._initRepeat = __rep;
		this._initFixed = __fixed;
		this._initPeriod = __period;
	}
	
	/**
	 * Is this task cancelled?
	 *
	 * @return If this is cancelled.
	 * @since 2026/10/10
	 */
	final boolean __isCancelled()
	{
		synchronized (this)
		{
			return this._isCancelled;
		}
	}
}
