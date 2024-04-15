// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import com.nttdocomo.ui.UIException;
import java.lang.ref.Reference;
import java.util.TimerTask;

/**
 * This executes the timer listener accordingly when a timer event occurs.
 *
 * @since 2022/10/10
 */
class __DoJaTimerTask__
	extends TimerTask
{
	/** The timer we are executing. */
	private final Reference<Timer> _timer;
	
	/** Is this timer started? */
	private volatile boolean _isStarted;
	
	/**
	 * Initializes the timer task.
	 * 
	 * @param __timer The timer to execute.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/10
	 */
	__DoJaTimerTask__(Reference<Timer> __timer)
		throws NullPointerException
	{
		if (__timer == null)
			throw new NullPointerException("NARG");
		
		this._timer = __timer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	public boolean cancel()
	{
		synchronized (this)
		{
			// Set timer state as stopped
			this._isStarted = false;
			
			// Cancel the timer above
			return super.cancel();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	@Override
	public void run()
	{
		// Get our timer
		Timer timer = this._timer.get();
		if (timer == null)
		{
			// If we forgot about the timer we want, then just stop this from
			// running
			this.cancel();
			
			return;
		}
		
		// Lock on barrier
		synchronized (timer)
		{
			// Cancel our timer so it does not repeat
			if (!timer._repeats)
				this.cancel();
			
			// Execute timer
			TimerListener listener = timer._listener;
			if (listener != null)
				listener.timerExpired(timer);
		}
	}
	
	/**
	 * Checks whether the timer is started, if it is then this method will
	 * fail.
	 * 
	 * @throws UIException If the timer has been started.
	 * @since 2022/10/10
	 */
	void __checkStarted()
		throws UIException
	{
		synchronized (this)
		{
			// {@squirreljme.error AH0w Cannot perform action on a timer that
			// has already been started.}
			if (this._isStarted)
				throw new UIException(UIException.ILLEGAL_STATE, "AH0w");
		}
	}
	
	/**
	 * Starts the given timer.
	 * 
	 * @param __javaTimer The Java timer to use.
	 * @param __interval The time interval.
	 * @since 2022/10/10
	 */
	void __start(java.util.Timer __javaTimer, int __interval)
	{
		synchronized (this)
		{
			// Set as started
			this._isStarted = true;
			
			// Start the timer
			__javaTimer.scheduleAtFixedRate(this, __interval, __interval);
		}
	}
}
