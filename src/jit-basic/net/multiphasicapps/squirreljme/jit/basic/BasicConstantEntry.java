// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

/**
 * This represents an entry within the constant pool.
 *
 * @param <T> The entry type used.
 * @since 2016/09/11
 */
public final class BasicConstantEntry<T>
{
	/** The entry index. */
	protected final int index;
	
	/** The value of this entry. */
	protected final T value;
	
	/**
	 * Initializes the constant entry.
	 *
	 * @param __v The value of the entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	BasicConstantEntry(int __dx, T __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.index = __dx;
		this.value = __v;
	}
	
	/**
	 * Obtains the value.
	 *
	 * @return The value of the entry.
	 * @since 2016/09/11
	 */
	public final T get()
	{
		return this.value;
	}
	
	/**
	 * Returns the index the entry is located at.
	 *
	 * @return The constant entry index.
	 * @since 2016/09/11
	 */
	public final int index()
	{
		return this.index;
	}
}

