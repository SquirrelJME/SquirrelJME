// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.map;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Base class for any addressable chunk.
 *
 * @since 2026/06/10
 */
public abstract class ChunkAddressable
{
	/**
	 * Returns the tile data at the given tile.
	 *
	 * @param __layer The layer to read data from.
	 * @param __tx The X coordinate.
	 * @param __ty The Y coordinate.
	 * @return The tile data at the given tile.
	 * @throws IndexOutOfBoundsException If the tile is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/10
	 */
	public abstract long getTx(ChunkLayer __layer, int __tx, int __ty)
		throws IndexOutOfBoundsException, NullPointerException;
	
	/**
	 * Returns the tile data at the given pixel.
	 *
	 * @param __layer The layer to read data from.
	 * @param __px The X coordinate.
	 * @param __py The Y coordinate.
	 * @return The tile data at the given pixel.
	 * @throws IndexOutOfBoundsException If the tile is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/10
	 */
	public final long getPx(ChunkLayer __layer, int __px, int __py)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__layer == null)
			throw new NullPointerException("NARG");
		
		if (__px < 0 || __py < 0 ||
			__px >= this.pixelWidth() || __py >= this.pixelHeight())
			throw new IndexOutOfBoundsException("IOOB");
		
		// Map to an actual tile
		return this.getTx(__layer, 
			__px / Chunk.TILE_TO_PX, __py / Chunk.TILE_TO_PX);
	}
	
	/**
	 * Returns the pixel height of the map.
	 *
	 * @return The pixel height of the map.
	 * @since 2026/06/10
	 */
	public final int pixelHeight()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the pixel width of the map.
	 *
	 * @return The pixel width of the map.
	 * @since 2026/06/10
	 */
	public final int pixelWidth()
	{
		throw Debugging.todo();
	}
}
