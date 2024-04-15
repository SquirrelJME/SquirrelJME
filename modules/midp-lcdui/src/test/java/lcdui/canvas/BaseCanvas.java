// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.canvas;

import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import javax.microedition.lcdui.Display;
import lcdui.BaseDisplay;
import net.multiphasicapps.tac.OptionalFirstParameter;

/**
 * Base tests for canvases.
 *
 * @since 2020/07/27
 */
public abstract class BaseCanvas
	extends BaseDisplay
	implements OptionalFirstParameter
{
	/** The number of times to let the canvas settle before testing. */
	private static final int _SETTLE_COUNT =
		5;
	
	/**
	 * Performs the canvas test.
	 * 
	 * @param __display The display being run on.
	 * @param __platform The canvas platform used.
	 * @throws Throwable On any exception.
	 * @since 2020/07/27
	 */
	public abstract void test(Display __display, CanvasPlatform __platform)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/27
	 */
	@Override
	public final void test(Display __display, String __param)
		throws Throwable
	{
		CanvasPlatform platform = new CanvasPlatform();
		__display.setCurrent(platform);
		
		// Perform testing on the canvas
		try
		{
			// Allow the canvas to settle and properly appear
			for (int i = 0; i < BaseCanvas._SETTLE_COUNT; i++)
			{
				// Wait for the canvas events to settle
				__display.callSerially(new __Wait__());
				
				// Repaint and wait for those to appear
				platform.repaint();
				platform.serviceRepaints();
				
				// Flush events to settle these
				UIBackendFactory.getInstance(true).flushEvents();
				
				// Sleep a bit
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException ignored)
				{
				}
			}
			
			// Run the test
			this.test(__display, platform);
		}
		finally
		{
			if (__display.getCurrent() == platform)
				__display.removeCurrent();
		}
	}
}
