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
 * This contains the initial settings which is used to determine how to
 * generate and initialize the initial game.
 *
 * @since 2017/02/09
 */
public final class InitialSettings
{
	/** The width of the map in tiles. */
	protected final int mapwidth;
	
	/** The height of the map in tiles. */
	protected final int mapheight;
	
	/** The level seed. */
	protected final long seed;
	
	/**
	 * Initializes the initial settings.
	 *
	 * @param __b The initial settings to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/09
	 */
	InitialSettings(InitialSettingsBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.mapwidth = __b._mapwidth;
		this.mapheight = __b._mapheight;
		this.seed = __b._seed;
	}
	
	/**
	 * Returns the height of the map.
	 *
	 * @return The height of the map.
	 * @since 2017/02/10
	 */
	public int mapHeight()
	{
		return this.mapheight;
	}
	
	/**
	 * Returns the width of the map.
	 *
	 * @return The width of the map.
	 * @since 2017/02/10
	 */
	public int mapWidth()
	{
		return this.mapwidth;
	}
	
	/**
	 * Returns the seed for the map and game events.
	 *
	 * @return The game seed.
	 * @since 2017/02/10
	 */
	public long seed()
	{
		return this.seed;
	}
}

