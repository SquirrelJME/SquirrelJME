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
import java.io.IOException;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;

/**
 * This contains a cachable.
 *
 * @since 2017/02/14
 */
public final class UnitInfo
{
	/** The type of unit this has info for. */
	protected final UnitType type;
	
	/**
	 * Initializes the unit information.
	 *
	 * @param __t The unit to load information for.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/15
	 */
	UnitInfo(UnitType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __t;
		
		// Could fail
		String path = "units/" + TerrainType.__lower(__t.name()) + "/info";
		try (InputStream is = UnitInfo.class.getResourceAsStream(path))
		{
			// {@squirreljme.error BE0b No information resource exists for the
			// given unit type. (The unit type; The attempted path)}
			if (is == null)
				throw new IOException(String.format("BE0b %s %s", __t, path));
			
			// Load manifest
			JavaManifest man = new JavaManifest(is);
			JavaManifestAttributes attr = man.getMainAttributes();
			
			if (false)
				throw new IOException("OOPS");
			throw new Error("TODO");
		}
		
		// {@squirreljme.error BE0a Failed to load information for the
		// specified unit type. (The unit type)}
		catch (IOException e)
		{
			throw new RuntimeException(String.format("BE0a %s", __t), e);
		}
	}
	
	/**
	 * Returns the unit type which this has informaton for.
	 *
	 * @return The unit type this has information for,
	 * @since 2017/02/15
	 */
	public UnitType type()
	{
		return this.type;
	}
}

