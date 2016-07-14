// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.boxed;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;
import net.multiphasicapps.util.empty.EmptyList;

/**
 * This wraps an integer array and provides boxes access to getting and
 * setting of values.
 *
 * @since 2016/04/11
 */
public final class BoxedIntegerList
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
	private BoxedIntegerList(int... __v)
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
	
	/**
	 * Makes a boxed wrapped around a given list.
	 *
	 * @param __v The list to wrap boxed values for.
	 * @return The wrapped array as boxed values or the empty list if the
	 * input array is {@code null} or is empty.
	 * @since 2016/04/11
	 */
	public static List<Integer> of(int... __v)
	{
		if (__v == null || __v.length <= 0)
			return EmptyList.<Integer>empty();
		return new BoxedIntegerList(__v);
	}
}

