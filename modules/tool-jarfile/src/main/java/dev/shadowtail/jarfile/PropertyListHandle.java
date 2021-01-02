// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a handle which represents a list of properties.
 *
 * @since 2020/12/20
 */
public abstract class PropertyListHandle
	extends ListValueHandle
{
	/**
	 * Initializes the base property list handle.
	 *
	 * @param __id The memory handle ID.
	 * @param __count The size of the list.
	 * @throws IllegalArgumentException If the memory handle does not have the
	 * correct security bits specified or if the count is negative.
	 * @since 2020/12/20
	 */
	PropertyListHandle(int __id, int __count)
		throws IllegalArgumentException
	{
		super(__id, __count);
	}
	
	/**
	 * Sets the given integer property.
	 * 
	 * @param __prop The property to set.
	 * @param __iVal The integer value to set.
	 * @throws IllegalArgumentException If the property is not valid.
	 * @since 2020/12/21
	 */
	@Override
	public void set(int __prop, int __iVal)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the given memory handle property.
	 * 
	 * @param __prop The property to set.
	 * @param __memHandle The memory handle to refer to.
	 * @throws IllegalArgumentException If the property is not valid.
	 * @since 2020/12/21
	 */
	@Override
	public void set(int __prop, MemHandle __memHandle)
		throws IllegalArgumentException, NullPointerException
	{
		if (__memHandle == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
