// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.gui.lcdui;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Graphics;

/**
 * This is the canvas which is used to draw the actual game.
 *
 * @since 2016/10/08
 */
public class LCDCanvas
	extends GameCanvas
{
	/**
	 * Initializes the canvas.
	 *
	 * @since 2016/10/08
	 */
	public LCDCanvas()
	{
		super(false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/10
	 */
	public Graphics getGraphics()
	{
		return super.getGraphics();
	}
}

