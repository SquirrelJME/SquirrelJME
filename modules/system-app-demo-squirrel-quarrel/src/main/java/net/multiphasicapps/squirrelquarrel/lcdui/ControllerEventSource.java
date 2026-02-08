// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.lcdui;

import net.multiphasicapps.squirrelquarrel.game.EventSource;
import net.multiphasicapps.squirrelquarrel.game.Game;
import net.multiphasicapps.squirrelquarrel.ui.SplitScreen;

/**
 * Event source which grabs from the player's controller.
 *
 * @since 2019/03/24
 */
public class ControllerEventSource
	implements EventSource
{
	/** Game to control. */
	protected final Game game;
	
	/** Split-screen to determine which player is doing the action. */
	protected final SplitScreen splitscreen;
	
	/**
	 * Initializes the event source.
	 *
	 * @param __g The game used.
	 * @param __ss The split-screen for play.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	public ControllerEventSource(Game __g, SplitScreen __ss)
		throws NullPointerException
	{
		if (__g == null || __ss == null)
			throw new NullPointerException("NARG");
		
		this.game = __g;
		this.splitscreen = __ss;
	}
}

