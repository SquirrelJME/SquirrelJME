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
import net.multiphasicapps.squirrelquarrel.player.Player;
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
		this._numscreens = playercount;
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
		int numscreens = this._numscreens;
		GameScreen[] screens = this._screens;
		
		// Just get all the player screens for now
		GameScreen a = screens[0],
			b = screens[1],
			c = screens[2],
			d = screens[3];
		
		// These will be used
		int midx = __w >> 1,
			midy = __h >> 1;
		
		// Depends on the screen space
		switch (numscreens)
		{
				// Single player
			case 1:
				a.configure(0, 0, __w, __h);
				break;
				
				// Two players
			case 2:
				// Split vertically for wider screens
				if (__w > __h)
				{
					a.configure(0, 0, midx, __h);
					b.configure(midx, 0, midx, __h);
				}
				
				// Otherwise split horizontally
				else
				{
					a.configure(0, 0, __w, midy);
					b.configure(0, midy, __w, midy);
				}
				break;
				
				// 3/4 player split
			case 3:
			case 4:
			default:
				a.configure(0, 0, midx, midy);
				b.configure(midx, 0, midx, midy);
				c.configure(0, midy, midx, midy);
				d.configure(midx, midy, midx, midy);
				break;
		}
	}
	
	/**
	 * Returns the number of screens to draw.
	 *
	 * @return The screen count.
	 * @since 2018/03/22
	 */
	public final int count()
	{
		return this._numscreens;
	}
	
	/**
	 * Gets the screen from the given index.
	 *
	 * @param __i The screen to get.
	 * @return The given screen.
	 * @throws IndexOutOfBoundsException If the screen is outside of the
	 * player bounds.
	 * @since 2018/03/22
	 */
	public final GameScreen get(int __i)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error BE0t The game screen which was requested is
		// outside of the player bounds.}
		if (__i < 0 || __i >= this._numscreens)
			throw new IndexOutOfBoundsException("BE0t");
		
		return this._screens[this._order[__i].ordinal()];
	}
}

