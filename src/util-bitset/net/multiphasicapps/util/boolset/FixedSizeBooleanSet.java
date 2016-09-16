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
 * This is a {@link BooleanSet} which is of a fixed size, it only permits
 * reading and writing from bits that are valid within the set.
 *
 * @since 2016/09/16
 */
public class FixedSizeBooleanSet
	implements BooleanSet
{
	/**
	 * Initializes the fixed size boolean set.
	 *
	 * @param __n The number of bits to store.
	 * @throws IndexOutOfBoundsException If the boolean is out of bounds.
	 * @since 2016/09/16
	 */
	public FixedSizeBooleanSet(int __n)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error CH01 Cannot have a boolean set of a negative
		// size.}
		if (__n < 0)
			throw new IndexOutOfBoundsException("CH01");
		
		throw new Error("TODO");
	}
}

