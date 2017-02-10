// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This stores the mega tiles within the level along with storing level related
 * data structures.
 *
 * @since 2017/02/09
 */
public class Level
{
	/** The owning game. */
	protected final Game game;
	
	/** The mega tile array. */
	protected final MegaTile[] tiles;
	
	/** The width of the level in pixels. */
	protected final int pixelw;
	
	/** The height of the level in pixels. */
	protected final int pixelh;
	
	/**
	 * Initializes the level with the given initial settings.
	 *
	 * @param __g The game which owns this.
	 * @param __is The initial settings to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	public Level(Game __g, InitialSettings __is)
		throws NullPointerException
	{
		// Check
		if (__g == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __g;
		
		// Get the map size
		int mw = __is.mapWidth(),
			mh = __is.mapHeight(),
			mtw = mw / MegaTile.TILES_PER_MEGA_TILE,
			mth = mh / MegaTile.TILES_PER_MEGA_TILE;
		MegaTile[] tiles = new MegaTile[mtw * mth];
		this.tiles = tiles;
		
		// Set sizes
		this.pixelw = mw * MegaTile.TILE_PIXEL_SIZE;
		this.pixelh = mh * MegaTile.TILE_PIXEL_SIZE;
	}
	
	/**
	 * Initializes the level from a previously serialized stream such as one
	 * that was made for a replay or saved game.
	 *
	 * @param __g The game which owns this.
	 * @param __is The stream to deserialize from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	public Level(Game __g, DataInputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__g == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __g;
		
		throw new Error("TODO");
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
}

