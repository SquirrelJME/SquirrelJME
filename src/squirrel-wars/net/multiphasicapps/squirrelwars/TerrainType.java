// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelwars;

/**
 * This represents the type of terrain which is available in the game.
 *
 * @since 2016/06/05
 */
public enum TerrainType
{
	/** Produces air units. */
	AIRPORT("Airport", true, ProductionType.AIR),
	
	/** Infantry barracks. */
	BARRACKS("Barracks", true, ProductionType.INFANTRY),
	
	/** A nice beach that can be walked on. */
	BEACH("Beach", false, ProductionType.NOTHING),
	
	/** Increase offense and defense of units. */
	BEACON("Beacon", true, ProductionType.NOTHING),
	
	/** A bridge over a river. */
	BRIDGE("Bridge", false, ProductionType.NOTHING),
	
	/** A city which may be captured. */
	CITY("City", true, ProductionType.NOTHING),
	
	/** A desert which contains much sand. */
	DESERT("Desert", false, ProductionType.NOTHING),
	
	/** A bridge over the ocean that may also be crossed by naval units. */
	DRAWBRIDGE("Drawbridge", false, ProductionType.NOTHING),
	
	/** Produces naval units. */
	DOCK("Dock", true, ProductionType.NAVAL),
	
	/** Produces ground units. */
	FACTORY("Factory", true, ProductionType.GROUND),
	
	/** Has lots of trees. */
	FOREST("Forest", false, ProductionType.NOTHING),
	
	/** Has lots of grass. */
	GRASSLAND("Grasslands", false, ProductionType.NOTHING),
	
	/** Main base for a player. */
	HEADQUARTERS("Headquarters", true, ProductionType.NOTHING),
	
	/** Helipad which produces only helicoptors. */
	HELIPAD("Helipad", true, ProductionType.HELICOPTORS),
	
	/** Contains a missile which may be lauched. */
	MISSILE_SILO("Missile Silo", false, ProductionType.NOTHING),
	
	/** A missile silo with its missile gone. */
	MISSILE_SILO_EMPTY("Empty Silo", false, ProductionType.NOTHING),
	
	/** A tall mountain. */
	MOUNTAIN("Mountain", false, ProductionType.NOTHING),
	
	/** The open ocean. */
	OCEAN("Ocean", false, ProductionType.NOTHING),
	
	/** A pipe which acts as a wall. */
	PIPE("Pipe", false, ProductionType.NOTHING),
	
	/** A reef that is in the water. */
	REEF("Reef", false, ProductionType.NOTHING),
	
	/** A river that infantry may cross. */
	RIVER("River", false, ProductionType.NOTHING),
	
	/** A road that can be driven on. */
	ROAD("Road", false, ProductionType.NOTHING),
	
	/** End. */
	;
	
	/** The name of the terrain. */
	protected final String name;
	
	/** Can this terrain be "owned" by a player? */
	protected final boolean isownable;
	
	/** The units the terrain may produce when owned. */
	protected final ProductionType production;
	
	/**
	 * Initializes the terrain information.
	 *
	 * @param __name The name of the terrain.
	 * @param __owned Can this terrain be owned?
	 * @param __pt The types of unit this terrain produces.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/07
	 */
	private TerrainType(String __name, boolean __owned,
		ProductionType __pt)
		throws NullPointerException
	{
		// Check
		if (__name == null || __pt == null)
			throw new NullPointerException("NARG");
		
		// Set
		name = __name;
		isownable = __owned;
		production = __pt;
	}
}

