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

import net.multiphasicapps.squirrelquarrel.util.ReplayInputStream;
import net.multiphasicapps.squirrelquarrel.util.ReplayFormatException;
import net.multiphasicapps.squirrelquarrel.util.ReplayOutputStream;

/**
 * This contains the initial settings which is used to determine how to
 * generate and initialize the initial game.
 *
 * @since 2017/02/09
 */
public final class InitialSettings
{
	/** Magic number for initial settings. */
	private static final long _MAGIC_NUMBER =
		0x53715872FF00FFFFL;
	
	/** The timestamp the game started. */
	protected final long timestamp;
	
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
		this.timestamp = __b._timestamp;
	}
	
	/**
	 * Writes the settings to the given replay output stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public final void demoRecord(ReplayOutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		__out.writeLong(InitialSettings._MAGIC_NUMBER);
		__out.writeLong(this.timestamp);
		__out.writeLong(this.seed);
		__out.writeInt(this.mapwidth);
		__out.writeInt(this.mapheight);
	}
	
	/**
	 * Returns the height of the map.
	 *
	 * @return The height of the map.
	 * @since 2017/02/10
	 */
	public final int mapHeight()
	{
		return this.mapheight;
	}
	
	/**
	 * Returns the width of the map.
	 *
	 * @return The width of the map.
	 * @since 2017/02/10
	 */
	public final int mapWidth()
	{
		return this.mapwidth;
	}
	
	/**
	 * Returns the seed for the map and game events.
	 *
	 * @return The game seed.
	 * @since 2017/02/10
	 */
	public final long seed()
	{
		return this.seed;
	}
	
	/**
	 * Returns the starting timestamp of the game.
	 *
	 * @return The starting timestamp of the game.
	 * @since 2018/03/19
	 */
	public final long startTimeMillis()
	{
		return this.timestamp;
	}
	
	/**
	 * Reads the initial settings from the replay input.
	 *
	 * @param __in The stream to read from.
	 * @return The initial settings.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public static final InitialSettings demoReplay(ReplayInputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BE0q Invalid initial settings magic number.
		// (The read magic number)}
		long magic = __in.readLong();
		if (magic != InitialSettings._MAGIC_NUMBER)
			throw new ReplayFormatException(String.format("BE0q %08x", magic));
		
		InitialSettingsBuilder rv = new InitialSettingsBuilder();
		
		rv.startTimeMillis(__in.readLong());
		rv.seed(__in.readLong());
		rv.mapSize(__in.readInt(), __in.readInt());
		
		return rv.build();
	}
}

