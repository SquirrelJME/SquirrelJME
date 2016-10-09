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

