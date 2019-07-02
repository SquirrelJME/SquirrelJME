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
	
	/** The current cursor tile. */
	protected final MutablePoint cursor =
		new MutablePoint();
	
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
		// Get game details to draw
		Game game = this.game;
		
		// Get the canvas dimensions
		int cw = this.getWidth(),
			ch = this.getHeight();
		
		// Draw the tile map
		this.__drawTiles(__g, cw, ch, game.tilemap);
	}
	
	/**
	 * Draws the underlying tilemap.
	 *
	 * @param __g The graphics to draw to.
	 * @param __cw The canvas width.
	 * @param __ch The canvas height.
	 * @param __tilemap The raw tile data.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/02
	 */
	private final void __drawTiles(Graphics __g, int __cw, int __ch,
		TileMap __tilemap)
		throws NullPointerException
	{
		if (__g == null || __tilemap == null)
			throw new NullPointerException("NARG");
		
		// Size of the map in tiles
		int mtw = __tilemap.tilewidth,
			mth = __tilemap.tileheight;
		
		// Size of the view in tiles, an extra tile is given so that they are
		// not clipped off the right side at all!
		int vtw = (__cw + TileMap.TILE_PIXELS_MASK) / TileMap.TILE_PIXELS,
			vth = (__ch + TileMap.TILE_PIXELS_MASK) / TileMap.TILE_PIXELS;
		
		// Half of the view, used for centering and capping
		int vsw = vtw / 2,
			vsh = vth / 2;
		
		// Get the cursor position and determine how that is drawn, the view
		// is always centered on it for simplicity
		MutablePoint cursor = this.cursor;
		int cx = cursor.x,
			cy = cursor.y,
			vtx = cx - (vtw / 2),
			vty = cy - (vth / 2);
		
		// Passed bounds on the right side?
		if (vtx > mtw - vsw)
			vtx = mtw - vsw;
		if (vty > mth - vsh)
			vty = mth - vsh;
		if (vtx < 0)
			vtx = 0;
		if (vty < 0)
			vty = 0;
		
		// End tile positions
		int etx = vtx + vtw,
			ety = vty + vth;
		if (etx > mtw)
			etx = mtw;
		if (ety > mth)
			ety = mth;
		
		// Get the background logical tile data
		byte[] tiles = __tilemap._tiles;
		
		// Draw all tiles in the region
		for (int y = vty; y < ety; y++)
		{
			// Logical tile Y on screen
			int ly = (y - vty) * TileMap.TILE_PIXELS;
			
			// Base byte position in array
			int bbase = y * mtw;
			
			// Scan in row
			for (int x = vtx; x < etx; x++)
			{
				// Determine logical screen position of file
				int lx = (x - vtx) * TileMap.TILE_PIXELS;
				
				// Get the byte code for this tile
				byte b = tiles[bbase + x];
				
				// Draw background tile
				__g.drawImage(TileMap.imageBackground(b), lx, ly, 0);
				
				// Draw cursor box?
				if (cx == x && cy == y)
				{
					// Draw solid line
					__g.setStrokeStyle(Graphics.SOLID);
					__g.setColor(0xFFFF00);
					__g.drawRect(lx, ly,
						TileMap.TILE_PIXELS - 2, TileMap.TILE_PIXELS - 2);
					
					// Dotted purple box
					__g.setStrokeStyle(Graphics.DOTTED);
					__g.setColor(0xFF00FF);
					__g.drawRect(lx, ly,
						TileMap.TILE_PIXELS - 2, TileMap.TILE_PIXELS - 2);
					
					// Make it solid again
					__g.setStrokeStyle(Graphics.SOLID);
				}
			}
		}
	}
}

