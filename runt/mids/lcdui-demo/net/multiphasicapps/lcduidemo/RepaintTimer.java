// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import javax.microedition.lcdui.Canvas;

/**
 * This is a thread which does timing.
 *
 * @since 2018/11/22
 */
public class RepaintTimer
	extends Thread
{
	/** Canvas to repaint. */
	protected final Canvas canvas;
	
	/** The delay. */
	protected final int delay;
	
	/**
	 * Initialize the timer.
	 *
	 * @param __c The canvas to redraw.
	 * @param __ms The millisecond delay.
	 * @throws IllegalArgumentException If the delay is zero or negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/22
	 */
	public RepaintTimer(Canvas __c, int __ms)
		throws IllegalArgumentException, NullPointerException
	{
		super("LCDUIDemoRepaintTimer");
		
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AW01 The timer cannot be zero or negative.}
		if (__ms <= 0)
			throw new IllegalArgumentException("AW01");
		
		this.canvas = __c;
		this.delay = __ms;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	public void run()
	{
		Canvas canvas = this.canvas;
		int delay = this.delay;
		
		// Infinite loop
		for (;;)
		{
			// Stop if there is no display
			if (canvas.getCurrentDisplay() == null)
				break;
			
			// Repaint
			canvas.repaint();
			
			// Sleep
			try
			{
				Thread.sleep(delay);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}

