// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * Returns the type of class this is.
 *
 * @since 2018/05/14
 */
public enum ClassType
{
	/** Normal class. */
	CLASS,
	
	/** Interface. */
	INTERFACE,
	
	/** Enumeration. */
	ENUM,
	
	/** Annotation. */
	ANNOTATION,
	
	/* End. */
	;
}

