// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.game.chunk;

import net.multiphasicapps.squirrelscavenger.game.block.BlockType;

/**
 * This represents a single chunk which points to the data for a given chunk.
 *
 * Note that the data that exists for a chunk is not defined here, it is
 * defined in another class.
 *
 * @since 2016/10/07
 */
public class Chunk
{
	/** The chunk manager. */
	protected final ChunkManager manager;
	
	/** The index of this chunk. */
	protected final ChunkIndex index;
	
	/** Block X position. */
	protected final int baseblockx;
	
	/** Block Y position. */
	protected final int baseblocky;
	
	/** Block Z position. */
	protected final int baseblockz;
	
	/**
	 * Initializes the chunk.
	 *
	 * @param __cm The owning chunk manager.
	 * @param __dx The index of this chunk.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/09
	 */
	public Chunk(ChunkManager __cm, ChunkIndex __dx)
		throws NullPointerException
	{
		// Check
		if (__cm == null || __dx == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manager = __cm;
		this.index = __dx;
		
		// Calculate base block position
		int ri = __dx.index();
		this.baseblockx = (ri >>> PositionFunctions.WORLD_CHUNK_X_SHIFT) &
			PositionFunctions.WORLD_CHUNK_BREADTH_MASK;
		this.baseblocky = (ri >>> PositionFunctions.WORLD_CHUNK_Y_SHIFT) &
			PositionFunctions.WORLD_CHUNK_BREADTH_MASK;
		this.baseblockz = ri & PositionFunctions.WORLD_CHUNK_HEIGHT_MASK;
	}
	
	/**
	 * Returns the block type at the given index.
	 *
	 * @param __dx The index of the block.
	 * @return The block type for this block.
	 * @since 2016/10/19
	 */
	public BlockType blockType(int __dx)
	{
		if (this.baseblockz > 128)
			return BlockType.AIR;
		return BlockType.DIRT;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/09
	 */
	@Override
	public int hashCode()
	{
		return this.index.hashCode();
	}
}

