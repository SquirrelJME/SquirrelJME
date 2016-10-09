// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.game.entity;

import net.multiphasicapps.squirrelscavenger.game.block.BlockType;
import net.multiphasicapps.squirrelscavenger.game.chunk.Chunk;
import net.multiphasicapps.squirrelscavenger.game.chunk.ChunkManager;
import net.multiphasicapps.squirrelscavenger.game.chunk.PositionFunctions;
import net.multiphasicapps.squirrelscavenger.game.Game;

/**
 * This class represents an entity that exists within the game.
 *
 * @since 2016/10/07
 */
public class Entity
{
	/** The game this entity is within. */
	protected final Game game;
	
	/** Entity coordinates. */
	private final int[] _pos =
		new int[3];
	
	/** The type of entity that this is. */
	private volatile EntityType _type;
	
	/** The chunk the entity is inside. */
	private volatile Chunk _inchunk;
	
	/**
	 * Initializes the entity.
	 *
	 * @param __g The game the entity is in.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/07
	 */
	public Entity(Game __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __g;
	}
	
	/**
	 * Initializes the entity so that it is the given type as if it were
	 * freshly spawned.
	 *
	 * @param __t The type of entity to initialize to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/07
	 */
	public void initializeTo(EntityType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set the type
		this._type = __t;
	}
	
	/**
	 * Returns {@code true} if the entity is spawned.
	 *
	 * @return The spawn state of the entity.
	 * @since 2016/10/09
	 */
	public boolean isSpawned()
	{
		// Only if the entity is in a chunk it is spawned
		return this._inchunk != null;
	}
	
	/**
	 * This attempts to spawn the given entity at the given coordinates on
	 * the surface of the world.
	 *
	 * A check will be made to make sure that the entity would survive in the
	 * given situation (not placed on top of lava, water if it cannot breathe
	 * water, or air if it cannot breathe air).
	 *
	 * @param __x The X coordinate to spawn at (entity position).
	 * @param __y The Y coordinate to spawn at (entity position).
	 * @return {@code true} if the entity was spawned at the specified
	 * location, otherwise false.
	 * @throws IllegalStateException If the entity type was not set.
	 * @since 2016/10/09
	 */
	public boolean spawnOnSurface(int __x, int __y)
		throws IllegalStateException
	{
		// {@squirreljme.error BA05 Cannot spawn on surface because the
		// entity type was not set.}
		EntityType type = this._type;
		if (type == null)
			throw new IllegalStateException("BA05");
		
		// Get the chunk managed since chunks need to be known
		ChunkManager cm = this.game.chunkManager();
		
		// Start from the top of the sky and go down until ground is reached
		boolean hit = false;
		Chunk last = null;
		int lastz = PositionFunctions.WORLD_BLOCK_HEIGHT - 1,
			lastez = lastz * PositionFunctions.SUB_SCALE;
		for (int z = lastz; z >= 0; z--)
		{
			// Need entity position
			int ez = z * PositionFunctions.SUB_SCALE;
			
			// Get the chunk at the given Z position
			Chunk top = cm.chunkByEntityPosition(__x, __y, ez);
		
			// Get the block index in the chunk which is used to check for air
			int bx = PositionFunctions.entityPositionToBlockIndex(__x, __y,
				ez);
			
			// Need the block type here
			BlockType bt = top.blockType(bx);
			
			// Can we spawn in this block?
			if (bt.canPlayerSpawnInBlock())
				hit = true;
			
			// Cannot spawn in the block, but check if we can spawn on top
			else if (hit)
				if (bt.canPlayerSpawnOnTop())
				{
					// Spawn in the block at the last chunk
					this._inchunk = last;
					
					// Set position
					int[] pos = this._pos;
					pos[0] = __x;
					pos[1] = __y;
					pos[2] = ez;
					
					// Debug
					System.err.printf("DEBUG -- Spawn at: (%d, %d, %d)" +
						"[%d, %d, %d]%n", __x, __y, ez,
						__x / PositionFunctions.SUB_SCALE,
						__y / PositionFunctions.SUB_SCALE,
						ez / PositionFunctions.SUB_SCALE);
					
					// Spawned here
					return true;
				}
				
				// Cannot spawn on top of this block, fail
				else
					return false;
			
			// Last chunk spawned in, since spawn in/ontop can traverse chunks
			last = top;
			lastz = z;
			lastez = ez;
		}
		
		// Not found
		return false;
	}
}

