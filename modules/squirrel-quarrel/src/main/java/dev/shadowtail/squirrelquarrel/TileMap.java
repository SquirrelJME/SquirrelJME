// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Image;

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
	
	/** The tile background mask. */
	public static final int TILE_BACKGOUND_MASK =
		0b0000_0011;
	
	/** Background tile images. */
	private static final Image[] _CACHE_BACKGROUND =
		new Image[4];
	
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
		
		/* {@squirreljme.error BE0r Negative or zero players, there must be
		at least one player. (The player count)} */
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
		
		for (int i = 0; i < tiledens; i++)
			tiles[i] = (byte)__rand.nextInt();
	}
	
	/**
	 * Looks into the cache and returns the specified image.
	 *
	 * @param __cache The cache to look in.
	 * @param __dx The index to cache.
	 * @param __prefix The resource prefix.
	 * @return The resulting image.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/02
	 */
	public static Image cacheImage(Image[] __cache, int __dx, String __prefix)
		throws NullPointerException
	{
		if (__cache == null || __prefix == null)
			throw new NullPointerException("NARG");
		
		// If the cache has the image, use it
		Image rv = __cache[__dx];
		if (rv != null)
			return rv;
		
		// Otherwise load it!
		try (InputStream in = TileMap.class.getResourceAsStream(
			__prefix + __dx + ".xpm"))
		{
			rv = Image.createImage(in);
		}
		
		/* {@squirreljme.error BE0t Could not cache the image. (The index;
		The prefix)} */
		catch (IOException e)
		{
			throw new RuntimeException("BE0t " + __dx + " " + __prefix, e);
		}
		
		// Store into the cache then return
		__cache[__dx] = rv;
		return rv;
	}
	
	/**
	 * Gets the image used for the background.
	 *
	 * @param __b The tile data.
	 * @return The image to use.
	 * @since 2019/07/02
	 */
	public static Image imageBackground(byte __b)
	{
		// Read cached image
		return TileMap.cacheImage(
			TileMap._CACHE_BACKGROUND, __b & TileMap.TILE_BACKGOUND_MASK,
			"tile");
	}
}

