// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

/**
 * This represents the type of comparison to perform.
 *
 * @since 2019/03/26
 */
public enum CompareType
{
	/** Equals. */
	EQUALS,
	
	/** Not equals. */
	NOT_EQUALS,
	
	/** Less than. */
	LESS_THAN,
	
	/** Less or equals. */
	LESS_THAN_OR_EQUALS,
	
	/** Greater than. */
	GREATER_THAN,
	
	/** Greater or equals. */
	GREATER_THAN_OR_EQUALS,
	
	/** Always true. */
	TRUE,
	
	/** Always false. */
	FALSE,
	
	/** End. */
	;
}

