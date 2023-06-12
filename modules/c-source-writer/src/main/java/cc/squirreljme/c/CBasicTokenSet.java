// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * A basic token set.
 *
 * @since 2023/06/12
 */
public enum CBasicTokenSet
	implements CTokenSet
{
	/**
	 * Cast to the given type.
	 * 
	 * Example: {@code (jint)squirrel}.
	 */
	CAST,
	
	/** A declaration of a struct. */
	STRUCT_DECLARATION,
	
	/** A definition of a struct. */
	STRUCT_DEFINITION,
	
	/** A member of a struct. */
	STRUCT_MEMBER,
	
	/* End. */
	;
}
