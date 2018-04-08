// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.layout;

/**
 * This represents a standard modifier.
 *
 * @since 2018/04/08
 */
public enum StandardModifier
	implements Modifier
{
	/** Abstract. */
	ABSTRACT,
		
	/** Final. */
	FINAL,
		
	/** Native. */
	NATIVE,
		
	/** Private. */
	PRIVATE,
		
	/** Protected. */
	PROTECTED,
		
	/** Public. */
	PUBLIC,
		
	/** Static. */
	STATIC,
		
	/** Strict floating point. */
	STRICTFP,
		
	/** Synchronized. */
	SYNCHRONIZED,
		
	/** Transient. */
	TRANSIENT,
		
	/** Volatile. */
	VOLATILE,
	
	/** End. */
	;
}

