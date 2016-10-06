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
import net.multiphasicapps.squirreldigger.game.chunk.ChunkManager;

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
	
	/** The seed used to generate the map structure. */
	protected final long seed;
	
	/** The manager for chunks. */
	protected final ChunkManager chunkmanager;
	
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
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public void run()
	{
		throw new Error("TODO");
	}
}

