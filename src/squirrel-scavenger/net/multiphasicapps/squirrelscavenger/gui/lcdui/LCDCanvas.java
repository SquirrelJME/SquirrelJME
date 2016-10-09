// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.gui.lcdui;

import javax.microedition.lcdui.game.GameCanvas;

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
}

