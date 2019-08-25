// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

/**
 * Represents an entry within the constant pool.
 *
 * @since 2019/07/15
 */
public final class BasicPoolEntry
{
	/** The index of this entry. */
	public final int index;
	
	/** The value. */
	public final Object value;
	
	/** The parts. */
	private final int[] _parts;
	
	/**
	 * Initializes a new entry.
	 *
	 * @param __dx The entry index.
	 * @param __v The value.
	 * @param __parts The parts.
	 * @since 2019/07/15
	 */
	public BasicPoolEntry(int __dx, Object __v, int... __parts)
	{
		this.index = __dx;
		this.value = __v;
		this._parts = (__parts == null ? new int[0] : __parts.clone());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/08/25
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/08/25
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the parts used.
	 *
	 * @return The used parts.
	 * @since 2019/07/15
	 */
	public final int[] parts()
	{
		return this._parts.clone();
	}
}

