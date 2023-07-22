// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.player;

import net.multiphasicapps.squirrelquarrel.game.InitialSettings;

/**
 * This manages the players which are in the game.
 *
 * @since 2018/03/18
 */
public final class Players
{
	/** Players in the game. */
	private final Player[] _players =
		new Player[PlayerColor.NUM_COLORS];
	
	/**
	 * Initializes the players.
	 *
	 * @param __is The initial settings to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public Players(InitialSettings __is)
		throws NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Needed to initialize the team a player is on
		int numplayers = __is.players();
		int[] teams = __is.teams();
		
		// Initialize players
		Player[] players = this._players;
		for (int i = 0, n = players.length; i < n; i++)
			players[i] = new Player(PlayerColor.of(i),
				PlayerColor.of(teams[i]), i < numplayers);
	}
	
	/**
	 * Returns the player by the given index.
	 *
	 * @param __i The index of the player to get.
	 * @return The player for the given index.
	 * @throws IllegalArgumentException If the index is not within bounds.
	 * @since 2017/02/14
	 */
	public Player get(int __i)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error BE05 Invalid player index. (The index)} */
		if (__i < 0 || __i >= PlayerColor.NUM_COLORS)
			throw new IllegalArgumentException(String.format("BE05 %d", __i));
		
		return this._players[__i];
	}
	
	/**
	 * Returns the player by the given color.
	 *
	 * @param __p The color to get the player of.
	 * @return The player for the given color.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/14
	 */
	public Player get(PlayerColor __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return this._players[__p.ordinal()];
	}
	
	/**
	 * Runs each individual player.
	 *
	 * @param __framenum The current game frame.
	 * @since 2018/03/18
	 */
	public void run(int __framenum)
	{
		// Run the player logic for each player
		Player[] players = this._players;
		for (int i = 0, n = players.length; i < n; i++)
			players[i].run(__framenum);
	}
}

