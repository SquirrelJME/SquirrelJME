// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.canvas;

import javax.microedition.lcdui.Display;

/**
 * Tests that repainting works.
 *
 * @since 2020/07/27
 */
public class TestCanvasRepaint
	extends BaseCanvas
{
	/** The number of times to try. */
	private static final int _RETRIES =
		5;
	
	/** Timeout before retries. */
	private static final int _TIMEOUT =
		1000;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/27
	 */
	@Override
	public void test(Display __display, CanvasPlatform __platform)
	{
		// Mark the time and request a repaint
		long base = System.currentTimeMillis();
		
		boolean gotRepaint = false; 
		
		// Try multiple times, as due to loops and threading it is possible
		// for this to miss
		for (int i = 0; i < TestCanvasRepaint._RETRIES; i++)
		{
			// Try repainting...
			__platform.repaint();
			__platform.serviceRepaints();
			
			// Did this happen?
			if (__platform.queryLastRepaint() > base)
			{
				gotRepaint = true;
				break;
			}
			
			// Debug
			System.err.println("Waiting for a repaint...");
			
			// Wait again
			try
			{
				Thread.sleep(TestCanvasRepaint._TIMEOUT);
			}
			catch (InterruptedException ignored)
			{
			}
		}
		
		// Hopefully this was triggered
		this.secondary("repainted", gotRepaint);
	}
}
