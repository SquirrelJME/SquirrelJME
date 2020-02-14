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

/**
 * This indicates the of placement to be performed when placing a unit.
 *
 * @since 2017/02/16
 */
public enum SpawnPlacementType
{
	/** Forced placement, it is created regardless if it is valid or not. */
	FORCED,
	
	/** Unit should follow building spawn rules, aligned to grid. */
	BUILDING,
	
	/** Normal best fit unit spawning, a creator may be used as a factory. */
	NORMAL,
	
	/** End. */
	;
}

