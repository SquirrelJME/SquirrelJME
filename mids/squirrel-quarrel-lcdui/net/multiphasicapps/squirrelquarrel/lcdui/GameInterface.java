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

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.squirrelquarrel.Game;
import net.multiphasicapps.squirrelquarrel.GameSpeed;
import net.multiphasicapps.squirrelquarrel.Level;
import net.multiphasicapps.squirrelquarrel.TerrainType;

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
	
	/** The level to render and interact with. */
	protected final Level level;
	
	/** The width of the level in pixels. */
	protected final int levelpxw;
	
	/** The height of the level in pixels. */
	protected final int levelpxh;
	
	/** The mega tile cacher, used to combine megatiles into one image. */
	protected final MegaTileCacher mtcacher;
	
	/** The current game speed. */
	private volatile GameSpeed _speed =
		GameSpeed.NORMAL;
	
	/** The number of frames which have been rendered. */
	private volatile int _renderframe;
	
	/** The viewport X position. */
	private volatile int _viewx;
	
	/** The viewport Y position. */
	private volatile int _viewy;
	
	/** The view width. */
	private volatile int _vieww;
	
	/** The view height. */
	private volatile int _viewh;
	
	/** Is the game in a repaint? */
	private volatile boolean _inpaint;
	
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
		
		// Quicker to store pixel sizes here
		Level level;
		this.level = (level = __g.level());
		this.levelpxw = level.pixelWidth();
		this.levelpxh = level.pixelHeight();
		this.mtcacher = new MegaTileCacher(level);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void paint(Graphics __g)
	{
		// Mark as in paint
		this._inpaint = true;
		
		// Get the current frame the game is on
		Game game = this.game;
		Level level = game.level();
		int framenum = game.frameCount();
		
		// Get the viewport
		int viewx = this._viewx,
			viewy = this._viewy,
			vieww = this._vieww,
			viewh = this._viewh;
		
		// Draw all megatiles in view
		MegaTileCacher mtcacher = this.mtcacher;
		
		// Just draw the first megatile
		__g.drawImage(mtcacher.cacheMegaTile(0, 0), framenum & 31, 0, 0);
		
		// No longer painting
		this._inpaint = false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void run()
	{
		Game game = this.game;
		for (;;)
		{
			// Get the current game speed and entry time
			GameSpeed speed = this._speed;
			long enter = System.nanoTime();
			
			// Run a single game cycle
			game.run();
			
			// Request a repaint if there is enough time to draw
			long exit = System.nanoTime();
			if ((exit - enter) < speed.nanoFrameTime() && !this._inpaint)
				repaint();
			
			// Delay thread for the next frame
			exit = System.nanoTime();
			long durr = (speed.nanoFrameTime() - (exit - enter)) / 1_000_000L;
			if (durr > 0)
				try
				{
					Thread.sleep(durr);
				}
				catch (InterruptedException e)
				{
				}
		}
	}
	
	/**
	 * Translates the viewport.
	 *
	 * @param __x The relative X translation.
	 * @param __y The relative Y translation.
	 * @since 2017/02/10
	 */
	public void translateViewport(int __x, int __y)
	{
		// Translate the viewport
		int viewx = this._viewx + __x,
			viewy = this._viewy + __y,
			vieww = this._vieww,
			viewh = this._viewh,
			levelpxw = this.levelpxw,
			levelpxh = this.levelpxh;
		
		// Cap right side
		if (viewx + vieww >= levelpxw)
			viewx = levelpxw - vieww;
		
		// Cop bottom side
		if (viewy + viewh >= levelpxh)
			viewy = levelpxh - viewh;
		
		// Cap left side
		if (viewx < 0)
			viewx = 0;
		
		// Cap top
		if (viewy < 0)
			viewy = 0;
		
		// Set new viewport
		this._viewx = viewx;
		this._viewy = viewy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	protected void sizeChanged(int __w, int __h)
	{
		// Super-class might do some things
		super.sizeChanged(__w, __h);
		
		// Correct the viewport
		this._vieww = __w;
		this._viewh = __h;
		translateViewport(0, 0);
	}
}

