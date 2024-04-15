// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import com.nttdocomo.util.TimeKeeper;
import com.nttdocomo.util.Timer;
import com.nttdocomo.util.TimerListener;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * This represents a "short-term" timer.
 * 
 * Unlike {@link Timer} which uses {@link TimerListener}, this one sends
 * events to {@link Canvas#processEvent(int, int)}.
 * 
 * @since 2022/10/10
 */
@Api
public final class ShortTimer
	implements TimeKeeper
{
	/** The timer ID. */
	final int _id;
	
	/** The canvas this timer is associated with. */
	final Reference<Canvas> _canvas;
	
	/** The timer we are really using. */
	private final Timer _timer;
	
	/** Has this timer been disposed? */
	private volatile boolean _isDisposed;
	
	/**
	 * Initializes the short timer.
	 * 
	 * @param __id The ID of this timer.
	 * @param __timer The target timer to use.
	 * @param __canvas The canvas this is associated with.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/10
	 */
	private ShortTimer(int __id, Timer __timer, Reference<Canvas> __canvas)
		throws NullPointerException
	{
		if (__timer == null || __canvas == null)
			throw new NullPointerException("NARG");
		
		this._id = __id;
		this._timer = __timer;
		this._canvas = __canvas;
		
		// Set the timer listener for the real timer we are basing on.
		__timer.setListener(new __ShortTimerListener__(
			new WeakReference<>(this)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	@Override
	@Api
	public void dispose()
	{
		// Only dispose the timer once
		synchronized (this)
		{
			if (this._isDisposed)
				return;
			this._isDisposed = true;
		}
		
		// Remove the timer from the canvas it is associated with
		Canvas canvas = this._canvas.get();
		if (canvas != null)
			synchronized (canvas)
			{
				// Remove it
				canvas._shortTimers.remove(this._id);
			}
		
		// Stop this timer so it no longer runs
		this.stop();
		
		// Kill off the other timer so it gets cleaned up
		this._timer.dispose();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	@Api
	public int getMinTimeInterval()
	{
		return this._timer.getMinTimeInterval();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	@Api
	public int getResolution()
	{
		return this._timer.getResolution();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	@Api
	public void start()
	{
		// Start internal timer
		this._timer.start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	@Api
	public void stop()
	{
		// Stop internal timer
		this._timer.stop();
	}
	
	/**
	 * Initializes a timer which sends events to a canvas, the timer will be
	 * associated with the canvas itself.
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
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	@Api
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
		ShortTimer result;
		synchronized (__canvas)
		{
			Map<Integer, Reference<ShortTimer>> timers = __canvas._shortTimers;
			
			// {@squirreljme.error AH0u Canvas already has a timer which is
			// associated with the given ID.}
			if (timers.containsKey(__id))
				throw new UIException(UIException.BUSY_RESOURCE, "AH0u");
			
			// Set up the timer that we actually use
			Timer timer = new Timer();
			timer.setTime(__interval);
			timer.setRepeat(__repeat);
			
			// Setup new timer
			result = new ShortTimer(__id, timer,
				new WeakReference<>(__canvas));
			
			// Store timer with its ID
			timers.put(__id, new WeakReference<>(result));
		}
		
		return result;
	}
}
