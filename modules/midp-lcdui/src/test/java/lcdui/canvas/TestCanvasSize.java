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
 * Tests that canvases have some size to them.
 *
 * @since 2020/08/02
 */
public class TestCanvasSize
	extends BaseCanvas
{
	/**
	 * {@inheritDoc}
	 * @since 2020/08/02
	 */
	@Override
	public void test(Display __display, CanvasPlatform __platform)
	{
		// Get the size
		int width = __platform.getWidth();
		int height = __platform.getHeight(); 
		
		// Debug
		System.err.printf("Canvas size: (%d, %d)%n", width, height);
		
		this.secondary("width", width > 0);
		this.secondary("height", height > 0);
	}
}
