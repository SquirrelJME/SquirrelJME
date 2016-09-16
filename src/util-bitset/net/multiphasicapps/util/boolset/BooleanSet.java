// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.boolset;

/**
 * This interface describes a boolean set.
 *
 * Implementations of this interface are not required to be thread safe.
 *
 * @since 2016/09/16
 */
public interface BooleanSet
{
	/**
	 * Sets the specified bit to the given state.
	 *
	 * @param __i The bit to set.
	 * @param __v The state of the bit.
	 * @return The old value of the bit.
	 * @throws IndexOutOfBoundsException If the bit is negative or exceeds
	 * the capacity of the set.
	 * @since 2016/09/16
	 */
	public abstract boolean set(int __i, boolean __v)
		throws IndexOutOfBoundsException;
	
	/**
	 * Returns the number of bits that are in the set.
	 *
	 * @return The number of bits in the set.
	 * @since 2016/09/16
	 */
	public abstract int size();
}

