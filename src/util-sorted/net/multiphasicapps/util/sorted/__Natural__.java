// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.sorted;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Comparator;

/**
 * This is used to compare two values using their natural comparison.
 *
 * @since 2016/09/06
 */
final class __Natural__<V>
	implements Comparator<V>
{
	/** The single instance. */
	private static volatile Reference<__Natural__> _REF;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public int compare(V __a, V __b)
	{
		// Consider two nulls to be equal
		boolean na = (__a == null), nb = (__b == null);
		if (na && nb)
			return 0;
		
		// Nulls before non-null
		else if (na && !nb)
			return -1;
		else if (!na && nb)
			return 1;
		
		// Use standard comparison
		return ((Comparable<V>)__a).compareTo(__b);
	}
	
	/**
	 * Returns the natural comparator instance.
	 *
	 * @param <V> The type of value to compare.
	 * @return The instance of the natural comparator.
	 * @since 2016/09/06
	 */
	@SuppressWarnings({"unchecked"})
	public static final <V> __Natural__<V> instance()
	{
		Reference<__Natural__> ref = _REF;
		__Natural__ rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			_REF = new WeakReference<>((rv = new __Natural__()));
		
		// Return it
		return (__Natural__<V>)rv;
	}
}

