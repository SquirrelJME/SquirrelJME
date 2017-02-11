// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel;

import java.io.InputStream;

/**
 * This represents the type of terrain that is available for usage.
 *
 * @since 2017/02/10
 */
public enum TerrainType
{
	/** Grass. */
	GRASS,
	
	/** End. */
	;
	
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
		// {@squirreljme.error BE02 Could not get the tile image for the
		// given terrain. (The terrain type)}
		InputStream rv = TerrainType.class.getResourceAsStream(
			"images/terrain/" + __lower(name()) + ".xpm");
		if (rv == null)
			throw new RuntimeException(String.format("BE02 %s", this));
		return rv;
	}
	
	/**
	 * Lowercases the specified string.
	 *
	 * @param __s The string to lowercase.
	 * @return The lowercased string.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	private static final String __lower(String __s)
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

