// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import java.util.Random;

/**
 * This manages the world map which consists of tiles.
 *
 * @since 2019/07/01
 */
public final class TileMap
{
	/** Each tile is 16x16 pixels. */
	public static final int TILE_PIXELS =
		16;
	
	/** Mask for pixels. */
	public static final int TILE_PIXELS_MASK =
		15;
	
	/** The size of the map. */
	public final MapSize size;
	
	/** The width of the map in tiles. */
	public final int tilewidth;
	
	/** The height of the map in tiles. */
	public final int tileheight;
	
	/** The internal tile data. */
	final byte[] _tiles;
	
	/**
	 * Initializes the tile map.
	 *
	 * @param __rand The random level generator.
	 * @param __size The map size to use.
	 * @param __numpl The number of players to make room for.
	 * @throws IllegalArgumentException If the player count is zero or
	 * negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/01
	 */
	public TileMap(Random __rand, MapSize __size, int __numpl)
		throws IllegalArgumentException, NullPointerException
	{
		if (__rand == null || __size == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BE0r Negative or zero players, there must be
		// at least one player. (The player count)}
		if (__numpl < 1)
			throw new IllegalArgumentException("BE0r " + __numpl);
		
		// Get and store size of the level
		int tilewidth = __size.width,
			tileheight = __size.height,
			tiledens = tilewidth * tileheight;
		this.size = __size;
		this.tilewidth = tilewidth;
		this.tileheight = tileheight;
		
		// Initialize data areas
		byte[] tiles = new byte[tiledens];
		this._tiles = tiles;
	}
}

