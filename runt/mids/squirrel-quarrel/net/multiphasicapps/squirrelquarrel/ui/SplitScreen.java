// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.ui;

import net.multiphasicapps.squirrelquarrel.game.Game;
import net.multiphasicapps.squirrelquarrel.game.GameLooper;
import net.multiphasicapps.squirrelquarrel.player.PlayerColor;
import net.multiphasicapps.squirrelquarrel.player.Players;

/**
 * This class is used to manage multiple screen spaces so that multiple players
 * can play on the same screen at once.
 *
 * @since 2018/03/19
 */
public final class SplitScreen
{
	/** Screens that may be visible. */
	private final GameScreen[] _screens =
		new GameScreen[PlayerColor.MAX_PLAYERS];
	
	/** The order of the screens. */
	private final PlayerColor[] _order =
		new PlayerColor[PlayerColor.MAX_PLAYERS];
	
	/** The number of visible screens. */
	private volatile int _numscreens =
		1;
	
	/** The width of the surface. */
	private volatile int _surfacew =
		1;
	
	/** The height of the surface. */
	private volatile int _surfaceh =
		1;
	
	/**
	 * Initializes the split screen.
	 *
	 * @param __loop The game being played.
	 * @throws NullPointerException On null arguments.
	 * @since 2108/03/19
	 */
	public SplitScreen(GameLooper __loop)
		throws NullPointerException
	{
		if (__loop == null)
			throw new NullPointerException("NARG");
		
		Players players = __loop.game().players();
		
		GameScreen[] screens = this._screens;
		PlayerColor[] order = this._order;
		
		// Initialize each screen
		int playercount = 0;
		for (int i = 0, n = screens.length; i < n; i++)
		{
			Player p = players.get(i);
			order[i] = p.color();
			screens[i] = new GameScreen(__loop, p);
			if (p.isPlaying())
				playercount++;
		}
	}
	
	/**
	 * Configure the screens for the given size.
	 *
	 * @param __w The screen width.
	 * @param __h The screen height.
	 * @since 2018/03/19
	 */
	public final void configure(int __w, int __h)
	{
		throw new todo.TODO();
	}
}

