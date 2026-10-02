// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Represents the class type.
 *
 * @since 2021/03/13
 */
public enum JDWPClassType
{
	/** A class. */
	CLASS(1),
	
	/** An interface. */
	INTERFACE(2),
	
	/** An array. */
	ARRAY(3),
	
	/* End. */
	;
	
	/** The ID. */
	public final int id;
	
	/**
	 * Initializes the class type.
	 * 
	 * @param __id The ID.
	 * @since 2021/03/13
	 */
	JDWPClassType(int __id)
	{
		this.id = __id;
	}
}
