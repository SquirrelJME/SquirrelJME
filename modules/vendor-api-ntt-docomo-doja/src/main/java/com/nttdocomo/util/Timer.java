// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import com.nttdocomo.ui.Canvas;
import com.nttdocomo.ui.ShortTimer;
import com.nttdocomo.ui.UIException;
import java.lang.ref.WeakReference;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * A timer which sends timer events to a given listener.
 * 
 * Unlike {@link ShortTimer} which uses {@link Canvas#processEvent(int, int)},
 * this one sends events to a {@link TimerListener}.
 * 
 * This is effectively a duplicate of Java's standard {@link java.util.Timer}
 * except that it can be reused.
 * 
 * @see java.util.Timer
 * @since 2022/10/10
 */
@Api
public final class Timer
	implements TimeKeeper
{
	/** The minimum supported time interval. */
	static final byte _MIN_TIME_INTERVAL =
		1;
	
	/** The minimum supported timer resolution. */
	static final byte _TIMER_RESOLUTION =
		1;
	
	/** The expiration store to use. */
	@SquirrelJMEVendorApi
	final __ExpireStore__ _expire;
	
	/** The current interval, in milliseconds. */
	private volatile int _interval =
		Timer._MIN_TIME_INTERVAL;
	
	/** Does this timer repeat? */
	@SquirrelJMEVendorApi
	volatile boolean _repeats;
	
	/** Has this been disposed? */
	private volatile boolean _isDisposed;
	
	/** The currently active timer. */
	private volatile java.util.Timer _timer;
	
	/**
	 * Initializes the timer.
	 * 
	 * @since 2022/10/10
	 */
	@Api
	public Timer()
	{
		this._expire = new __ExpireStore__(
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
	@Api
	public void setListener(TimerListener __listener)
	{
		// Set for any later occurring expirations
		this._expire.__set(__listener);
	}
	
	/**
	 * Sets whether the timer repeats.
	 * 
	 * @param __repeat If the timer should repeat.
	 * @throws UIException If the timer has already been started.
	 * @since 2022/10/10
	 */
	@Api
	public void setRepeat(boolean __repeat)
		throws UIException
	{
		synchronized (this)
		{
			// Check if timer started already
			this.__checkStart();
			
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
	@Api
	public void setTime(int __interval)
		throws IllegalArgumentException, UIException
	{
		if (__interval < 0)
			throw new IllegalArgumentException("NEGV");
		
		synchronized (this)
		{
			// Check if timer started already
			this.__checkStart();
			
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
			// Check if timer started already
			this.__checkStart();
			
			/* {@squirreljme.error AH0v Cannot start a timer which has been
			disposed.} */
			if (this._isDisposed)
				throw new UIException(UIException.ILLEGAL_STATE,
					__error__("AH0v"));
			
			// Setup new timer
			java.util.Timer result = new java.util.Timer();
			
			// Start scheduling it
			__ExpireListener__ expired =
				new __ExpireListener__(this._expire);
			long interval = this._interval;
			if (this._repeats)
				result.schedule(expired, interval, interval);
			else
				result.schedule(expired, interval);
			
			// Store timer for later
			this._timer = result;
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
			// Do nothing if there is no timer
			java.util.Timer timer = this._timer;
			if (timer == null)
				return;
			
			// Stop and clear the timer
			timer.cancel();
			this._timer = null;
		}
	}
	
	/**
	 * Has this timer been started already?
	 *
	 * @since 2024/12/05
	 */
	private void __checkStart()
	{
		synchronized (this)
		{
			/* {@squirreljme.error AH5d Timer already started.} */
			java.util.Timer timer = this._timer;
			if (timer != null)
				throw new UIException(UIException.ILLEGAL_STATE,
					__error__("AH5d"));
		}
	}
}
