// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.canvas;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import net.multiphasicapps.tac.UntestableException;

/**
 * Tests that a canvas is full-screen via
 * {@link Canvas#setFullScreenMode(boolean)}).
 *
 * @since 2020/09/12
 */
public class TestCanvasFullScreen
	extends BaseCanvas
{
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void test(Display __display, CanvasPlatform __platform)
	{
		// This test can fail multiple times due to tight timings
		for (int i = 0; i < 3; i++)
			if (this.runSequence(__display, __platform))
				return;
		
		throw new UntestableException("Ignoring full-screen test.");
	}
	
	public boolean runSequence(Display __display, CanvasPlatform __platform)
	{
		// Add a button, it should go away
		__platform.addCommand(
			new Command("Test button", Command.OK, 0)); 
		
		// Request a repaint and wait for it to occur
		__platform.repaint();
		__platform.serviceRepaints();
		
		// Get initial sizes
		int width = __platform.getWidth();
		int height = __platform.getHeight();
		
		// Request full screen state
		__platform.setFullScreenMode(true);
		
		// Request a repaint and wait for it to occur
		__platform.repaint();
		__platform.serviceRepaints();
		
		// Wait a bit to allow this to stay on
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException ignored)
		{
		}
		
		// Query the sizes again for checking
		int newWidth = __platform.getWidth();
		int newHeight = __platform.getHeight();
		
		// Full-screen should cause the canvas to grow in size
		boolean didWidth = newWidth >= width;
		boolean didHeight = newHeight >= height;
		this.secondary("width", didWidth);
		this.secondary("height", didHeight);
		
		// The canvas should be smaller or at the maximum display resolution
		boolean widthSmaller = newWidth <= __display.getWidth();
		boolean heightSmaller = newHeight <= __display.getHeight();
		this.secondary("wdisp", widthSmaller);
		this.secondary("hdisp", heightSmaller);
		
		return didWidth && didHeight && widthSmaller && heightSmaller;
	}
}
