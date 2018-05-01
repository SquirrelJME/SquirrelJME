// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

/**
 * This represents a basic modifier.
 *
 * @since 2018/04/21
 */
public enum BasicModifierSyntax
	implements ModifierSyntax
{
	/** Public access. */
	PUBLIC,
	
	/** Protected access. */
	PROTECTED,
	
	/** Private access. */
	PRIVATE,
	
	/** Static access. */
	STATIC,
	
	/** Abstract. */
	ABSTRACT,
	
	/** Final. */
	FINAL,
	
	/** Native. */
	NATIVE,
	
	/** Synchronized. */
	SYNCHRONIZED,
	
	/** Transient. */
	TRANSIENT,
	
	/** Volatile. */
	VOLATILE,
	
	/** Strict floating point (should not be used today). */
	STRICTFP,
	
	/** End. */
	;
}

