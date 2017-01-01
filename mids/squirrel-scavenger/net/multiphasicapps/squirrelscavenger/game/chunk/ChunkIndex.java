// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.game.chunk;

/**
 * This represents a chunk index which is used to identify the ID for a chunk.
 *
 * @since 2016/10/09
 */
public final class ChunkIndex
	implements Comparable<ChunkIndex>
{
	/** The chunk index ID. */
	protected final int index;
	
	/**
	 * Initializes the chunk index.
	 *
	 * @param __dx The chunk index value.
	 * @since 2016/10/09
	 */
	public ChunkIndex(int __dx)
	{
		// Set
		this.index = __dx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/09
	 */
	@Override
	public int compareTo(ChunkIndex __ci)
	{
		int a = this.index, b = __ci.index;
		if (a < b)
			return -1;
		else if (a > b)
			return 1;
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/09
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ChunkIndex))
			return false;
		
		return this.index == ((ChunkIndex)__o).index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/09
	 */
	@Override
	public int hashCode()
	{
		return this.index;
	}
	
	/**
	 * Returns the chunk index.
	 *
	 * @return The chunk index.
	 * @since 2016/10/09
	 */
	public int index()
	{
		return this.index;
	}
}

