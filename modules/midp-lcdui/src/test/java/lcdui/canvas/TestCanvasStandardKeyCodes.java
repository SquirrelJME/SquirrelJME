// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.canvas;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;

/**
 * Tests that the standard key codes do in fact map to game keys.
 *
 * @since 2020/08/02
 */
public class TestCanvasStandardKeyCodes
	extends BaseCanvas
{
	/**
	 * {@inheritDoc}
	 * @since 2020/08/02
	 */
	@Override
	public void test(Display __display, CanvasPlatform __platform)
	{
		// Set bits within the mask
		long gameKeyMask = 0;
		for (int i = Canvas.KEY_SELECT; i <= Canvas.KEY_DELETE; i++)
			try
			{
				int gameKey = __platform.getGameAction(i);
				
				if (gameKey <= 0)
					continue;
				
				gameKeyMask |= (1 << gameKey);
			}
			catch (IllegalArgumentException ignored)
			{
			}
		
		// There should have been all the game keys in this
		this.secondary("mask", gameKeyMask);
	}
}
