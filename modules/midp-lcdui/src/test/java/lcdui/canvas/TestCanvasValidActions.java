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
 * Ensures that game actions do have keys.
 *
 * @since 2020/09/12
 */
public class TestCanvasValidActions
	extends BaseCanvas
{
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void test(Display __display, CanvasPlatform __platform)
	{
		int mask = 0;
		
		for (int gameKey : new int[]{Canvas.UP, Canvas.DOWN, Canvas.LEFT,
			Canvas.RIGHT, Canvas.FIRE, Canvas.GAME_A, Canvas.GAME_B,
			Canvas.GAME_C, Canvas.GAME_D})
			if (__platform.getKeyCode(gameKey) != 0)
				mask |= (1 << gameKey);
		
		this.secondary("mask", mask);
	}
}
