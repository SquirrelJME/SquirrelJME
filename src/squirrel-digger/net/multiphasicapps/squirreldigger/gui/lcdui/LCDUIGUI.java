// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreldigger.gui.lcdui;

import net.multiphasicapps.squirreldigger.game.Game;
import net.multiphasicapps.squirreldigger.game.player.Controller;
import net.multiphasicapps.squirreldigger.gui.GUI;

/**
 * This implements the GUI on top of the LCD UI.
 *
 * @since 2016/10/08
 */
public class LCDUIGUI
	extends GUI
{
	/**
	 * Initializes the LCD UI interface.
	 *
	 * @param __g The game to draw over.
	 * @since 2016/10/08
	 */
	public LCDUIGUI(Game __g)
	{
		super(__g);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public Controller localController(int __id)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error BA03 Request for a negative controller.}
		if (__id < 0)
			throw new IndexOutOfBoundsException("BA03");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public void run()
	{
		throw new Error("TODO");
	}
}

