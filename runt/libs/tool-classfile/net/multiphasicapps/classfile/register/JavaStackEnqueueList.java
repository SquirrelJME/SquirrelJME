// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This contains every register which can be cleared after the specified
 * operation completes. Enqueue lists are always sorted from lowest to
 * highest.
 *
 * This class is immutable.
 *
 * @since 2019/03/30
 */
public final class JavaStackEnqueueList
{
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns if this is empty.
	 *
	 * @return If this is empty.
	 * @since 2019/03/30
	 */
	public final boolean isEmpty()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns all of the registers that have been enqueued.
	 *
	 * @return The enqueued set of registers.
	 * @since 2019/03/30
	 */
	public final int[] registers()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the top-most entry.
	 *
	 * @return The top most entry or {@code -1} if this is empty.
	 * @since 2019/03/30
	 */
	public final int top()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Trims the top entry from the enqueue list and returns the new list
	 * with the top missing.
	 *
	 * @return The resulting list.
	 * @since 2019/03/30
	 */
	public final JavaStackEnqueueList trimTop()
	{
		if (this.isEmpty())
			return this;
		
		throw new todo.TODO();
	}
}

