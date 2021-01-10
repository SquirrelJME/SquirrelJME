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
 * Represents a plain list value handle.
 *
 * @since 2020/12/21
 */
public class ListValueHandle
	extends MemHandle
{
	/** The number of items in this list. */
	protected final int count;
	
	/**
	 * Initializes the base list handle.
	 *
	 * @param __id The memory handle ID.
	 * @param __memActions The memory actions used.
	 * @param __count The size of the list.
	 * @throws IllegalArgumentException If the memory handle does not have the
	 * correct security bits specified or if the count is negative.
	 * @since 2020/12/21
	 */
	ListValueHandle(int __kind, int __id, MemActions __memActions, int __count)
		throws IllegalArgumentException
	{
		super(__kind, __id, __memActions, 4 * __count);
		
		// {@squirreljme.error BC03 Negative list size. (The count}}
		if (__count < 0)
			throw new IllegalArgumentException("BC03 " + __count);
		
		this.count = __count;
	}
	
	/**
	 * Returns the value at the given index.
	 * 
	 * @param __i The index to get.
	 * @return The value at the given index.
	 * @throws IllegalStateException If this is not of the given type.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2020/12/21
	 */
	public int getInteger(int __i)
		throws IllegalStateException, IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the entry at the given index.
	 * 
	 * @param __i The index to set.
	 * @param __iVal The handle to set it to.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @since 2020/12/19
	 */
	public void set(int __i, int __iVal)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__i < 0 || __i >= this.count)
			throw new IndexOutOfBoundsException("IOOB " + __i);
		
		this.memActions.writeInteger(this, __i * 4, __iVal);
	}
	
	/**
	 * Sets the entry at the given index.
	 * 
	 * @param __i The index to set.
	 * @param __handle The handle to set it to.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/19
	 */
	public void set(int __i, MemHandle __handle)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__handle == null)
			throw new NullPointerException("NARG");
		
		if (__i < 0 || __i >= this.count)
			throw new IndexOutOfBoundsException("IOOB " + __i);
		
		this.memActions.writeInteger(this, __i * 4, __handle);
	}
}
