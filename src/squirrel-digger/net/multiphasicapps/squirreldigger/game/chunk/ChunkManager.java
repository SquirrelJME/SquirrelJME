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

import java.nio.file.Path;
import net.multiphasicapps.squirreldigger.game.Game;

/**
 * This class is used to manage chunks which may be cached, loaded, and stored
 * by the game.
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
		this.seed = __seed;
		this.root = __root;
	}
}

