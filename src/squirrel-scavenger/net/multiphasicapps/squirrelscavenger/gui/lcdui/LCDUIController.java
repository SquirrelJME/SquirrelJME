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

import javax.microedition.lcdui.Display;
import net.multiphasicapps.squirrelscavenger.game.player.Controller;

/**
 * This is a controller which sources events from the LCDUI interface.
 *
 * @since 2016/10/08
 */
public class LCDUIController
	implements Controller
{
	/**
	 * Initializes the controller.
	 *
	 * @param __d The display to get events from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public LCDUIController(Display __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
	}
}

