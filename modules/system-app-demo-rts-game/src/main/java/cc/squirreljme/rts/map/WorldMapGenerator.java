// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.map;

/**
 * This is a generator for the world map, this completely can be tuned to
 * generate any map.
 *
 * @since 2026/06/11
 */
public class WorldMapGenerator
{
	/** The chunks to be on the map. */
	private volatile Chunks _chunks;
	
	/**
	 * Initializes the base world map generator.
	 *
	 * @since 2026/06/11
	 */
	public WorldMapGenerator()
	{
	}
	
	/**
	 * Produces the finalized and finished world map.
	 *
	 * @return The finalized world map.
	 * @throws IllegalStateException If the map is not yet suitable for use.
	 * @since 2026/06/11
	 */
	public WorldMap finish()
		throws IllegalStateException
	{
		// Need chunks!
		Chunks chunks = this._chunks;
		if (chunks == null)
			throw new IllegalStateException("No chunks!");
		
		// Setup final map now!
		return new WorldMap(chunks);
	}
	
	/**
	 * Initializes a blank chunk state with the given tile size.
	 *
	 * @param __tw The tile width of the map.
	 * @param __th The tile height of the map.
	 * @throws IllegalArgumentException If the width and/or height are zero
	 * or negative; if any dimension is not a multiple of the chunk size.
	 * @throws IllegalStateException If chunks have already been initialized.
	 * @since 2026/06/11
	 */
	public void size(int __tw, int __th)
		throws IllegalArgumentException, IllegalStateException
	{
		if (__tw <= 0 || __th <= 0 ||
			(__tw % Chunk.CHUNK_TO_TILE_DIM) != 0 ||
			(__th % Chunk.CHUNK_TO_TILE_DIM) != 0)
			throw new IllegalArgumentException("ILLV");
		
		synchronized (this)
		{
			// Can only set chunks once!
			if (this._chunks != null)
				throw new IllegalStateException("Chunks exist already!");
			
			// Setup new blank chunks
			this._chunks = new Chunks(__tw, __th);
		}
	}
}
