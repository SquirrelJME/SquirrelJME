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

import cc.squirreljme.runtime.lcdui.image.XPMReader;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.squirrelquarrel.world.World;
import net.multiphasicapps.squirrelquarrel.world.MegaTile;
import net.multiphasicapps.squirrelquarrel.world.TerrainType;
import net.multiphasicapps.squirrelquarrel.world.Tile;

/**
 * This is used to cache mega tiles as single large images since drawing a
 * large image is faster than drawing many smaller images.
 *
 * @since 2017/02/11
 */
public class MegaTileCacher
{
	/** The single image reader instance. */
	private static final XPMReader _XPM_READER =
		new XPMReader();
	
	/** The cache of terrain tiles. */
	private static final Map<TerrainType, Reference<Image>> _TILE_CACHE =
		new HashMap<>();
	
	/** The level to cache for. */
	protected final World world;
	
	/** The cache of tiles. */
	protected final Map<MegaTile, Reference<Image>> cache =
		new HashMap<>();
	
	/**
	 * Initializes the mega tile cacher.
	 *
	 * @param __l The level to cache for.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	public MegaTileCacher(World __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.world = __l;
	}
	
	/**
	 * Caches the image for the given megatile.
	 *
	 * @param __x The X coordinate in megatiles.
	 * @param __y The Y coordinate in megatiles.
	 * @return The cached image for the given mega tile.
	 * @since 2017/02/11
	 */
	public Image cacheMegaTile(int __x, int __y)
	{
		return cacheMegaTile(this.world.megaTile(__x, __y));
	}
	
	/**
	 * Caches the image for the given megatile.
	 *
	 * @param __mt The mega tile to cache.
	 * @return The cached image for the given mega tile.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/11
	 */
	public Image cacheMegaTile(MegaTile __mt)
		throws NullPointerException
	{
		// Check
		if (__mt == null)
			throw new NullPointerException("NARG");
		
		Map<MegaTile, Reference<Image>> cache = this.cache;
		Reference<Image> ref = cache.get(__mt);
		Image rv = null;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Create image to draw on
			rv = Image.createImage(MegaTile.PIXEL_SIZE,
				MegaTile.PIXEL_SIZE);
			
			// Draw on it
			Graphics g = rv.getGraphics();
			
			// Do not need to check cache for the same tile
			TerrainType oldtype = null;
			Image tilepic = null;
			
			// Go through the megatile to draw
			for (int ty = 0, dy = 0; ty < MegaTile.TILES_PER_MEGA_TILE;
				ty++, dy += Tile.PIXEL_SIZE)
				for (int tx = 0, dx = 0; tx < MegaTile.TILES_PER_MEGA_TILE;
					tx++, dx += Tile.PIXEL_SIZE)
				{
					// If the terrain type is the same then use the existing
					// image of it rather than checking the cache every
					// single time
					TerrainType type = __mt.subTileTerrain(tx, ty);
					if (type != oldtype)
					{
						oldtype = type;
						tilepic = __cacheTile(type);
					}
					
					// Draw it
					g.drawImage(tilepic, dx, dy, 0);
				}
			
			// Cache
			this.cache.put(__mt, new WeakReference<>(rv));
		}
		
		return rv;
	}
	
	/**
	 * Caches the specified tile.
	 *
	 * @param __t The terrain to get the image for.
	 * @return The image for the given terrain.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	private static final Image __cacheTile(TerrainType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Get ref
		Reference<Image> ref = _TILE_CACHE.get(__t);
		Image rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			try
			{
				_TILE_CACHE.put(__t, new WeakReference<>(
					(rv = _XPM_READER.readImage(__t.imageStream()))));
			}
			
			// {@squirreljme.error BE0i Failed to read the image data for the
			// specified file. (The terrain type)}
			catch (IOException e)
			{
				throw new RuntimeException(String.format("BE0i %s", __t), e);
			}
		
		return rv;
	}
}

