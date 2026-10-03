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
 * Support for class loader information.
 *
 * @since 2021/04/20
 */
public enum JDWPCommandSetClassLoader
	implements JDWPCommand
{
	/** Classes which are visible to the given loader. */
	VISIBLE_CLASSES(1),
	
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/04/20
	 */
	JDWPCommandSetClassLoader(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/20
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
