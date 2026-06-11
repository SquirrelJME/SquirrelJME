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
 * A single map chunk.
 *
 * @see Chunks
 * @since 2026/06/10
 */
public class Chunk
	extends ChunkAddressable
{
	/** The number of pixels per tile. */
	public static final short TILE_TO_PX =
		32;
	
	/** The number of pixels per chunk. */
	public static final short CHUNK_TO_PX =
		Chunk.TILE_TO_PX * Chunk.CHUNK_TO_TILE_DIM;
	
	/** The number of tiles in a chunk, for a single dimension. */
	public static final short CHUNK_TO_TILE_DIM =
		8;
	
	/** The number of tiles in a chunk, for the area of the tile. */
	public static final short CHUNK_TO_TILE_AREA =
		Chunk.CHUNK_TO_TILE_DIM * Chunk.CHUNK_TO_TILE_DIM;
	
	/** The index of this chunk. */
	protected final int index;
	
	/** The map tile width. */
	private final int mapTileW;
	
	/** The map tile X coordinate. */
	private final int mapTileX;
	
	/** The map tile Y coordinate. */
	private final int mapTileY;
	
	/**
	 * Initializes the chunk storage.
	 *
	 * @param __index The index of this chunk.
	 * @param __mtw The map tile width, this is used to determine the
	 * coordinates of the chunk.
	 * @throws IllegalArgumentException If any parameter is not valid.
	 * @since 2026/06/11
	 */
	public Chunk(int __index, int __mtw)
		throws IllegalArgumentException
	{
		if (__index < 0)
			throw new IllegalArgumentException("ILLV");
		
		// These are important
		this.index = __index;
		this.mapTileW = __mtw;
		
		// Determine the start position of this chunk
		this.mapTileX = (__index * Chunk.CHUNK_TO_TILE_DIM) % __mtw;
		this.mapTileY = ((__index * Chunk.CHUNK_TO_TILE_DIM) / __mtw) *
			Chunk.CHUNK_TO_TILE_DIM;
		
		// Debug
		Debugging.debugNote("Chunk %d (mtw %d) -> (%d, %d)",
			__index, __mtw, this.mapTileX, this.mapTileY);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public final long getTx(ChunkLayer __layer, int __tx, int __ty)
		throws IndexOutOfBoundsException, NullPointerException
	{
		throw Debugging.todo();
	}
}
