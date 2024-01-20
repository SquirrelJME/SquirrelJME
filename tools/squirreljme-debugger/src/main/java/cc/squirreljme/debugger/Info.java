// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Information storage for types and otherwise that are known to the
 * debugger.
 *
 * @since 2024/01/19
 */
public abstract class Info
{
	/** The ID of this item. */
	protected final int id;
	
	/** The type of info this is. */
	protected final InfoType type;
	
	/**
	 * Initializes the base information.
	 *
	 * @param __id The ID number of this info.
	 * @since 2024/01/20
	 */
	public Info(int __id, InfoType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.type = __type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/20
	 */
	@Override
	public String toString()
	{
		return String.format("%s#%d",
			this.type, this.id);
	}
}
