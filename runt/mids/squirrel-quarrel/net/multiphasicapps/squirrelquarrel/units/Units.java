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

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirrelquarrel.game.InitialSettings;

/**
 * This class handles and manages all of the units in the game along with
 * their mega tile links.
 *
 * @since 2018/03/18
 */
public final class Units
{
	/** Linked units into mega tiles. */
	protected final UnitLinker linker =
		new UnitLinker();
	
	/** Units in the game. */
	private final List<Unit> _units =
		new ArrayList<>();
	
	/** The ID of the next unit to create. */
	private volatile int _nextid;
	
	/**
	 * Initializes the units.
	 *
	 * @param __is The initial settings to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public Units(InitialSettings __is)
		throws NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * Creates a new unit.
	 *
	 * @param __spt How the unit should be placed on the map.
	 * @param __t The type of unit to spawn.
	 * @param __creator The creating unit, may be {@code null}.
	 * @param __x The target X position.
	 * @param __y The target Y position.
	 * @return The created unit or {@code null} if it could not be created.
	 * @throws NullPointerException If no spawn type or unit type was
	 * specified.
	 * @since 2017/02/16
	 */
	public final Unit createUnit(SpawnPlacementType __spt, UnitType __t,
		Unit __creator, int __x, int __y)
		throws NullPointerException
	{
		throw new todo.TODO();
		/*
		return this.createUnit(__spt, __t, (__creator != null ?
			__creator.pointer() : null), __x, __y);
		*/
	}
	
	/**
	 * Creates a new unit.
	 *
	 * @param __spt How the unit should be placed on the map.
	 * @param __t The type of unit to spawn.
	 * @param __creator The creating unit, may be {@code null}.
	 * @param __x The target center X position.
	 * @param __y The target center Y position.
	 * @return The created unit or {@code null} if it could not be created.
	 * @throws NullPointerException If no spawn type or unit type was
	 * specified.
	 * @since 2017/02/16
	 */
	public final Unit createUnit(SpawnPlacementType __spt, UnitType __t,
		UnitReference __creator, int __x, int __y)
		throws NullPointerException
	{
		// Check
		if (__spt == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// Setup unit
		Unit rv = new Unit(this);
		UnitInfo info = __t.info();
		
		// Morph to the given unti type
		rv.morph(__t);
		
		// Determine the location where the unit is to be placed
		int px, py;
		switch (__spt)
		{
				// No restriction
			case FORCED:
				px = __x;
				py = __y;
				break;
				
				// Building, 
			case BUILDING:
				px = info.placeBuilding(false, __x);
				py = info.placeBuilding(true, __y);
				break;
				
				// Normal placement
			case NORMAL:
				throw new todo.TODO();
				
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
		
		// Move unit to the specified position
		rv.__move(px, py);
		
		// Try to replace an existing null with this new unit
		List<Unit> units = this._units;
		boolean didset = false;
		for (int n = units.size(), i = n - 1; i >= 0; i++)
			if (units.get(i) == null)
			{
				units.set(i, rv);
				didset = true;
				break;
			}
		
		// Otherwise place at end
		if (!didset)
			units.add(rv);
		
		// Link unit into the map
		rv.__link(true);
		
		// Return it
		return rv;
		*/
	}
	
	/**
	 * Runs the unit logic.
	 *
	 * @param __frame The current game frame.
	 * @since 2018/03/18
	 */
	public void run(int __frame)
	{
		throw new todo.TODO();
		/*
		// Run the unit logic for every unit
		List<Unit> units = this._units;
		for (int i = 0, n = units.size(); i < n; i++)
			if (units.get(i).run(framenum))
				units.set(i, null);
		*/
	}
}

