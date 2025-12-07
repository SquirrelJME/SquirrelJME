// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import javax.microedition.lcdui.Command;

/**
 * Handles returning to the game.
 *
 * @since 2019/12/25
 */
public final class ReturnToGameCommand
	extends Command
{
	/** The game interface. */
	protected final GameInterface gameinterface;
	
	/**
	 * Initializes the return to game.
	 *
	 * @param __gi The game interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/25
	 */
	public ReturnToGameCommand(GameInterface __gi)
		throws NullPointerException
	{
		super("Resume", Command.SCREEN, 0);
		
		if (__gi == null)
			throw new NullPointerException("NARG");
		
		this.gameinterface = __gi;
	}
}

