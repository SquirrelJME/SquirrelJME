// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This represents a basic constant pool.
 *
 * @since 2019/07/17
 */
public final class BasicPool
{
	/** Entries which exist in the constant pool. */
	private final Map<Object, BasicPoolEntry> _entries;
	
	/** Linear entries within the pool. */
	private final List<BasicPoolEntry> _linear;
	
	/**
	 * Input for the basic pool.
	 *
	 * @param __it The input entries.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/07
	 */
	public BasicPool(Iterable<BasicPoolEntry> __it)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Gets the pool entry by index.
	 *
	 * @param __i The index to get.
	 * @return The index of the given entry.
	 * @throws IndexOutOfBoundsException If the entry is not within bounds.
	 * @since 2019/09/11
	 */
	public final BasicPoolEntry byIndex(int __i)
		throws IndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
}

