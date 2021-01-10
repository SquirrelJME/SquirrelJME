// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Map;

/**
 * Actions for a single {@link MemHandle}.
 *
 * @since 2021/01/10
 */
public final class MemHandleActions
{
	/** Actions at specific addresses. */
	private final Map<Integer, Object> _addresses =
		new SortedTreeMap<>();
	
	/**
	 * Writes the given integer value at the given address.
	 * 
	 * @param __off The offset to write at.
	 * @param __oVal The value to store.
	 * @throws ClassCastException If the input type is not valid.
	 * @throws IllegalArgumentException If the offset is not an aligned
	 * multiple of four.
	 * @throws IndexOutOfBoundsException If the offset is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/10
	 */
	public void writeInteger(int __off, Object __oVal)
		throws ClassCastException, IllegalArgumentException,
			IndexOutOfBoundsException, NullPointerException
	{
		if (__oVal == null)
			throw new NullPointerException("NARG");
		if (__off < 0)
			throw new IndexOutOfBoundsException("IOOB " + __off);
		if ((__off % 4) != 0)
			throw new IllegalArgumentException("ALGN " + __off);
		
		// {@squirreljme.error BC07 Invalid object specified. (The object)}
		if (!((__oVal instanceof Integer) || (__oVal instanceof MemHandle)))
			throw new ClassCastException("BC07 " + __oVal);
		
		// Store into the positional map
		this._addresses.put(__off, __oVal);
	}
}
