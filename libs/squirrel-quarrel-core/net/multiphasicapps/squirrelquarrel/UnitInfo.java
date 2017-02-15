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
		
		throw new Error("TODO");
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

