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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.squirrelquarrel.Level;

/**
 * This class is used to draw and update the automap which is used to give the
 * position of terrain and objects around the map. This class handles the
 * drawing aspects of it.
 *
 * @since 2017/02/12
 */
public class Automap
{
	/** The owning game interface. */
	protected final GameInterface gameinterface;
	
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
	 * @param __gi The owning game interface.
	 * @param __w The automap width.
	 * @param __h The automap height.
	 * @throws NullPointerException On null arguments
	 * @since 2017/02/12
	 */
	public Automap(GameInterface __gi, int __w, int __h)
		throws NullPointerException
	{
		// Check
		if (__gi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.gameinterface = __gi;
		this.width = __w;
		this.height = __w;
		
		// Save active image for later
		Image active = Image.createImage(__w, __h);
		this.active = active;
		this.graphics = active.getGraphics();
		
		// Get level size
		Level level = __gi.level();
		int levelpxw = level.pixelWidth(),
			levelpxh = level.pixelHeight();
		this.levelpxw = levelpxw;
		this.levelpxh = levelpxh;
		
		// However, initialize the terrain layer now
		Image terrain = Image.createImage(__w, __h);
		this.terrain = terrain;
		Graphics graphics = terrain.getGraphics();
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
}

