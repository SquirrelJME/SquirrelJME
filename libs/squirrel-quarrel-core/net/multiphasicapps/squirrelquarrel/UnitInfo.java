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
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;

/**
 * This contains a cachable.
 *
 * @since 2017/02/14
 */
public final class UnitInfo
{
	/** Key for hitpoints. */
	public static final JavaManifestKey HP_KEY =
		new JavaManifestKey("hp");
	
	/** Key for armor. */
	public static final JavaManifestKey ARMOR_KEY =
		new JavaManifestKey("armor");
	
	/** Key for unit size. */
	public static final JavaManifestKey SIZE_KEY =
		new JavaManifestKey("size");
	
	/** Key for unit cost in salt. */
	public static final JavaManifestKey SALT_COST_KEY =
		new JavaManifestKey("salt-cost");
	
	/** Key for unit cost in peppers. */
	public static final JavaManifestKey PEPPER_COST_KEY =
		new JavaManifestKey("pepper-cost");
	
	/** Key for build time in frames. */
	public static final JavaManifestKey BUILD_TIME_KEY =
		new JavaManifestKey("build-time");
	
	/** Key for the amount of supply that is provided. */
	public static final JavaManifestKey SUPPLY_PROVIDED_KEY =
		new JavaManifestKey("supply-provided");
	
	/** Key for the amount of supply that is consumed. */
	public static final JavaManifestKey SUPPLY_COST_KEY =
		new JavaManifestKey("supply-cost");
	
	/** Key for the dimensions of the unit in pixels. */
	public static final JavaManifestKey PIXEL_DIMENSIONS_KEY =
		new JavaManifestKey("pixel-dimensions");
	
	/** Key for the offsets in dimensions (used for buildings). */
	public static final JavaManifestKey OFFSET_DIMENSIONS_KEY =
		new JavaManifestKey("offset-dimensions");
	
	/** The key for sight range. */
	public static final JavaManifestKey SIGHT_RANGE_KEY =
		new JavaManifestKey("sight");
	
	/** Key for the build score of the unit. */
	public static final JavaManifestKey SCORE_BUILD_KEY =
		new JavaManifestKey("score-build");
	
	/** Key for the destroy score of the unit. */
	public static final JavaManifestKey SCORE_DESTROY_KEY =
		new JavaManifestKey("score-destroy");
	
	/** Key for the speed of the unit. */
	public static final JavaManifestKey SPEED_KEY =
		new JavaManifestKey("speed");
	
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

