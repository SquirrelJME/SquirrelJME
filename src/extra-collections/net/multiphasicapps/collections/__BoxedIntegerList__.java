// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * This wraps an integer array and provides boxes access to getting and
 * setting of values.
 *
 * @since 2016/04/11
 */
final class __BoxedIntegerList__
	extends AbstractList<Integer>
	implements RandomAccess
{
	/** The original primitive array. */
	protected final int[] primitives;
	
	/**
	 * Initializes the boxed list.
	 *
	 * @param __v The values to box.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/11
	 */
	__BoxedIntegerList__(int... __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Set
		primitives = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/11
	 */
	@Override
	public Integer get(int __i)
	{
		return primitives[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/11
	 */
	@Override
	public Integer set(int __i, Integer __e)
	{
		// Get old value
		int old = primitives[__i];
		
		// Set new value
		primitives[__i] = __e.intValue();
		
		// Return old
		return old;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/11
	 */
	@Override
	public int size()
	{
		return primitives.length;
	}
}

