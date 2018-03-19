// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.game;

/**
 * This is used to build the initial settings for the game.
 *
 * This class is not thread safe.
 *
 * @since 2017/02/09
 */
public class InitialSettingsBuilder
{
	/** The width of the map in tiles. */
	volatile int _mapwidth =
		64;
	
	/** The height of the map in tiles. */
	volatile int _mapheight =
		64;
	
	/** The seed to use. */
	volatile long _seed =
		System.currentTimeMillis();
	
	/**
	 * Builds the settings.
	 *
	 * @return The resulting settings.
	 * @since 2017/02/09
	 */
	public InitialSettings build()
	{
		return new InitialSettings(this);
	}
	
	/**
	 * Sets the map size in tiles. If an input dimension is not valid then it
	 * will be corrected.
	 *
	 * @param __w The width of the map.
	 * @param __h The height of the map.
	 * @since 2017/02/10
	 */
	public void mapSize(int __w, int __h)
	{
		this._mapwidth = Math.max(MegaTile.TILES_PER_MEGA_TILE,
			(__w - (__w % MegaTile.TILES_PER_MEGA_TILE)));
		this._mapheight = Math.max(MegaTile.TILES_PER_MEGA_TILE,
			(__h - (__w % MegaTile.TILES_PER_MEGA_TILE)));
	}
	
	/**
	 * Sets the seed to use for random generation.
	 *
	 * @param __s The seed to use.
	 * @since 2017/02/10
	 */
	public void seed(long __s)
	{
		this._seed = __s;
	}
}

