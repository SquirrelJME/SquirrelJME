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
 * This represents a single mega tile which represents a small region in the
 * level.
 *
 * @since 2017/02/09
 */
public class MegaTile
{
	/** The number of tiles per mega tile. */
	public static final int TILES_PER_MEGA_TILE =
		8;
	
	/** The number of tiles in mega tiles. */
	public static final int TILES_IN_MEGA_TILE =
		TILES_PER_MEGA_TILE * TILES_PER_MEGA_TILE;
	
	/** The size of tiles in pixels. */
	public static final int TILE_PIXEL_SIZE =
		32;
	
	/** Terrain information. */
	protected byte[] terrain =
		new byte[TILES_IN_MEGA_TILE];
	
	/** Fog of war revealed information. */
	protected byte[] revealedfog =
		new byte[TILES_IN_MEGA_TILE];
	
	/**
	 * Initializes the megatile from a previously serialized replay or save
	 * game.
	 *
	 * @param __is The stream to read from.
	 * @since 2017/02/10
	 */
	public MegaTile(DataInputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

