// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreldigger.game;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreldigger.game.chunk.ChunkManager;
import net.multiphasicapps.squirreldigger.game.player.Controller;
import net.multiphasicapps.squirreldigger.game.player.Player;

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
	
	/** The lock for the game loop. */
	protected final Object lock =
		new Object();
	
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
		this.chunkmanager = new ChunkManager(__seed, __root);
	}
	
	/**
	 * Adds a player to the game.
	 *
	 * @return The newly added player.
	 * @since 2016/10/06
	 */
	public Player addPlayer()
	{
		throw new Error("TODO");
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
		synchronized (this.lock)
		{
			// Are there any players to be added to the game?
			List<Player> players = this._players;
			Deque<Player> waitplayers = this._waitplayers;
			if (!waitplayers.isEmpty())
				throw new Error("TODO");
			
			// Handle player specific logic
			int n = players.size();
			for (int i = 0; i < n; i++)
			{
				Player p = players.get(i);
				
				throw new Error("TODO");
			}
			
			if (true)
				throw new Error("TODO");
			
			// Always increment the game frame
			this._currentframe = currentframe + 1;
		}
	}
}

