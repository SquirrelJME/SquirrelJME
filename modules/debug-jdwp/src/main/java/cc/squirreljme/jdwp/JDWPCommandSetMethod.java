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
 * Method command set.
 *
 * @since 2021/03/14
 */
public enum JDWPCommandSetMethod
	implements JDWPCommand
{
	/** Line number table. */
	LINE_TABLE(1),
	
	/** Variable table without generics. */
	VARIABLE_TABLE(2),
	
	/** Method byte code. */
	BYTE_CODES(3),
	
	/** Variable table with generics. */
	VARIABLE_TABLE_WITH_GENERIC(5),
		
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/14
	 */
	JDWPCommandSetMethod(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/14
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
