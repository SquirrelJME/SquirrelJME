// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.world;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This stores the mega tiles within the level along with storing level related
 * data structures.
 *
 * @since 2017/02/09
 */
public class World
{
	/** The mega tile array. */
	protected final MegaTile[] _tiles;
	
	/** The width of the level in tiles. */
	protected final int tilew;
	
	/** The height of the level in tiles. */
	protected final int tileh;
	
	/** The width of the level in pixels. */
	protected final int pixelw;
	
	/** The height of the level in pixels. */
	protected final int pixelh;
	
	/** The width of the level in megatiles. */
	protected final int megaw;
	
	/** The height of the level in megatiles. */
	protected final int megah;
	
	/**
	 * Initializes the level with the given initial settings.
	 *
	 * @param __is The initial settings to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	public World(InitialSettings __is)
		throws NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Initialize the tile map
		int mw = __is.mapWidth(),
			mh = __is.mapHeight();
		this._tiles = __initTiles(mw, mh);
		
		// Store sizes
		this.tilew = mw;
		this.tileh = mh;
		this.megaw = mw / MegaTile.TILES_PER_MEGA_TILE;
		this.megah = mh / MegaTile.TILES_PER_MEGA_TILE;
		this.pixelw = mw * Tile.PIXEL_SIZE;
		this.pixelh = mh * Tile.PIXEL_SIZE;
	}
	
	/**
	 * Returns the megatile at the given megatile coordinates.
	 *
	 * @param __x The X coordinate in megatiles.
	 * @param __y The Y coordinate in megatiles.
	 * @return The megatile at the given coordinates.
	 * @throws IndexOutOfBoundsException If the megatile position is not
	 * within bounds.
	 * @since 2017/02/11
	 */
	public MegaTile megaTile(int __x, int __y)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error BE03 Megatile not in level range.}
		int megaw = this.megaw,
			megah = this.megah;
		if (__x < 0 || __y < 0 || __x >= megaw || __y >= megah)
			throw new IndexOutOfBoundsException("BE03");
		
		// Get
		return this._tiles[(__y * megaw) + __x];
	}
	
	/**
	 * Converts a pixel coordinate to a mega tile.
	 *
	 * @param __c The coordinate to convert.
	 * @return The converted coordinate.
	 * @since 2017/02/13
	 */
	public static int pixelCoordToMegaTile(int __c)
	{
		return __c / MegaTile.PIXEL_SIZE;
	}
	
	/**
	 * Converts a pixel coordinate to a tile.
	 *
	 * @param __c The coordinate to convert.
	 * @return The converted coordinate.
	 * @since 2017/02/13
	 */
	public static int pixelCoordToTile(int __c)
	{
		return __c / MegaTile.TILE_PIXEL_SIZE;
	}
	
	/**
	 * Converts a pixel coordinate to a sub tile in a megatile.
	 *
	 * @param __c The coordinate to convert.
	 * @return The converted coordinate.
	 * @since 2017/02/13
	 */
	public static int pixelCoordToSubTile(int __c)
	{
		return (__c / Tile.PIXEL_SIZE) % MegaTile.TILES_PER_MEGA_TILE;
	}
	
	/**
	 * Returns the height of the map in megatiles.
	 *
	 * @return The map height in megatiles.
	 * @since 2017/02/12
	 */
	public int megaTileHeight()
	{
		return this.megah;
	}
	
	/**
	 * Returns the width of the map in megatiles.
	 *
	 * @return The map width in megatiles.
	 * @since 2017/02/12
	 */
	public int megaTileWidth()
	{
		return this.megaw;
	}
	
	/**
	 * Returns the height of the level in pixels.
	 *
	 * @return The height of the level in pixels.
	 * @since 2017/02/10
	 */
	public int pixelHeight()
	{
		return this.pixelh;
	}
	
	/**
	 * Returns the width of the level in pixels.
	 *
	 * @return The width of the level in pixels.
	 * @since 2017/02/10
	 */
	public int pixelWidth()
	{
		return this.pixelw;
	}
	
	/**
	 * Returns the terrain type at the given pixel
	 *
	 * @param __x The pixel X position.
	 * @param __y The pixel Y position.
	 * @return The terrain type for the given tile.
	 * @since 2017/02/13
	 */
	public TerrainType pixelTerrain(int __x, int __y)
	{
		return megaTile(pixelCoordToMegaTile(__x), pixelCoordToMegaTile(__y)).
			subTileTerrain(pixelCoordToSubTile(__x), pixelCoordToSubTile(__y));
	}
	
	/**
	 * This checks whether the given tile at the specified pixel is revealed
	 * for the given player.
	 *
	 * @param __p The player to check if they can see the given tile.
	 * @param __x The tile X position.
	 * @param __y The tile Y position.
	 * @return {@code true} if it is revealed.
	 * @since 2017/02/13
	 */
	public boolean pixelRevealed(Player __p, int __x, int __y)
	{
		return megaTile(pixelCoordToMegaTile(__x), pixelCoordToMegaTile(__y)).
			subTileRevealed(__p, pixelCoordToSubTile(__x),
			pixelCoordToSubTile(__y));
	}
	
	/**
	 * Initializes the map tiles.
	 *
	 * @param __tw The tile width of the map.
	 * @param __th The tile height of the map.
	 * @return The initialized mega tiles.
	 * @since 2017/02/14
	 */
	private MegaTile[] __initTiles(int __tw, int __th)
	{
		int megaw = __tw / MegaTile.TILES_PER_MEGA_TILE;
		int megah = __th / MegaTile.TILES_PER_MEGA_TILE;
		
		// Initialize
		MegaTile[] tiles = new MegaTile[megaw * megah];
		for (int i = 0, n = tiles.length; i < n; i++)
			tiles[i] = new MegaTile(this, i % megaw, i / megaw);
		
		return tiles;
	}
	
	/**
	 * Runs the level logic.
	 *
	 * @param __frame The current frame.
	 * @since 2017/02/14
	 */
	void __run(int __frame)
	{
		// Run the megatile loop
		MegaTile[] tiles = this._tiles;
		for (int i = 0, n = tiles.length; i < n; i++)
			tiles[i].__run(__frame);
	}
}

