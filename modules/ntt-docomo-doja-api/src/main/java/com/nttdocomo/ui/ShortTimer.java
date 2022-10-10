// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.nttdocomo.util.TimeKeeper;
import com.nttdocomo.util.Timer;
import com.nttdocomo.util.TimerListener;

/**
 * This represents a "short-term" timer.
 * 
 * Unlike {@link Timer} which uses {@link TimerListener}, this one sends
 * events to {@link Canvas#processEvent(int, int)}.
 * 
 * @since 2022/10/10
 */
public final class ShortTimer
	implements TimeKeeper
{
	/** The minimum supported time interval. */
	private static final byte _MIN_TIME_INTERVAL =
		1;
	
	/** The timer we are really using. */
	private final Timer _timer;
	
	/**
	 * Initializes the short timer.
	 * 
	 * @param __timer The target timer to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/10
	 */
	private ShortTimer(Timer __timer)
		throws NullPointerException
	{
		if (__timer == null)
			throw new NullPointerException("NARG");
		
		this._timer = __timer;
	}
	
	@Override
	public void dispose()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	public int getMinTimeInterval()
	{
		return ShortTimer._MIN_TIME_INTERVAL;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	public int getResolution()
	{
		return 1;
	}
	
	@Override
	public void start()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void stop()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes a timer which sends events to a canvas.
	 * 
	 * When an interval occurs, {@link Display#TIMER_EXPIRED_EVENT} will be
	 * sent to the canvas.
	 * 
	 * @param __canvas The canvas to send events to.
	 * @param __id The ID of this timer, this should be unique per canvas.
	 * @param __interval The amount of time between each fire of the timer.
	 * @param __repeat Should this timer repeat or be one-shot?
	 * @return The timer.
	 * @throws IllegalArgumentException If {@code __interval} is negative.
	 * @throws NullPointerException If no canvas was specified.
	 * @throws UIException Will be {@link UIException#BUSY_RESOURCE} if the
	 * canvas already as a timer with the given ID.
	 * @since 2022/10/10
	 */
	public static ShortTimer getShortTimer(Canvas __canvas, int __id,
		int __interval, boolean __repeat)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__canvas == null)
			throw new NullPointerException("NARG");
		if (__interval < 0)
			throw new IllegalArgumentException("NEGV");
		
		// Internally for SquirrelJME, ShortTimers are just Timers which use
		// Java's standard timers, this is to reduce duplication of timers.
		
		throw Debugging.todo();
	}
}
