// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio;

public abstract class Buffer
{
	Buffer()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the array of the buffer.
	 *
	 * @return The array buffer.
	 * @since 2020/03/26
	 */
	@SuppressWarnings("unused")
	abstract Object array();
	
	/**
	 * Does this have an array?
	 *
	 * @return If this has an array.
	 * @since 2020/03/26
	 */
	@SuppressWarnings("unused")
	abstract boolean hasArray();
	
	/**
	 * Returns the offset of the array.
	 *
	 * @return The array offset.
	 * @since 2020/03/26
	 */
	@SuppressWarnings("unused")
	abstract int arrayOffset();
	
	public final int capacity()
	{
		throw new todo.TODO();
	}
	
	public final Buffer clear()
	{
		throw new todo.TODO();
	}
	
	public final Buffer flip()
	{
		throw new todo.TODO();
	}
	
	public final boolean hasRemaining()
	{
		throw new todo.TODO();
	}
	
	public final int limit()
	{
		throw new todo.TODO();
	}
	
	public final Buffer limit(int __a)
	{
		throw new todo.TODO();
	}
	
	public final int position()
	{
		throw new todo.TODO();
	}
	
	public final Buffer position(int __a)
	{
		throw new todo.TODO();
	}
	
	public final int remaining()
	{
		throw new todo.TODO();
	}
	
	public final Buffer rewind()
	{
		throw new todo.TODO();
	}
}


