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
		Chunk.TILE_TO_PX * Chunk.CHUNK_TO_TILE;
	
	/** The number of tiles in a chunk. */
	public static final short CHUNK_TO_TILE =
		4;
	
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
