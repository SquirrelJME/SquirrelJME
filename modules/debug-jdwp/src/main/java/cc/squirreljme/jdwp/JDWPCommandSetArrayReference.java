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
 * Array references.
 *
 * @since 2021/03/19
 */
public enum JDWPCommandSetArrayReference
	implements JDWPCommand
{
	/** Length of array. */
	LENGTH(1),
	
	/** Get values. */
	GET_VALUES(2),
		
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/19
	 */
	JDWPCommandSetArrayReference(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/19
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
