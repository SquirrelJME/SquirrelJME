// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import com.nttdocomo.ui.Canvas;
import com.nttdocomo.ui.ShortTimer;
import com.nttdocomo.ui.UIException;
import java.lang.ref.WeakReference;

/**
 * A timer which sends timer events to a given listener.
 * 
 * Unlike {@link ShortTimer} which uses {@link Canvas#processEvent(int, int)},
 * this one sends events to a {@link TimerListener}.
 * 
 * This is effectively a duplicate of Java's standard {@link java.util.Timer}.
 * 
 * @see java.util.Timer
 * @since 2022/10/10
 */
public final class Timer
	implements TimeKeeper
{
	/** The base Java timer. */
	private static volatile java.util.Timer _JAVA_TIMER;
	
	/** The minimum supported time interval. */
	static final byte _MIN_TIME_INTERVAL =
		1;
	
	/** The minimum supported timer resolution. */
	static final byte _TIMER_RESOLUTION =
		1;
	
	/** The timer task. */
	private final __DoJaTimerTask__ _timerTask;
	
	/** The current timer listener. */
	volatile TimerListener _listener;
	
	/** The current interval. */
	private volatile int _interval =
		Timer._MIN_TIME_INTERVAL;
	
	/** Does this timer repeat? */
	volatile boolean _repeats;
	
	/** Has this been disposed? */
	private volatile boolean _isDisposed;
	
	/**
	 * Initializes the timer.
	 * 
	 * @since 2022/10/10
	 */
	public Timer()
	{
		// Setup task to refer to this
		this._timerTask = new __DoJaTimerTask__(
			new WeakReference<>(this));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	public void dispose()
	{
		synchronized (this)
		{
			if (this._isDisposed)
				return;
			this._isDisposed = true;
			
			// Stop this from running
			this.stop();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	public int getMinTimeInterval()
	{
		return Timer._MIN_TIME_INTERVAL;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	public int getResolution()
	{
		return Timer._TIMER_RESOLUTION;
	}
	
	/**
	 * Sets the listener for the timer, if the timer has already started then
	 * this will set the new listener for the next timer event.
	 * 
	 * @param __listener The listener to set.
	 * @since 2022/10/10
	 */
	public void setListener(TimerListener __listener)
	{
		synchronized (this)
		{
			// Set for later starts
			this._listener = __listener;
		}
	}
	
	/**
	 * Sets whether the timer repeats.
	 * 
	 * @param __repeat If the timer should repeat.
	 * @throws UIException If the timer has already been started.
	 * @since 2022/10/10
	 */
	public void setRepeat(boolean __repeat)
		throws UIException
	{
		synchronized (this)
		{
			// Check timer state
			this._timerTask.__checkStarted();
			
			// Set the new interval
			this._repeats = __repeat;
		}
	}
	
	/**
	 * Sets the interval of the timer which fires after each interval.
	 * 
	 * @param __interval The interval in milliseconds, will always be at least
	 * or higher than {@link #getMinTimeInterval()}.
	 * @throws IllegalArgumentException If the interval is negative.
	 * @throws UIException If the timer has already been started.
	 * @since 2022/10/10
	 */
	public void setTime(int __interval)
		throws IllegalArgumentException, UIException
	{
		if (__interval < 0)
			throw new IllegalArgumentException("NEGV");
		
		synchronized (this)
		{
			// Check timer state
			this._timerTask.__checkStarted();
			
			// Set the new interval
			this._interval = Math.max(__interval, Timer._MIN_TIME_INTERVAL);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws UIException If the timer was already started.
	 * @since 2022/10/10
	 */
	@Override
	public void start()
		throws UIException
	{
		synchronized (this)
		{
			// {@squirreljme.error AH0v Cannot start a timer which has been
			// disposed.}
			if (this._isDisposed)
				throw new UIException(UIException.ILLEGAL_STATE, "AH0v");
			
			// Check timer state
			this._timerTask.__checkStarted();
			
			// Start the core timer
			this._timerTask.__start(Timer.__javaTimer(), this._interval);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	public void stop()
	{
		synchronized (this)
		{
			// Cancel the timer
			this._timerTask.cancel();
		}
	}
	
	/**
	 * Returns the single instance of a Java timer.
	 * 
	 * @return The single instance timer.
	 * @since 2022/10/10
	 */
	private static java.util.Timer __javaTimer()
	{
		// Was this already made?
		java.util.Timer result = Timer._JAVA_TIMER;
		if (result != null)
			return result;
		
		synchronized (Timer.class)
		{
			// Double check
			result = Timer._JAVA_TIMER;
			if (result != null)
				return result;
			
			// Setup timer for later
			result = new java.util.Timer("SquirrelJMEDoJaTimer");
			Timer._JAVA_TIMER = result;
			
			return result;
		}
	}
}
