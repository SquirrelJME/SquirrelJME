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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * This represents a chunk index which is used to identify the ID for a chunk
 * along with having a pointer to its data (if it is loaded).
 *
 * @since 2016/10/09
 */
public final class ChunkIndex
	implements Comparable<ChunkIndex>
{
	/** The owning chunk manager. */
	protected final ChunkManager manager;
	
	/** The chunk index ID. */
	protected final int index;
	
	/** The chunk pointer reference (and its data). */
	private volatile Reference<Chunk> _ref;
	
	/**
	 * Initializes the chunk index.
	 *
	 * @param __cm The chunk manager owning this.
	 * @param __dx The chunk index value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/09
	 */
	public ChunkIndex(ChunkManager __cm, int __dx)
		throws NullPointerException
	{
		// Check
		if (__cm == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manager = __cm;
		this.index = __dx;
	}
	
	/**
	 * Returns the referenced chunk for this index.
	 *
	 * @return The chunk for this index.
	 * @since 2016/10/09
	 */
	public Chunk chunk()
	{
		// If a chunk reference does not exist create it
		ChunkManager manager = this.manager;
		Reference<Chunk> ref = this._ref;
		Chunk rv;
		if (ref == null || null == (rv = ref.get()))
		{
			// Need the queue because of cache purposes
			ReferenceQueue<Chunk> queue = manager._queue;
			
			
			throw new Error("TODO");
		}
		
		// Return it
		return rv;
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
}

