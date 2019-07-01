// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * This is an interface to the game which allows it to be drawn and interacted
 * with accordingly. This is effectively just a canvas which draws the game
 * and such.
 *
 * @since 2019/07/01
 */
public final class GameInterface
	extends Canvas
{
	/** The game this will be drawing and interacting with. */
	protected final Game game;
	
	/**
	 * Initializes the game interface.
	 *
	 * @param __g The game to interact with.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/01
	 */
	public GameInterface(Game __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Initialize variables
		this.game = __g;
		
		// Setup canvas view and such
		this.setTitle("Squirrel Quarrel");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/07/01
	 */
	@Override
	protected final void paint(Graphics __g)
	{
	}
}

