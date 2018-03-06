// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This is the stack map table which is used for verification purposes.
 *
 * @since 2017/10/09
 */
public final class StackMapTable
{
	/** Stack map states. */
	private final Map<Integer, StackMapTableState> _states;
	
	/**
	 * Initializes the stack map table.
	 *
	 * @param __s The input states.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/16
	 */
	StackMapTable(Map<Integer, StackMapTableState> __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this._states = new SortedTreeMap<>(__s);
	}
	
	/**
	 * Returns the stack map table state for the given entry.
	 *
	 * @param __a The address to get.
	 * @return The state for the given address or {@code null} if it there is
	 * no entry.
	 * @since 2017/10/16
	 */
	public StackMapTableState get(int __a)
	{
		return this._states.get(__a);
	}
}

