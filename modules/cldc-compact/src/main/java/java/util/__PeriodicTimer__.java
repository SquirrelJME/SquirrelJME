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
	/** The basis monotonic clock time when this timer was created. */
	final long _basisMono;
	
	/** The basis UTC time when this timer was created. */
	final long _basisUtcs;
	
	/** The owning timer. */
	final Timer _timer;
	
	/** The owning task. */
	final TimerTask _task;
	
	/** The initialized delay to start from now, in nanoseconds. */
	final long _initDelay;
	
	/** Fixed delays from execution? */
	final boolean _initFixed;
	
	/** The period between each repetition, in nanoseconds. */
	final long _initPeriod;
	
	/** Repeat the task? */
	final boolean _initRepeat;
	
	/** Is this timer cancelled? */
	private volatile boolean _isCancelled;
	
	/** The time when this fires, in monotonic time. */
	private volatile long _fireMono;
	
	/** How many times has this been charged? */
	private volatile int _chargeCount;
	
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
	 * @param __period The period between each repetition, in milliseconds.
	 * @param __basisUtcs The basis UTC time.
	 * @param __basisMono The basis monotonic time, this is for accuracy if
	 * the system clock changes.
	 * @throws IllegalArgumentException If the date is negative or the
	 * period is zero or negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/10
	 */
	__PeriodicTimer__(Timer __timer, TimerTask __task,
		Date __first, long __delay,
		boolean __rep, boolean __fixed, long __period,
		long __basisUtcs, long __basisMono)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__timer == null || __task == null)
			throw new NullPointerException("NARG");
		
		if ((__first != null) == (__delay != Long.MIN_VALUE))
			throw new IllegalArgumentException("ILLV");
		
		// If a date is passed, the delay from now needs to be calculated
		// as this is far easier to work with compared to straight dates
		if (__first != null)
			__delay = __first.getTime() - __basisUtcs;
		
		// Always map delay and period to nanoseconds
		__delay *= 1_000000L;
		__period *= 1_000000L;
		
		// This event would occur in the past, or the period overflows
		// conversion to nanoseconds
		if (__period < 0 || __delay < 0 || (__delay + __basisUtcs) < 0)
			throw new IllegalArgumentException("ILLV");
		
		this._timer = __timer;
		this._task = __task;
		this._basisUtcs = __basisUtcs;
		this._basisMono = __basisMono;
		this._initDelay = __delay;
		this._initRepeat = __rep;
		this._initFixed = __fixed;
		
		// If no period is set, then just set the initial delay
		this._initPeriod = (__period == 0 ? __delay : __period);
		
		// Calculate the next fire time from the basis
		this.__charge(this._basisMono);
	}
	
	/**
	 * Cancels this timer.
	 *
	 * @return If the timer was cancelled.
	 * @since 2026/11/14
	 */
	final boolean __cancel()
	{
		synchronized (this)
		{
			// Was this originally cancelled?
			boolean was = this._isCancelled;
			
			// Invalidate times and set as cancelled
			if (!was)
			{
				this._isCancelled = true;
				this._fireMono = Long.MAX_VALUE;
			}
			
			// This would be the inverted state
			return !was;
		}
	}
	
	/**
	 * Charges the timer so that it is set to execute following the given
	 * basis.
	 *
	 * @param __basisMono The basis time.
	 * @return The resultant charge time, {@link Long#MAX_VALUE} means that
	 * the event is no longer scheduled.
	 * @since 2026/11/14
	 */
	long __charge(long __basisMono)
	{
		// Set and return the calculated time
		synchronized (this)
		{
			// Timer is cancelled?
			if (this._isCancelled)
			{
				this._fireMono = Long.MAX_VALUE;
				return Long.MAX_VALUE;
			}
			
			// Determine whether this event repeats or ends
			int chargeCount = this._chargeCount++;
			if (chargeCount > 0 && !this._initRepeat)
			{
				this._isCancelled = true;
				this._fireMono = Long.MAX_VALUE;
				return Long.MAX_VALUE;
			}
			
			// How much is the new fire time adjusted for?
			long addBy = (chargeCount == 0 ? this._initDelay :
				this._initPeriod);
			
			// If this is not on a fixed timer and this is a later periodic
			// run, then the basis time changes to the current monotonic time
			if (chargeCount > 0 && !this._initFixed)
				__basisMono = System.nanoTime();
			
			// Calculate the resultant time to use
			long calcFire = __basisMono + addBy;
			
			// This completely overflows time and the event would happen
			// before this one, so invalidate this
			if (calcFire < __basisMono)
			{
				this._isCancelled = true;
				this._fireMono = Long.MAX_VALUE;
				return Long.MAX_VALUE;
			}
			
			// Set the time for the next fire
			this._fireMono = calcFire;
			return calcFire;
		}
	}
	
	/**
	 * Is this timer eligible to run?
	 *
	 * @param __nowTime The current time.
	 * @return If this can run.
	 * @since 2026/11/14
	 */
	final boolean __eligible(long __nowTime)
	{
		synchronized (this)
		{
			return !this._isCancelled && __nowTime >= this._fireMono;
		}
	}
	
	/**
	 * Is this timer before the other timer?
	 *
	 * @param __other The other timer.
	 * @return If this is before that one.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/11/14
	 */
	final boolean __isBefore(__PeriodicTimer__ __other)
		throws NullPointerException
	{
		if (__other == null)
			throw new NullPointerException("NARG");
		
		return this._fireMono < __other._fireMono;
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
	
	/**
	 * Returns the time of which this is to execute.
	 *
	 * @return The time of execution.
	 * @since 2026/11/14
	 */
	final long __scheduledExecutionTime()
	{
		synchronized (this)
		{
			// The time needs to be converted back to a relative time then
			// offset back to the basis UTC time
			return this._basisUtcs +
				((this._fireMono - this._basisMono) / 1_000000L);
		}
	}
	
	/**
	 * Returns the sleep offset, in milliseconds.
	 *
	 * @param __nowTime The current monotonic time.
	 * @return The sleep offset, in milliseconds.
	 * @since 2026/11/15
	 */
	final long __sleepOffset(long __nowTime)
	{
		synchronized (this)
		{
			// This is always based on the fire time
			long fireMono = this._fireMono;
			if (fireMono == Long.MAX_VALUE || this._isCancelled)
				return Long.MAX_VALUE;
			return (fireMono - __nowTime) / 1_000000L;
		}
	}
}
