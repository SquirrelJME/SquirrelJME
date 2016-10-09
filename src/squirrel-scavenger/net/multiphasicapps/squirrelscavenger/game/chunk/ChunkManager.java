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
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import net.multiphasicapps.squirrelscavenger.game.Game;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class is used to manage chunks which may be cached, loaded, and stored
 * by the game.
 *
 * Chunk IDs are stored as a single integer value with their position encoded
 * in the integer data.
 *
 * Players and entities use the {@link Chunk} class to access chunk data and to
 * potentially modify those chunks.
 *
 * Internally a semi-weak mapping of chunk data is mapped to the chunk
 * references. If an iteration detects that a chunk is not loaded then it will
 * be flushed to the disk and removed. If a chunk is requested and it is
 * cached on the disk, it will be read.
 *
 * @since 2016/10/06
 */
public class ChunkManager
{
	/** The game that owns this chunk manager. */
	protected final Game game;
	
	/** The seed used to generate map data. */
	protected final long seed;
	
	/** The biome generator. */
	protected final BiomeGenerator biome;
	
	/** Chunks loaded in memory. */
	final Map<ChunkIndex, Reference<Chunk>> _chunks =
		new WeakHashMap<>();
	
	/**
	 * Initializes the chunk manager.
	 *
	 * @param __g The game that owns this.
	 * @param __seed The seed used for map generation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/06
	 */
	public ChunkManager(Game __g, long __seed)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __g;
		this.seed = __seed;
		
		// Setup generator helpers
		this.biome = new BiomeGenerator(__seed);
	}
	
	/**
	 * Returns the chunk which uses the specified index.
	 *
	 * @param __i The index to use for the chunk.
	 * @return The the chunk for the given index.
	 * @since 2016/10/07
	 */
	public Chunk chunkByChunkIndex(int __i)
	{
		// Make chunk index
		ChunkIndex key = new ChunkIndex(__i);
		
		// See if a reference to a chunk stil exists
		Map<ChunkIndex, Reference<Chunk>> chunks = this._chunks;
		Reference<Chunk> ref = chunks.get(key);
		Chunk rv;
		
		// Does not exist, must cache it
		if (ref == null || null == (rv = ref.get()))
			chunks.put(key, new WeakReference<>((rv = new Chunk(this, key))));
		
		// Return it
		return rv;
	}
	
	/**
	 * Returns the chunk for the specified chunk position.
	 *
	 * @param __x The x position.
	 * @param __y The y position.
	 * @param __z The z position.
	 * @return The chunk for this given position.
	 * @since 2016/10/07
	 */
	public Chunk chunkByChunkPosition(int __x, int __y, int __z)
	{
		return chunkByChunkIndex(
			PositionFunctions.chunkPositionToChunkIndex(__x, __y, __z));
	}
	
	/**
	 * Returns the chunk for the specified entity position.
	 *
	 * @param __x The x position.
	 * @param __y The y position.
	 * @param __z The z position.
	 * @return The chunk for this given position.
	 * @since 2016/10/09
	 */
	public Chunk chunkByEntityPosition(int __x, int __y, int __z)
	{
		return chunkByChunkIndex(
			PositionFunctions.entityPositionToChunkIndex(__x, __y, __z));
	}
}

