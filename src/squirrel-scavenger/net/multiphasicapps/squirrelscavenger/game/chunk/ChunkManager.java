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
import net.multiphasicapps.squirrelscavenger.game.Game;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class is used to manage chunks which may be cached, loaded, and stored
 * by the game.
 *
 * Chunk IDs are stored as a single integer value with their position encoded
 * in the integer data. This means that there is a limit of 1024x1024x1024
 * chunks.
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
	
	/** The directory containing the chunk cache. */
	protected final Path root;
	
	/** The biome generator. */
	protected final BiomeGenerator biome;
	
	/** This is a queue to detect which chunks were collected. */
	final ReferenceQueue<Chunk> _queue =
		new ReferenceQueue<>();
	
	/** This is the chunk index mapping. */
	final Map<Integer, ChunkIndex> _chunks =
		new SortedTreeMap<>();
	
	/** Mapping of references to chunk indexes. */
	final Map<Reference<Chunk>, Integer> _reftoindex =
		new HashMap<>();
	
	/**
	 * Initializes the chunk manager.
	 *
	 * @param __g The game that owns this.
	 * @param __seed The seed used for map generation.
	 * @param __root The root directory where chunk data is stored.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/06
	 */
	public ChunkManager(Game __g, long __seed, Path __root)
		throws NullPointerException
	{
		// Check
		if (__g == null || __root == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __g;
		this.seed = __seed;
		this.root = __root;
		
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
		// Get the chunk index mappings
		Integer i = Integer.valueOf(__i);
		Map<Integer, ChunkIndex> chunks = this._chunks;
		
		// See if a chunk index needs to be created
		ChunkIndex dx = chunks.get(i);
		if (dx == null)
			chunks.put(i, (dx = new ChunkIndex(this, __i)));
		
		// Get the chunk reference as needed
		return dx.chunk();
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

