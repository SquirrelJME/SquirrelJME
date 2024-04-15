// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.annotation;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * Specifies the type of element that the annotation is permitted to be
 * attached to in the source code.
 *
 * @since 2014/10/13
 */
@Api
public enum ElementType
{
	/** Annotation types). */
	@Api
	ANNOTATION_TYPE(),
	
	/** Constructors. */
	@Api
	CONSTRUCTOR(),
	
	/** Fields. */
	@Api
	FIELD(),
	
	/** Local variables. */
	@Api
	LOCAL_VARIABLE(),
	
	/** Methods. */
	@Api
	METHOD(),
	
	/** Packages. */
	@Api
	PACKAGE(),
	
	/** Parameter of a method. */
	@Api
	PARAMETER(),
	
	/** A class, interface, or enumeration. */
	@Api
	TYPE(),
	
	/** End. */
	;
}

