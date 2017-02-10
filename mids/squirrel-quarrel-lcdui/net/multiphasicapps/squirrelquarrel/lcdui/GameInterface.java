// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.lcdui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Graphics;
import net.multiphasicapps.squirrelquarrel.Game;

/**
 * This class provides an interface to the game, allowing for input to be
 * handled along with the game itself.
 *
 * @since 2017/02/08
 */
public class GameInterface
	extends Canvas
{
	/** The game to draw and interact with. */
	protected final Game game;
	
	/**
	 * Initializes the game.
	 *
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/08
	 */
	public GameInterface(Game __g)
		throws NullPointerException
	{
		//super(false, true);
		
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Setup details
		setTitle("Squirrel Quarrel");
		
		// Set
		this.game = __g;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void paint(Graphics __g)
	{
		// Needed to draw X
		int w = getWidth(),
			h = getHeight();
		
		// Draw one line
		__g.setColor(0x00FF00);
		__g.setStrokeStyle(Graphics.SOLID);
		__g.drawLine(0, 0, w, h);
		
		// Draw another
		__g.setColor(0x0000FF);
		__g.setStrokeStyle(Graphics.DOTTED);
		__g.drawLine(0, h, w, 0);
	}
}

