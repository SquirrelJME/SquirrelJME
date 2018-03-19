// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.lcdui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.squirrelquarrel.world.World;
import net.multiphasicapps.squirrelquarrel.players.Player;
import net.multiphasicapps.squirrelquarrel.ui.Viewport;

/**
 * This class is used to draw and update the automap which is used to give the
 * position of terrain and objects around the map. This class handles the
 * drawing aspects of it.
 *
 * @since 2017/02/12
 */
public class Automap
{
	/** The viewport which stores where the screen is looking. */
	protected final Viewport viewport;
	
	/** The level to draw on. */
	protected final World world;
	
	/** The background terrain image. */
	protected final Image terrain;
	
	/** The active image to draw of the automap. */
	protected final Image active;
	
	/** This is used much. */
	protected final Graphics graphics;
	
	/** The automap width. */
	protected final int width;
	
	/** The automap height. */
	protected final int height;
	
	/** The level width in pixels. */
	protected final int levelpxw;
	
	/** The level height in pixels. */
	protected final int levelpxh;
	
	/**
	 * Initializes the automap.
	 *
	 * @param __v The viewport into the game.
	 * @param __w The automap width.
	 * @param __h The automap height.
	 * @throws NullPointerException On null arguments
	 * @since 2017/02/12
	 */
	public Automap(Viewport __v, int __w, int __h)
		throws NullPointerException
	{
		throw new todo.TODO();
		/*
		// Check
		if (__gi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.gameinterface = __gi;
		this.width = __w;
		this.height = __h;
		
		// Save active image for later
		Image active = Image.createImage(__w, __h);
		this.active = active;
		this.graphics = active.getGraphics();
		
		// Get level size
		Level level = __gi.level();
		this.level = level;
		int levelpxw = level.pixelWidth(),
			levelpxh = level.pixelHeight();
		this.levelpxw = levelpxw;
		this.levelpxh = levelpxh;
		
		// However, initialize the terrain layer now
		Image terrain = Image.createImage(__w, __h);
		this.terrain = terrain;
		__drawLayer(terrain.getGraphics(), false);
		*/
	}
	
	/**
	 * Returns the height of the automap.
	 *
	 * @return The automap height.
	 * @since 2017/02/12
	 */
	public int height()
	{
		return this.height;
	}
	
	/**
	 * Updates the automap and returns it.
	 *
	 * @return The updated automap.
	 * @since 2017/02/12
	 */
	public Image update()
	{
		throw new todo.TODO();
		/*
		GameInterface gameinterface = this.gameinterface;
		Image terrain = this.terrain;
		Image active = this.active;
		Graphics graphics = this.graphics;
		int width = this.width,
			height = this.height,
			levelpxw = this.levelpxw,
			levelpxh = this.levelpxh;
		
		// Full alpha
		graphics.setAlpha(0xFF);
		
		// Draw the terrain over the map
		graphics.drawImage(terrain, 0, 0, 0);
		
		// Draw the fog
		__drawLayer(graphics, true);
		
		// Draw units
		graphics.setAlpha(0xFF);
		
		// Draw where the viewport is in the automap
		graphics.setAlpha(0xFF);
		graphics.setColor(0x00FFFF);
		int viewx = gameinterface.viewportX(),
			viewy = gameinterface.viewportY(),
			vieww = gameinterface.viewportWidth(),
			viewh = gameinterface.viewportHeight();
		double pvx = width * ((double)viewx / (double)levelpxw),
			pvy = height * ((double)viewy / (double)levelpxh),
			pvw = width * ((double)vieww / (double)levelpxw),
			pvh = height * ((double)viewh / (double)levelpxh);
		graphics.drawRect((int)pvx, (int)pvy, (int)pvw, (int)pvh);
		
		// Draw a nice border around the map
		graphics.setColor(0xFFFFFF);
		graphics.drawRect(0, 0, width - 2, height - 2);
		
		// Return the active map
		return this.active;
		*/
	}
	
	/**
	 * Returns the width of the automap.
	 *
	 * @return The automap width.
	 * @since 2017/02/12
	 */
	public int width()
	{
		return this.width;
	}
	
	/**
	 * Draws a layer on the specified graphics.
	 *
	 * @param __g The target graphics to draw on.
	 * @param __fog If {@code true} then fog is drawn instead of tiles.
	 * @since 2017/02/15
	 */
	private void __drawLayer(Graphics __g, boolean __fog)
		throws NullPointerException
	{
		throw new todo.TODO();
		/*
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		Player player = this.gameinterface.player();
		Level level = this.level;
		int width = this.width,
			height = this.height,
			levelpxw = this.levelpxw,
			levelpxh = this.levelpxh;
		
		// If drawing fog, keep it light
		if (__fog)
			__g.setAlphaColor(0xAF000000);
		
		// Otherwise terrain is always visible
		else
			__g.setAlpha(0xFF);
		
		// Drawing loop
		for (double sy = 0, dy = 0, msx = (double)levelpxw / width,
			msy = (double)levelpxh / height, enddy = height,
			enddx = width; dy < enddy; sy += msy, dy += 1)
			for (double sx = 0, dx = 0; dx < enddx; sx += msx, dx += 1)
			{
				// Do not draw over revealed areas
				if (__fog)
				{
					if (level.pixelRevealed(player, (int)sx, (int)sy))
						continue;
				}
				
				// Use terrain color
				else
					__g.setColor(
						level.pixelTerrain((int)sx, (int)sy).color());
				
				// Draw single pixel as a line
				__g.drawLine((int)dx, (int)dy, (int)dx + 1, (int)dy);
			}
		*/
	}
}

