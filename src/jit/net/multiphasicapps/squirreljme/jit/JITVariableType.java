// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is the type of value that is stored within virtual registers.
 *
 * @since 2016/08/29
 */
public enum JITVariableType
{
	/** An {@code int}. */
	INTEGER,
	
	/** A {@code long}. */
	LONG,
	
	/** A {@code float}. */
	FLOAT,
	
	/** A {@code double}. */
	DOUBLE,
	
	/** An object (reference). */
	OBJECT,
	
	/** End. */
	;
}

