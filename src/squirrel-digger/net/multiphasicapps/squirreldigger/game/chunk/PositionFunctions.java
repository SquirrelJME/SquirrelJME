// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreldigger.game.chunk;

/**
 * This is a class which just contains functions that work on positions.
 *
 * @since 2016/10/09
 */
public class PositionFunctions
{
	/** The number of bits to use for the breadth. */
	public static final int WORLD_CHUNK_BREADTH_BITS =
		13;
	
	/** The number of bits to use for the height. */
	public static final int WORLD_CHUNK_HEIGHT_BITS =
		6;
	
	/** The number of chunks for the breadth of the world. */
	public static final int WORLD_CHUNK_BREADTH =
		1 << WORLD_CHUNK_BREADTH_BITS;
	
	/** The number of chunks in the height of the world. */
	public static final int WORLD_CHUNK_HEIGHT =
		1 << WORLD_CHUNK_HEIGHT_BITS;
	
	/** The number of bits to use for blocks. */
	public static final int BLOCK_BITS =
		3;
	
	/** The scale of a block. */
	public static final int BLOCK_SCALE =
		1 << BLOCK_BITS;
	
	/** The number of bits to use for the sub-position. */
	public static final int SUB_BITS =
		31 - WORLD_CHUNK_BREADTH_BITS - BLOCK_BITS;
	
	/** The breadth of the world in blocks. */
	public static final int WORLD_BLOCK_BREADTH =
		WORLD_CHUNK_BREADTH * BLOCK_SCALE;
	
	/** The height of the world in blocks. */
	public static final int WORLD_BLOCK_HEIGHT =
		WORLD_CHUNK_HEIGHT * BLOCK_SCALE;
}

