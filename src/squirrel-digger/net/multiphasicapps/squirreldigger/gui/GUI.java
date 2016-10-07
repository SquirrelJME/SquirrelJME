// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreldigger.gui;

import net.multiphasicapps.squirreldigger.game.Game;
import net.multiphasicapps.squirreldigger.game.player.Controller;

/**
 * This is the base class for GUI interfaces.
 *
 * @since 2016/10/06
 */
public abstract class GUI
	implements Runnable
{
	/** The game to render for. */
	protected final Game game;
	
	/**
	 * Initializes the base game.
	 *
	 * @param __g The game to render for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/06
	 */
	public GUI(Game __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __g;
	}
	
	/**
	 * Returns the controller for the local game.
	 *
	 * @param __id The local ID of the player.
	 * @return The local game controller or {@code null} if it is not valid
	 * for the given player ID.
	 * @since 2016/10/06
	 */
	public abstract Controller localController(int __id);
}

