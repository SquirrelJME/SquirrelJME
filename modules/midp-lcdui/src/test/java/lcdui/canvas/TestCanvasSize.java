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
		this.secondary("width", __platform.getWidth() > 0);
		this.secondary("height", __platform.getHeight() > 0);
	}
}
