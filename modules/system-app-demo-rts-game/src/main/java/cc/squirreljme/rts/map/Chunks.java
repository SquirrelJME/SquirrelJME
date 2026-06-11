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
import java.lang.ref.Reference;

/**
 * Manages multiple {@link Chunks} to create a uniform and optimal map.
 * 
 * Chunks within are stored swizzled.
 *
 * @see Chunk
 * @since 2026/06/10
 */
public class Chunks
	extends ChunkAddressable
	implements WorldMapBindable<Chunks>
{
	/** The world map this is bound to. */
	private volatile Reference<WorldMap> _worldMap;
	
	/** The linear set of chunks within the map. */
	private final Chunk[] _chunks;
	
	/** The tile width of the map. */
	protected final int tileW;
	
	/** The tile height of the map. */
	protected final int tileH;
	
	/** The area of tiles on the map. */
	protected final int tileA;
	
	/**
	 * Initializes chunks to store the given map size.
	 *
	 * @param __tw The tile width of the map.
	 * @param __th The tile height of the map.
	 * @throws IllegalArgumentException If the width and/or height are zero
	 * or negative; if any dimension is not a multiple of the chunk size.
	 * @since 2026/06/11
	 */
	public Chunks(int __tw, int __th)
		throws IllegalArgumentException
	{
		if (__tw <= 0 || __th <= 0 ||
			(__tw % Chunk.CHUNK_TO_TILE_DIM) != 0 ||
			(__th % Chunk.CHUNK_TO_TILE_DIM) != 0)
			throw new IllegalArgumentException("ILLV");
		
		// Set base parameters
		this.tileW = __tw;
		this.tileH = __th;
		
		// Determine the tile area
		int tileA = __tw * __th;
		this.tileA = tileA;
		
		// Setup all individual chunks
		Chunk[] chunks = new Chunk[tileA];
		for (int i = 0; i < tileA / Chunk.CHUNK_TO_TILE_AREA; i++)
			chunks[i] = new Chunk(i, __tw);
		
		// Store all chunks
		this._chunks = chunks;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/11
	 */
	@Override
	public Chunks bind(Reference<WorldMap> __map)
		throws IllegalStateException, NullPointerException
	{
		if (__map == null)
			throw new NullPointerException("NARG");
		
		// The reference needs to actually be valid!
		WorldMap map = __map.get();
		if (map == null)
			throw new IllegalStateException("GCGC");
		
		synchronized (this)
		{
			// Cannot be relinked!
			Reference<WorldMap> ref = this._worldMap;
			if (ref != null)
				throw new IllegalStateException("LNKD");
			
			this._worldMap = __map;
		}
		
		// Always return self!
		return this;
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
