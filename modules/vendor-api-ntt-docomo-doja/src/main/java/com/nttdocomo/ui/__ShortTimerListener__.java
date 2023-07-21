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
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import com.nttdocomo.util.Timer;
import com.nttdocomo.util.TimerListener;
import java.lang.ref.Reference;
import javax.microedition.lcdui.Display;

/**
 * Forwards timer events to {@link Canvas#processEvent(int, int)}.
 *
 * @since 2022/10/10
 */
class __ShortTimerListener__
	implements TimerListener, Runnable
{
	/** The timer we are referencing. */
	private final Reference<ShortTimer> _timer;
	
	/**
	 * Initializes the timer listener.
	 * 
	 * @param __shortTimer The timer to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/10
	 */
	__ShortTimerListener__(Reference<ShortTimer> __shortTimer)
		throws NullPointerException
	{
		if (__shortTimer == null)
			throw new NullPointerException("NARG");
		
		this._timer = __shortTimer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@SerializedEvent
	@Override
	public void run()
	{
		// Get the actual source timer
		ShortTimer timer = this._timer.get();
		if (timer == null)
			return;
		
		// The actual source canvas
		Canvas canvas = timer._canvas.get();
		if (canvas == null)
			return;
		
		// Process the actual event
		canvas.processEvent(com.nttdocomo.ui.Display.TIMER_EXPIRED_EVENT,
			timer._id);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/10
	 */
	@Override
	public void timerExpired(Timer __source)
	{
		// Get the actual source timer
		ShortTimer timer = this._timer.get();
		if (timer == null)
			return;
		
		// The actual source canvas
		Canvas canvas = timer._canvas.get();
		if (canvas == null)
			return;
		
		// Make the timer event happen in the event loop for the canvas;
		Display midpDisplay = canvas._midpCanvas.getCurrentDisplay();
		if (midpDisplay != null)
			midpDisplay.callSerially(this);
	}
}
