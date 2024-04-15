// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import java.util.Random;

/**
 * This class keeps track of the entire game state, such as active players,
 * artificial intelligence, the tile map, and the units.
 *
 * @since 2019/07/01
 */
public final class Game
{
	/** The random number generator. */
	public final Random random;
	
	/** The world map. */
	public final TileMap tilemap;
	
	/**
	 * Initializes the game.
	 *
	 * @param __seed The seed to use.
	 * @param __mapsize The size of the map.
	 * @param __numpl The number of players to use.
	 * @since 2019/07/01
	 */
	public Game(long __seed, MapSize __mapsize, int __numpl)
	{
		// Initialize RNG
		Random random;
		this.random = (random = new Random(__seed));
		
		// Initialize random map
		TileMap tilemap;
		this.tilemap = (tilemap = new TileMap(random, __mapsize, __numpl));
	}
}

