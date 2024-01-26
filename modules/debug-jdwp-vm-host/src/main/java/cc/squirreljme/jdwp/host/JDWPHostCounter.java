// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

/**
 * A value counter.
 *
 * @since 2024/01/26
 */
public final class JDWPHostCounter
{
	/** The current count. */
	private volatile int _count;
	
	/**
	 * Decrements the count and returns it.
	 *
	 * @return The resultant count.
	 * @since 2024/01/26
	 */
	public int decrement()
	{
		synchronized (this)
		{
			return --this._count;
		}
	}
	
	/**
	 * Increments the count.
	 *
	 * @return The count to increment.
	 * @since 2024/01/26
	 */
	public int increment()
	{
		synchronized (this)
		{
			int count = this._count;
			this._count = (++count);
			return count;
		}
	}
	
	/**
	 * Returns the current count.
	 *
	 * @return The current count.
	 * @since 2024/01/26
	 */
	public int query()
	{
		synchronized (this)
		{
			return this._count;
		}
	}
}
