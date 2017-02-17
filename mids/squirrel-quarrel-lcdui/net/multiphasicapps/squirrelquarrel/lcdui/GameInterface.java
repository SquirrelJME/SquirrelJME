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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.KeyListener;
import net.multiphasicapps.squirrelquarrel.Game;
import net.multiphasicapps.squirrelquarrel.GameSpeed;
import net.multiphasicapps.squirrelquarrel.Level;
import net.multiphasicapps.squirrelquarrel.MegaTile;
import net.multiphasicapps.squirrelquarrel.Player;
import net.multiphasicapps.squirrelquarrel.TerrainType;
import net.multiphasicapps.squirrelquarrel.Unit;
import net.multiphasicapps.squirrelquarrel.UnitDeletedException;
import net.multiphasicapps.squirrelquarrel.UnitInfo;
import net.multiphasicapps.squirrelquarrel.UnitType;

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
	
	/** Input handler. */
	protected final GameInputHandler inputhandler =
		new GameInputHandler(this);
	
	/** The level to render and interact with. */
	protected final Level level;
	
	/** The automap. */
	protected final Automap automap;
	
	/** The width of the level in pixels. */
	protected final int levelpxw;
	
	/** The height of the level in pixels. */
	protected final int levelpxh;
	
	/** The width of the level in megatiles. */
	protected final int levelmtw;
	
	/** The height of the level in megatiles. */
	protected final int levelmth;
	
	/** The mega tile cacher, used to combine megatiles into one image. */
	protected final MegaTileCacher mtcacher;
	
	/** Units to draw. */
	private final List<Unit.Pointer> _drawunits =
		new ArrayList<>();
	
	/** The current game speed. */
	private volatile GameSpeed _speed =
		GameSpeed.SLOWEST;
	
	/** The currently active view player. */
	private volatile Player _viewplayer;
	
	/** The last frame the game was rendered on. */
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
	
	/** The starting X megatile. */
	private volatile int _msx;
	
	/** The starting Y megatile. */
	private volatile int _msy;
	
	/** The ending X megatile. */
	private volatile int _mex;
	
	/** The ending Y megatile. */
	private volatile int _mey;
	
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
		
		// Set initial view player
		this._viewplayer = __g.player(0);
		
		// Quicker to store pixel sizes here
		Level level;
		this.level = (level = __g.level());
		this.levelpxw = level.pixelWidth();
		this.levelpxh = level.pixelHeight();
		this.levelmtw = level.megaTileWidth();
		this.levelmth = level.megaTileHeight();
		this.mtcacher = new MegaTileCacher(level);
		
		// Use self as the key listener
		this.setKeyListener(this.inputhandler);
		
		// Setup automap
		this.automap = new Automap(this, 128, 128);
	}
	
	/**
	 * Returns the automap.
	 *
	 * @return The automap.
	 * @since 2017/02/12
	 */
	public Automap automap()
	{
		return this.automap;
	}
	
	/**
	 * Returns the game speed.
	 *
	 * @return The game speed.
	 * @since 2017/02/12
	 */
	public GameSpeed gameSpeed()
	{
		return this._speed;
	}
	
	/**
	 * Returns the level being played.
	 *
	 * @return The current level.
	 * @since 2017/02/12
	 */
	public Level level()
	{
		return this.level;
	}
	
	/**
	 * Converts a map X coordinate to a screen X coordinate.
	 *
	 * @param __x The input X coordinate.
	 * @return The output X coordinate.
	 * @since 2017/02/12
	 */
	public int mapToScreenX(int __x)
	{
		return __x - this._viewx;
	}
	
	/**
	 * Converts a map Y coordinate to a screen Y coordinate.
	 *
	 * @param __x The input Y coordinate.
	 * @return The output y coordinate.
	 * @since 2017/02/12
	 */
	public int mapToScreenY(int __y)
	{
		return __y - this._viewy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void paint(Graphics __g)
	{
		// If already painting, do not duplicate a paint
		if (this._inpaint)
			return;
		this._inpaint = true;
		
		// Get the current frame the game is on
		Game game = this.game;
		this._renderframe = game.frameCount();
		Level level = game.level();
		int framenum = game.frameCount();
		Player viewplayer = this._viewplayer;
		
		// Get the viewport
		int viewx = this._viewx,
			viewy = this._viewy,
			vieww = this._vieww,
			viewh = this._viewh;
		
		// MegaTiles do not often change
		MegaTileCacher mtcacher = this.mtcacher;
		
		// Megatile draw loop
		int msx = this._msx,
			msy = this._msy,
			mex = this._mex,
			mey = this._mey;
		for (int my = msy,
			sy = mapToScreenY(my * MegaTile.MEGA_TILE_PIXEL_SIZE),
			bsx = mapToScreenX(msx * MegaTile.MEGA_TILE_PIXEL_SIZE);
			my < mey; my++, sy += MegaTile.MEGA_TILE_PIXEL_SIZE)
			for (int mx = msx, sx = bsx; mx < mex; mx++,
				sx += MegaTile.MEGA_TILE_PIXEL_SIZE)
			{
				// Get the megatile here
				MegaTile mt = level.megaTile(mx, my);
				
				// Draw it
				__g.drawImage(mtcacher.cacheMegaTile(mt), sx, sy, 0);
					
				// Draw units in this mega tile
				__drawUnits(__g, mt);
				
				// Setup flags for fog drawing
				__g.setColor(0x000000);
				__g.setStrokeStyle(Graphics.DOTTED);
				
				// Draw fog scanlines
				for (int sty = 0, psy = sy, bpsx = sx;
					sty < MegaTile.TILES_PER_MEGA_TILE;
					sty++, psy += MegaTile.TILE_PIXEL_SIZE)
					for (int stx = 0, psx = bpsx;
						stx < MegaTile.TILES_PER_MEGA_TILE;
						stx++, psx += MegaTile.TILE_PIXEL_SIZE)
					{
						// Ignore revealed tiles
						if (mt.subTileRevealed(viewplayer, stx, sty))
							continue;
						
						// Otherwise find the next revealed tile (or the end)
						int end;
						for (end = stx + 1; end < MegaTile.TILES_PER_MEGA_TILE;
							end++)
							if (mt.subTileRevealed(viewplayer, end, sty))
								break;
						
						// Draw dotted lines for fog
						int endpx = sx + (stx * MegaTile.TILE_PIXEL_SIZE) +
							(end * MegaTile.TILE_PIXEL_SIZE),
							endpy = psy + MegaTile.TILE_PIXEL_SIZE;
						for (int py = psy; py < endpy; py++)
						{
							// Lines off to the side can get clipped where the
							// dot patterns stop being correct
							int bx = (psx < 0 ? 0 : psx);
							__g.drawLine(bx + (py & 1), py, endpx, py);
						}
						
						// Set current spot to the end
						stx = end;
						psx = bpsx + (end * MegaTile.TILE_PIXEL_SIZE);
					}
			}
		
		// Reset translation for the HUD
		__g.translate(-__g.getTranslateX(), -__g.getTranslateY());
		
		// Draw the automap in the bottom left corner
		Image map = this.automap.update();
		__g.setAlpha(160);
		__g.drawImage(map, 0, viewh, Graphics.LEFT | Graphics.BOTTOM);
		
		// No longer painting
		this._inpaint = false;
	}
	
	/**
	 * Returns the current view player.
	 *
	 * @return The view player.
	 * @since 2017/02/15
	 */
	public Player player()
	{
		return this._viewplayer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	protected void pointerDragged(int __x, int __y)
	{
		this.inputhandler.pointerDragged(__x, __y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	protected void pointerPressed(int __x, int __y)
	{
		this.inputhandler.pointerPressed(__x, __y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	protected void pointerReleased(int __x, int __y)
	{
		this.inputhandler.pointerReleased(__x, __y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void run()
	{
		Game game = this.game;
		GameInputHandler inputhandler = this.inputhandler;
		for (;;)
		{
			// Get the current game speed and entry time
			GameSpeed speed = this._speed;
			long enter = System.nanoTime();
			
			// Run a single game cycle
			game.run();
			
			// Perform local client event handling (commands and such)
			inputhandler.run();
			
			// Request a repaint if there is enough time to draw
			long exit = System.nanoTime();
			if ((exit - enter) < speed.nanoFrameTime() && !this._inpaint)
			{
				int gameframe = game.frameCount(),
					paintframe = this._renderframe;
				
				// Renderer seems to be a bit slow, skip the request
				if (gameframe < paintframe - 1)
					this._renderframe = gameframe;
				
				// Request repaint
				else
					repaint();
			}
			
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
	 * Sets the game speed.
	 *
	 * @param __gs The game speed to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/12
	 */
	public void setGameSpeed(GameSpeed __gs)
		throws NullPointerException
	{
		// Check
		if (__gs == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._speed = __gs;
	}
	
	/**
	 * Sets the viewport position.
	 *
	 * @param __x The absolute X position.
	 * @param __y The absolute Y position.
	 * @since 2017/02/10
	 */
	public void setViewport(int __x, int __y)
	{
		// Translate the viewport
		int viewx = __x,
			viewy = __y,
			vieww = this._vieww,
			viewh = this._viewh,
			levelpxw = this.levelpxw,
			levelpxh = this.levelpxh,
			levelmtw = this.levelmtw,
			levelmth = this.levelmth;
		
		// Cap right side
		if (viewx + vieww >= levelpxw - 1)
			viewx = (levelpxw - vieww) - 1;
		
		// Cop bottom side
		if (viewy + viewh >= levelpxh - 1)
			viewy = (levelpxh - viewh) - 1;
		
		// Cap left side
		if (viewx < 0)
			viewx = 0;
		
		// Cap top
		if (viewy < 0)
			viewy = 0;
		
		// Set new viewport
		this._viewx = viewx;
		this._viewy = viewy;
		
		// Recalculate tiles in view
		int msx = screenToMapX(0) / MegaTile.MEGA_TILE_PIXEL_SIZE,
			msy = screenToMapY(0) / MegaTile.MEGA_TILE_PIXEL_SIZE,
			mex = (screenToMapX(vieww) / MegaTile.MEGA_TILE_PIXEL_SIZE) + 1,
			mey = (screenToMapY(viewh) / MegaTile.MEGA_TILE_PIXEL_SIZE) + 1;
		
		// Cap
		if (mex > levelmtw)
			mex = levelmtw;
		if (mey > levelmth)
			mey = levelmth;
		
		// Set
		this._msx = msx;
		this._msy = msy;
		this._mex = mex;
		this._mey = mey;
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
		setViewport(this._viewx + __x, this._viewy + __y);
	}
	
	/**
	 * Converts a screen X coordinate to a map X coordinate.
	 *
	 * @param __x The input X coordinate.
	 * @return The output X coordinate.
	 * @since 2017/02/12
	 */
	public int screenToMapX(int __x)
	{
		return this._viewx + __x;
	}
	
	/**
	 * Converts a screen Y coordinate to a map Y coordinate.
	 *
	 * @param __x The input Y coordinate.
	 * @return The output y coordinate.
	 * @since 2017/02/12
	 */
	public int screenToMapY(int __y)
	{
		return this._viewy + __y;
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
	
	/**
	 * Returns the height of the viewport.
	 *
	 * @return The viewport height.
	 * @since 2017/02/12
	 */
	public int viewportHeight()
	{
		return this._viewh;
	}
	
	/**
	 * Returns the width of the viewport.
	 *
	 * @return The viewport width.
	 * @since 2017/02/12
	 */
	public int viewportWidth()
	{
		return this._vieww;
	}
	
	/**
	 * Returns the X position of the viewport.
	 *
	 * @return The viewport X position.
	 * @since 2017/02/12
	 */
	public int viewportX()
	{
		return this._viewx;
	}
	
	/**
	 * Returns the Y position of the viewport.
	 *
	 * @return The viewport Y position.
	 * @since 2017/02/12
	 */
	public int viewportY()
	{
		return this._viewy;
	}
	
	/**
	 * Draws units in this megatile.
	 *
	 * @param __g The target graphics.
	 * @param __mt The megatile to source from.
	 * @since 2017/02/17
	 */
	private void __drawUnits(Graphics __g, MegaTile __mt)
	{
		// Store the old clip
		int oldcx = __g.getClipX(),
			oldcy = __g.getClipY(),
			oldcw = __g.getClipWidth(),
			oldch = __g.getClipHeight();
		
		// Used for selection boxes
		__g.setColor(0xFFFF00);
		__g.setStrokeStyle(Graphics.SOLID);
		
		// Set new clipping so units are not drawn outside of the tile and
		// potentially into other tiles
		int mdx = mapToScreenX(__mt.x() * MegaTile.MEGA_TILE_PIXEL_SIZE),
			mdy = mapToScreenY(__mt.y() * MegaTile.MEGA_TILE_PIXEL_SIZE);
		__g.setClip(mdx, mdy, mdx + MegaTile.MEGA_TILE_PIXEL_SIZE,
			mdy + MegaTile.MEGA_TILE_PIXEL_SIZE);
		
		// Load units
		List<Unit.Pointer> drawunits = this._drawunits;
		drawunits.clear();
		__mt.loadLinkedUnits(drawunits);
		
		// Draw them
		for (int i = 0, n = drawunits.size(); i < n; i++)
			try
			{
				// Get unit, and its information
				Unit unit = drawunits.get(i).get();
				UnitType type = unit.type();
				if (type == null)
					continue;
				UnitInfo info = type.info();
				
				// Get draw position on the screen
				int dx = mapToScreenX(unit.centerX()),
					dy = mapToScreenY(unit.centerY());
				
				// Draw sprite
				__g.drawRect(dx - 10, dy - 10, 20, 20);
			}
			
			// Ignore due to threading potential
			catch (UnitDeletedException e)
			{
			}
		
		// Restore the old clip
		__g.setClip(oldcx, oldcy, oldcw, oldch);
	}
}

