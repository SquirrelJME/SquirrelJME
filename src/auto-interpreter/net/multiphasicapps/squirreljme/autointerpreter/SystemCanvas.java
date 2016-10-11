// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.autointerpreter;

import javax.microedition.lcdui.game.GameCanvas;

/**
 * This is the canvas which is used to display what the interpreter "sees".
 *
 * @since 2016/10/11
 */
public class SystemCanvas
	extends GameCanvas
{
	/**
	 * Initializes the canvas.
	 *
	 * @since 2016/10/11
	 */
	public SystemCanvas()
	{
		super(true, true);
		
		// Set the title
		setTitle("SquirrelJME");
	}
}

