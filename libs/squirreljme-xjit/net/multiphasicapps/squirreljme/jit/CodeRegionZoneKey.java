// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.lang.ref.Reference;

/**
 * This is a zone key which is used to represent a range of byte code within
 * a method.
 *
 * @since 2017/05/29
 */
@Deprecated
public class CodeRegionZoneKey
	extends ZoneKey
{
	/**
	 * Initializes the code region key which wraps around a specific range of
	 * byte code.
	 *
	 * @param __pr The program this refers to.
	 * @param __start The starting point of the region.
	 * @param __end The ending point of the region.
	 * @since 2017/05/29
	 */
	public CodeRegionZoneKey(Reference<ProgramState> __pr, int __start,
		int __end)
	{
		super(__pr);
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/29
	 */
	@Override
	public int compareTo(ZoneKey __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/29
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
}

