// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.player;

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
	 * Initializes the player mappings.
	 *
	 * @since 2018/03/18
	 */
	{
		// Initialize players
		Player[] players = this._players;
		for (int i = 0, n = players.length; i < n; i++)
			players[i] = new Player(this, PlayerColor.of(i));
	}
	
	/**
	 * Runs each individual player.
	 *
	 * @param __frame The current game frame.
	 * @since 2018/03/18
	 */
	public void run(int __frame)
	{
		// Run the player logic for each player
		Player[] players = this._players;
		for (int i = 0, n = players.length; i < n; i++)
			players[i].run(framenum);
	}
}

