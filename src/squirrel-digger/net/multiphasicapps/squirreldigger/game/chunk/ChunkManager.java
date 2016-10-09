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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreldigger.game.Game;

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
	/** The number of bits that are used in a chunk index position. */
	public static final int CHUNK_BITS =
		10;
	
	/** The number of bits that are used in the chunk mask. */
	public static final int CHUNK_MASK =
		0b1111111111;
	
	/** The full index max. */
	public static final int INDEX_MASK =
		0b1111111111_1111111111_1111111111;
	
	/** The shift for X values. */
	public static final int X_SHIFT =
		0;
	
	/** The shift for Y values. */
	public static final int Y_SHIFT =
		CHUNK_BITS;
	
	/** The shift for Z values. */
	public static final int Z_SHIFT =
		CHUNK_BITS * 2;
	
	/** The value mask for the entity sub-block position. */
	public static final int ENTITY_POS_SUB_VALUE_MASK =
		0b1111_1111;
	
	/** The shift to get the block ID for the entity position. */
	public static final int ENTITY_POS_BLOCK_SHIFT =
		8;
		
	/** The value mask for the entity block position. */
	public static final int ENTITY_POS_BLOCK_VALUE_MASK =
		0b111;
	
	/** The shifted mask for the block position. */
	public static final int ENTITY_POS_BLOCK_SHIFT_MASK =
		ENTITY_POS_BLOCK_VALUE_MASK << ENTITY_POS_BLOCK_SHIFT;
	
	/** The shift to get the chunk ID for the entity position. */
	public static final int ENTITY_POS_CHUNK_SHIFT =
		11;
	
	/** This is the value mask for the chunk position. */
	public static final int ENTITY_POS_CHUNK_VALUE_MASK =
		CHUNK_MASK;
	
	/** This is the masked chunk coordinate position. */
	public static final int ENTITY_POS_CHUNK_SHIFT_MASK =
		ENTITY_POS_CHUNK_VALUE_MASK << ENTITY_POS_CHUNK_SHIFT;
	
	/** The game that owns this chunk manager. */
	protected final Game game;
	
	/** The seed used to generate map data. */
	protected final long seed;
	
	/** The directory containing the chunk cache. */
	protected final Path root;
	
	/** This is a queue to detect which chunks were collected. */
	private final ReferenceQueue<Chunk> _queue =
		new ReferenceQueue<>();
	
	/** This is the mapping of chunks to chunk data. */
	private final Map<Reference<Chunk>, ChunkData> _chunktodata =
		new HashMap<>();
	
	/** This is the mapping of chunk indices to chunk references. */
	private final Map<Integer, Reference<Chunk>> _indextochunk =
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
		// Clip to the mask to force it to be valid
		__i &= INDEX_MASK;
		
		// Get mappings
		Map<Reference<Chunk>, ChunkData> chunktodata = this._chunktodata;
		Map<Integer, Reference<Chunk>> indextochunk = this._indextochunk;
		
		// If the chunk is loaded in memory, use it
		Integer i = Integer.valueOf(__i);
		Reference<Chunk> ref = indextochunk.get(i);
		Chunk rv;
		if (ref != null && null != (rv = ref.get()))
			return rv;
		
		throw new Error("TODO");
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
		return chunkByChunkIndex(chunkPositionToIndex(__x, __y, __z));
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
		return chunkByChunkIndex(entityPositionToIndex(__x, __y, __z));
	}
	
	/**
	 * Returns the chunk index for the block at the specified position.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __z The Z coordinate.
	 * @return The index for the given block position.
	 * @since 2016/10/09
	 */
	public static int blockPositionToIndex(int __x, int __y, int __z)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Caps the Z coordinate so that it does not overflow, that is getting
	 * the chunk just above the highest point will not return the lowest
	 * chunk.
	 *
	 * @param __z The Z coordinate to cap.
	 * @return The capped Z coordinate.
	 * @since 2016/10/09
	 */
	public static int capChunkZ(int __z)
	{
		if (__z < 0)
			return 0;
		else if (__z > CHUNK_MASK)
			return CHUNK_MASK;
		return __z & CHUNK_MASK;
	}
	
	/**
	 * Translates a chunk position to a chunk index.
	 *
	 * @param __x The x position.
	 * @param __y The y position.
	 * @param __z The z position.
	 * @return The index for the given chunk.
	 * @since 2016/10/07
	 */
	public static int chunkPositionToIndex(int __x, int __y, int __z)
	{
		return ((__x & CHUNK_MASK) << X_SHIFT) |
			((__y & CHUNK_MASK) << Y_SHIFT) |
			(capChunkZ(__z) << Z_SHIFT);
	}
	
	/**
	 * Returns the chunk index to use for the specified entity position.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __z The Z coordinate.
	 * @return The chunk index for the given entity position.
	 * @since 2016/10/09
	 */
	public static int entityPositionToIndex(int __x, int __y, int __z)
	{
		return chunkPositionToIndex(
			(__x >>> ENTITY_POS_CHUNK_SHIFT) & ENTITY_POS_CHUNK_VALUE_MASK,
			(__y >>> ENTITY_POS_CHUNK_SHIFT) & ENTITY_POS_CHUNK_VALUE_MASK,
			(__z >> ENTITY_POS_CHUNK_SHIFT));
	}
}

