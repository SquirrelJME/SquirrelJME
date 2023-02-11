// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

/**
 * Represents a statistic.
 *
 * @since 2022/09/06
 */
public final class Statistic
{
	/** The current statistic. */
	private volatile int _current;
	
	/**
	 * Returns the current statistic value.
	 * 
	 * @return The current value.
	 * @since 2022/09/06
	 */
	public int get()
	{
		synchronized (this)
		{
			return this._current;
		}
	}
	
	/**
	 * Increments the given statistic.
	 * 
	 * @param __by The amount to increase by.
	 * @throws IllegalArgumentException If the given value is negative.
	 * @since 2022/09/06
	 */
	public void increment(int __by)
		throws IllegalArgumentException
	{
		if (__by < 0)
			throw new IllegalArgumentException("ILLA");
		
		synchronized (this)
		{
			int current = this._current;
			int next = current + __by;
			
			// Cap at maximum integer value
			this._current = (next < current ? Integer.MAX_VALUE : next);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/06
	 */
	@Override
	public String toString()
	{
		synchronized (this)
		{
			return Integer.toString(this._current);
		}
	}
}
