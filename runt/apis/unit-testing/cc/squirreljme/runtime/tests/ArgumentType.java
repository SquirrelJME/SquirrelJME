// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.tests;

/**
 * This represents the type of value the parameter takes which determines
 * which annotation field is valid.
 *
 * @since 2018/03/06
 */
public enum ArgumentType
{
	/** Null. */
	NULL,
	
	/** Boolean. */
	BOOLEAN,
	
	/** Byte. */
	BYTE,
	
	/** Short. */
	SHORT,
	
	/** Character. */
	CHARACTER,
	
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/** String. */
	STRING,
	
	/** Class Type. */
	CLASS,
	
	/** Enumeration. */
	ENUMERATION,
	
	/** End. */
	;
}

