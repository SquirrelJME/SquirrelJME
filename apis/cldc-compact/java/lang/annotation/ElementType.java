// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.annotation;

/**
 * Specifies the type of element that the annotation is permitted to be
 * attached to in the source code.
 *
 * @since 2014/10/13
 */
public enum ElementType
{
	/** Annotation types). */
	ANNOTATION_TYPE(),
	
	/** Constructors. */
	CONSTRUCTOR(),
	
	/** Fields. */
	FIELD(),
	
	/** Local variables. */
	LOCAL_VARIABLE(),
	
	/** Methods. */
	METHOD(),
	
	/** Packages. */
	PACKAGE(),
	
	/** Parameter of a method. */
	PARAMETER(),
	
	/** A class, interface, or enumeration. */
	TYPE(),
	
	/** End. */
	;
}

