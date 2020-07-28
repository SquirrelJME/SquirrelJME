// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.canvas;

import javax.microedition.lcdui.Display;
import lcdui.FormUtils;

/**
 * Tests that repainting works.
 *
 * @since 2020/07/27
 */
public class TestCanvasRepaint
	extends BaseCanvas
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/27
	 */
	@Override
	public void test(Display __display, CanvasPlatform __platform)
	{
		// Wait for the canvas to settle
		FormUtils.flushAndWait(500);
		
		// Mark the time and request a repaint
		long base = System.currentTimeMillis();
		__platform.repaint();
		
		boolean gotRepaint = false; 
		
		// Wait for the signal
		long end = FormUtils.futureTime(3000);
		while (FormUtils.untilThen(end))
		{
			if (__platform.queryLastRepaint() > base)
			{
				gotRepaint = true;
				break;
			}
		}
		
		this.secondary("repainted", gotRepaint);
	}
}
