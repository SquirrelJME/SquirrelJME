// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

/**
 * These are flags which may be defined by classes accordingly.
 *
 * @since 2018/03/10
 */
@Deprecated
public enum DefinedClassFlag
{
	/** Public. */
	PUBLIC,
	
	/** Protected. */
	PROTECTED,
	
	/** Private. */
	PRIVATE,
	
	/** Abstract. */
	ABSTRACT,
	
	/** Static. */
	STATIC,
	
	/** Final. */
	FINAL,
	
	/** Strict floating point. */
	STRICTFP,
	
	/** An inner class. */
	INNER_CLASS,
	
	/** Enumeration. */
	ENUM,
	
	/** Interface. */
	INTERFACE,
	
	/** Annotation. */
	ANNOTATION,
	
	/** Is the class anonymous? */
	ANONYMOUS,
	
	/** End. */
	;
}

