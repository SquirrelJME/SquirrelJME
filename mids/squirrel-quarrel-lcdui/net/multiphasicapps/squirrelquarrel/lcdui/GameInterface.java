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
	implements Runnable
{
	/** The game to draw and interact with. */
	protected final Game game;
	
	/** The number of frames which have been rendered. */
	private volatile int _renderframe;
	
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
		int w = 640/*getWidth()*/,
			h = 480/*getHeight()*/;
		
		// For animation
		int renderframe = this._renderframe;
		this._renderframe = renderframe + 1;
		
		// Clip everything
		__g.translate(-__g.getTranslateX(), -__g.getTranslateY());
		__g.setClip(renderframe % (w / 2), renderframe % (h / 2),
			(w / 2), (h / 2));
		
		// Draw the clipping box
		__g.setColor(0xFF0000);
		__g.drawRect(renderframe % (w / 2), renderframe % (h / 2),
			(w / 2) - 1, (h / 2) - 1);
		
		// Translate the first line
		__g.translate(renderframe % w, (renderframe / 2) % h);
		
		// Draw one line
		__g.setColor(0x00FF00);
		__g.setStrokeStyle(Graphics.SOLID);
		__g.drawLine(0, 0, w, h);
		__g.drawRect((w / 5) * 1, (h / 5) * 1, (w / 5), (h / 5));
		
		// Do not translate the second
		__g.translate(-__g.getTranslateX(), -__g.getTranslateY());
		
		// Draw another
		__g.setColor(0x0000FF);
		__g.setStrokeStyle(Graphics.DOTTED);
		__g.drawLine(0, h, w, 0);
		__g.fillRect((w / 5) * 3, (h / 5) * 3, (w / 5), (h / 5));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void run()
	{
		for (;;)
			repaint();
	}
}

