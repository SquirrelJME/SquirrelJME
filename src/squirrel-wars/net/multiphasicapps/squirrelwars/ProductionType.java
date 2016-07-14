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
 * This is the type of unit that specific owned terrain may produce.
 *
 * @since 2016/06/05
 */
public enum ProductionType
{
	/** Produces nothing. */
	NOTHING,
	
	/** Produces ground units. */
	GROUND,
	
	/** Produces air units. */
	AIR,
	
	/** Produces naval units. */
	NAVAL,
	
	/** Produces only infantry. */
	INFANTRY,
	
	/** Produces only helicoptors. */
	HELICOPTORS,
	
	/** End. */
	;
}

