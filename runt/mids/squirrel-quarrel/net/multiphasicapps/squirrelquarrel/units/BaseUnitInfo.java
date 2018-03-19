// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.units;

import java.io.InputStream;
import java.io.IOException;
import java.util.Objects;
import net.multiphasicapps.squirrelquarrel.util.ConstantFixedPoint;
import net.multiphasicapps.squirrelquarrel.util.Dimension;
import net.multiphasicapps.squirrelquarrel.util.FixedPoint;
import net.multiphasicapps.squirrelquarrel.util.Point;
import net.multiphasicapps.squirrelquarrel.util.Rectangle;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This contains a cachable.
 *
 * @since 2017/02/14
 */
public final class BaseUnitInfo
{
	/** The type of unit this has info for. */
	public final UnitType type;
	
	/** Unit hitpoints. */
	public final int hp;
	
	/** Unit shields. */
	public final int shields;
	
	/** Armor. */
	public final int armor;
	
	/** Unit size. */
	public final UnitSize size;
	
	/** The cost in salt. */
	public final int salt;
	
	/** The cost in methane. */
	public final int methane;
	
	/** The build time in frames. */
	public final int buildtime;
	
	/** The supply provided. */
	public final int supplyprovided;
	
	/** The supply cost. */
	public final int supplycost;
	
	/** The dimension of the unit in pixels. */
	public final Dimension pixeldimension;
	
	/** Center point offset for the unit. */
	public final Point centerpointoffset;
	
	/** The center point offset used for buildings (based on tile grid). */
	public final Point buildingcenterpointoffset;
	
	/** The unit size in tiles (for buildings). */
	public final Dimension tiledimension;
	
	/** The dimenion of the unit in pixels matching the tiled size. */
	public final Dimension pixeltiledimension;
	
	/** The sight range. */
	public final int sight;
	
	/** The score for creating this unit. */
	public final int scorebuild;
	
	/** The score for destroying this unit. */
	public final int scoredestroy;
	
	/** The speed of this unit, in 16.16 fixed point. */
	public final int speed;
	
	/**
	 * Initializes the unit information from the given manifest.
	 *
	 * @param __m The input unit manifest.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	BaseUnitInfo(JavaManifest __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
			
	/** Key for hitpoints. */
	public static final JavaManifestKey HP_KEY =
		new JavaManifestKey("hp");
		
	/** Key for shields. */
	public static final JavaManifestKey SHIELDS_KEY =
		new JavaManifestKey("shields");
	
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
	
		
		// Set
		this.type = __t;
		
		// Could fail
		String path = "units/" + TerrainType.__lower(__t.name()) + "/info";
		try (InputStream is = BaseUnitInfo.class.getResourceAsStream(path))
		{
			// {@squirreljme.error BE0d No information resource exists for the
			// given unit type. (The unit type; The attempted path)}
			if (is == null)
				throw new IOException(String.format("BE0d %s %s", __t, path));
			
			// Load manifest
			JavaManifest man = new JavaManifest(is);
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// Load these values directly
			this.hp = Integer.parseInt(
				Objects.toString(attr.get(HP_KEY), "0"));
			this.shields = Integer.parseInt(
				Objects.toString(attr.get(SHIELDS_KEY), "0"));
			this.armor = Integer.parseInt(
				Objects.toString(attr.get(ARMOR_KEY), "0"));
			this.salt = Integer.parseInt(
				Objects.toString(attr.get(SALT_COST_KEY), "0"));
			this.pepper = Integer.parseInt(
				Objects.toString(attr.get(PEPPER_COST_KEY), "0"));
			this.buildtime = Integer.parseInt(
				Objects.toString(attr.get(BUILD_TIME_KEY), "0"));
			this.supplyprovided = Integer.parseInt(
				Objects.toString(attr.get(SUPPLY_PROVIDED_KEY), "0"));
			this.supplycost = Integer.parseInt(
				Objects.toString(attr.get(SUPPLY_COST_KEY), "0"));
			this.sight = Integer.parseInt(
				Objects.toString(attr.get(SIGHT_RANGE_KEY), "0"));
			this.scorebuild = Integer.parseInt(
				Objects.toString(attr.get(SCORE_BUILD_KEY), "0"));
			this.scoredestroy = Integer.parseInt(
				Objects.toString(attr.get(SCORE_DESTROY_KEY), "0"));
			this.speed = Integer.parseInt(
				Objects.toString(attr.get(SPEED_KEY), "0"));
			
			// Parse size
			String vsize = Objects.toString(attr.get(SIZE_KEY), "small");
			switch (vsize)
			{
				case "small": this.size = UnitSize.SMALL; break;
				case "medium": this.size = UnitSize.MEDIUM; break;
				case "large": this.size = UnitSize.LARGE; break;
				
					// {@squirreljme.error BE0e Unknown unit size. (Unit size)}
				default:
					throw new IOException(String.format("BE0e %s", vsize));
			}
			
			// Parse unit dimensions and potential offsets
			Dimension pixeldimension = __parseDimension(
				Objects.toString(attr.get(PIXEL_DIMENSIONS_KEY), "0 0"));
			this.pixeldimension = pixeldimension;
			
			// Load offset to calculate building related details
			Point offset = __parsePoint(
				Objects.toString(attr.get(PIXEL_DIMENSIONS_KEY), "0 0"));
			
			// Center point is just half the dimension
			this.centerpointoffset = new Point(pixeldimension.width / 2,
				pixeldimension.height / 2);
			
			// Get total size of unit in tiles, rounded up to the megatile
			int pxw = (pixeldimension.width + offset.x +
					MegaTile.TILE_PIXEL_SIZE) & ~(MegaTile.TILE_PIXEL_MASK),
				pxh = (pixeldimension.height + offset.y +
					MegaTile.TILE_PIXEL_SIZE) & ~(MegaTile.TILE_PIXEL_MASK);
			
			// Determine tile dimension of unit
			Dimension tiledimension = new Dimension(
				pxw / MegaTile.TILE_PIXEL_SIZE,
				pxh / MegaTile.TILE_PIXEL_SIZE);
			this.tiledimension = tiledimension;
			this.pixeltiledimension = new Dimension(
				tiledimension.width / MegaTile.TILE_PIXEL_SIZE,
				tiledimension.height / MegaTile.TILE_PIXEL_SIZE);
			
			// Offset to the center of the building is in the center of
			// the tile dimensions
			this.buildingcenterpointoffset = new Point(pxw / 2, pxh / 2);
		}
		
		// {@squirreljme.error BE0f Failed to load information for the
		// specified unit type. (The unit type)}
		catch (IOException|NumberFormatException e)
		{
			throw new RuntimeException(String.format("BE0f %s", __t), e);
		}
	}
	
	/**
	 * This performs the calculation of determining where a building should be
	 * placed. Building placement is aligned to the grid.
	 *
	 * @param __y If {@code true} then {@code __v} is a Y coordinate.
	 * @param __v The coordinate value.
	 * @return The center position of the building.
	 * @since 2017/02/17
	 */
	public int placeBuilding(boolean __y, int __v)
	{
		return __v;
	}
}

