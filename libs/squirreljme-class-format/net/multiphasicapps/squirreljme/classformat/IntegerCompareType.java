// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This represents the type of comparison which can be performed on integer
 * values.
 *
 * @since 2017/04/01
 */
public enum IntegerCompareType
	implements CompareType
{
	/** Equality. */
	EQUAL,
	
	/** Inequality. */
	NOT_EQUAL,
	
	/** Less than. */
	LESS_THAN,
	
	/** Less then or equal to. */
	LESS_THAN_OR_EQUAL,
	
	/** Greater than. */
	GREATER_THAN,
	
	/** Greater than or equal to. */
	GREATER_THAN_OR_EQUAL,
	
	/** End. */
	;
}

