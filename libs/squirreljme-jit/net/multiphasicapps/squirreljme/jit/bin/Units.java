// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.java.ClassName;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This contains all of the units (which are classes) which are associated with
 * the linker.
 *
 * @since 2017/06/17
 */
public final class Units
	extends __SubState__
{
	/** Mapping of class names to units. */
	private final Map<ClassName, Unit> _units =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the unit manager.
	 *
	 * @param __ls The owning linker state.
	 * @since 2017/06/17
	 */
	Units(Reference<LinkerState> __ls)
	{
		super(__ls);
	}
	
	/**
	 * Creates a new unit and places it within this set of units.
	 *
	 * @param __n The name of the unit to create.
	 * @return The newly created unit.
	 * @throws JITException If the unit already exists.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/08
	 */
	public final Unit createUnit(ClassName __n)
		throws JITException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI16 Duplicate class name.
		// (The duplicate class)}
		Map<ClassName, Unit> units = this._units;
		if (units.containsKey(__n))
			throw new JITException(String.format("JI16 %s", __n));
		
		// Create unit
		Unit rv = new Unit(__linkerState().__reference(), __n);
		units.put(__n, rv);
		return rv;
	}
}

