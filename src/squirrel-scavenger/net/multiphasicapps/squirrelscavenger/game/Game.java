// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.game;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.multiphasicapps.squirrelscavenger.game.chunk.ChunkManager;
import net.multiphasicapps.squirrelscavenger.game.entity.Entity;
import net.multiphasicapps.squirrelscavenger.game.entity.EntityType;
import net.multiphasicapps.squirrelscavenger.game.player.Controller;
import net.multiphasicapps.squirrelscavenger.game.player.Player;

/**
 * This class is the main game implementation.
 *
 * @since 2016/10/06
 */
public class Game
	implements Runnable
{
	/** The framerate of the game. */
	public static final int FRAME_RATE =
		20;
	
	/** The number of tries to spend spawning the entity before giving up. */
	private static final int _SURFACE_SPAWN_TRIES =
		8;
	
	/** The seed used to generate the map structure. */
	protected final long seed;
	
	/** The manager for chunks. */
	protected final ChunkManager chunkmanager;
	
	/** Players in the game. */
	private final List<Player> _players =
		new ArrayList<>();
	
	/** Players waiting to join. */
	private final Deque<Player> _waitplayers =
		new ArrayDeque<>();
	
	/** The player to entity mapping. */
	private final Map<Player, Entity> _playermap =
		new HashMap<>();
	
	/** Random number generator, */
	private final Random _random;
	
	/** The current game frame. */
	private volatile long _currentframe;
	
	/**
	 * Initializes the game with the given seed and path to the game chunks.
	 *
	 * @param __seed The map generator seed.
	 * @param __root The directory where chunks 
	 */
	public Game(long __seed, Path __root)
		throws NullPointerException
	{
		// Check
		if (__root == null)
			throw new NullPointerException("NARG");
		
		// Setup chunk manager
		this.seed = __seed;
		this.chunkmanager = new ChunkManager(this, __seed, __root);
		
		// Setup PRNG
		this._random = new Random(__seed);
	}
	
	/**
	 * Adds a player to the game.
	 *
	 * @return The newly added player.
	 * @since 2016/10/06
	 */
	public Player addPlayer()
	{
		// Lock
		synchronized (this)
		{
			// Create new player
			Player rv = new Player(this);
			
			// Add to the queue of waiting players
			this._waitplayers.offerLast(rv);
			
			// Return the given player
			return rv;
		}
	}
	
	/**
	 * Returns the chunk manager.
	 *
	 * @return The chunk manager.
	 * @since 2016/10/09
	 */
	public ChunkManager chunkManager()
	{
		return this.chunkmanager;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public void run()
	{
		// Need frame
		long currentframe = this._currentframe;
		Random random = this._random;
		synchronized (this)
		{
			// Are there any players to be added to the game?
			List<Player> players = this._players;
			Deque<Player> waitplayers = this._waitplayers;
			if (!waitplayers.isEmpty())
			{
				Player p = waitplayers.removeFirst();
				
				// Only add if it is below the maximum
				if (players.size() < Integer.MAX_VALUE)
					players.add(p);
			}
			
			// Handle player specific logic
			int n = players.size();
			Map<Player, Entity> playermap = this._playermap;
			for (int i = 0; i < n; i++)
			{
				Player p = players.get(i);
				
				// If the player has no entity then they either just joined or
				// got killed and are trying to respawn
				Entity e = playermap.get(p);
				if (e == null || !e.isSpawned())
				{
					// Create new entity and map it to the player
					e = new Entity(this);
					playermap.put(p, e);
					
					// Make them a squirrel
					e.initializeTo(EntityType.SQUIRREL);
					
					// Attempt to spawn them on the surface of the world
					int tries = _SURFACE_SPAWN_TRIES;
					for (int j = 0; j < tries; j++)
						if (e.spawnOnSurface(random.nextInt(),
							random.nextInt()))
							break;
				}
				
				// If not spawned, calculate for other players instead
				if (!e.isSpawned())
					continue;
				
				throw new Error("TODO");
			}
			
			// Always increment the game frame
			this._currentframe = currentframe + 1;
		}
	}
}

