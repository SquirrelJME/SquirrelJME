// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
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
	AIRPORT("Airport", true),
	
	/** A nice beach that can be walked on. */
	BEACH("Beach", false),
	
	/** Increase offense and defense of units. */
	BEACON("Beacon", true),
	
	/** A bridge over a river. */
	BRIDGE("Bridge", false),
	
	/** A city which may be captured. */
	CITY("City", true),
	
	/** A bridge over the ocean that may also be crossed by naval units. */
	DRAWBRIDGE("Drawbridge", false),
	
	/** Produces naval units. */
	DOCK("Dock", true),
	
	/** Produces ground units. */
	FACTORY("Factory", true),
	
	/** Has lots of trees. */
	FOREST("Forest", false),
	
	/** Has lots of grass. */
	GRASSLAND("Grasslands", false),
	
	/** Main base for a player. */
	HEADQUARTERS("Headquarters", true),
	
	/** Contains a missile which may be lauched. */
	MISSILE_SILO("Missile Silo", false),
	
	/** A missile silo with its missile gone. */
	MISSILE_SILO_EMPTY("Empty Silo", false),
	
	/** A tall mountain. */
	MOUNTAIN("Mountain", false),
	
	/** The open ocean. */
	OCEAN("Ocean", false),
	
	/** A pipe which acts as a wall. */
	PIPE("Pipe", false),
	
	/** A reef that is in the water. */
	REEF("Reef", false),
	
	/** A river that infantry may cross. */
	RIVER("River", false),
	
	/** A road that can be driven on. */
	ROAD("Road", false),
	
	/** End. */
	;
	
	/** The name of the terrain. */
	protected final String name;
	
	/** Can this terrain be "owned" by a player? */
	protected final boolean isownable;
	
	/**
	 * Initializes the terrain information.
	 *
	 * @param __name The name of the terrain.
	 * @param __owned Can this terrain be owned?
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/07
	 */
	private TerrainType(String __name, boolean __owned)
		throws NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Set
		name = __name;
		isownable = __owned;
	}
}

