// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.world;

import java.io.InputStream;

/**
 * This represents the type of terrain that is available for usage.
 *
 * @since 2017/02/10
 */
public enum TerrainType
{
	/** Grass. */
	GRASS(0x007F00),
	
	/** Dirt. */
	DIRT(0x6E370B),
	
	/** End. */
	;
	
	/** Cache of terrain types. */
	private static final TerrainType[] _VALUES =
		values();
	
	/** The terrain color. */
	protected final int color;
	
	/**
	 * Initializes the terrain type.
	 *
	 * @param __color The color used for this terrain.
	 * @since 2017/09/13
	 */
	private TerrainType(int __color)
	{
		// Set
		this.color = __color;
	}
	
	/**
	 * Returns the color used for the terrain.
	 *
	 * @return The terrain color.
	 * @since 2017/02/13
	 */
	public int color()
	{
		return this.color;
	}
	
	/**
	 * Returns an input stream to the image data.
	 *
	 * @return The image data input stream.
	 * @throws RuntimeException If the data is missing.
	 * @since 2017/02/10
	 */
	public InputStream imageStream()
		throws RuntimeException
	{
		// {@squirreljme.error BE0j Could not get the tile image for the
		// given terrain. (The terrain type)}
		InputStream rv = TerrainType.class.getResourceAsStream(
			"images/terrain/" + __lower(name()) + ".xpm");
		if (rv == null)
			throw new RuntimeException(String.format("BE0j %s", this));
		return rv;
	}
	
	/**
	 * Returns the terrain type of the given ID.
	 *
	 * @param __i The ID of the terrain to return.
	 * @return The terrain type at this position.
	 * @since 2017/02/13
	 */
	public static TerrainType of(int __i)
	{
		return _VALUES[__i];
	}
	
	/**
	 * Lowercases the specified string.
	 *
	 * @param __s The string to lowercase.
	 * @return The lowercased string.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	static final String __lower(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Convert
		int n = __s.length();
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			if (c >= 'A' && c <= 'Z')
				c = (char)((c - 'A') + 'a');
			
			sb.append(c);
		}
		
		return sb.toString();
	}
}

