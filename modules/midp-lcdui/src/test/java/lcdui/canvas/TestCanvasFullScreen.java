// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.canvas;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;

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
		// Get initial sizes
		int width = __platform.getWidth();
		int height = __platform.getHeight();
		
		// Request full screen state
		__platform.setFullScreenMode(true);
		
		// Request a repaint and wait for it to occur
		__platform.repaint();
		__platform.serviceRepaints();
		
		// Query the sizes again for checking
		int newWidth = __platform.getWidth();
		int newHeight = __platform.getHeight();
		
		// Full-screen should cause the canvas to grow in size
		this.secondary("width", newWidth >= width);
		this.secondary("height", newHeight > height);
		
		// The canvas should be smaller or at the maximum display resolution
		this.secondary("wdisp", newWidth <= __display.getWidth());
		this.secondary("hdisp", newHeight <= __display.getHeight());
	}
}
