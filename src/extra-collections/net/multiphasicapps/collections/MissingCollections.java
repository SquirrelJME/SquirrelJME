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

import java.util.Map;
import java.util.Set;

/**
 * This class contains helper methods for methods that exist in the desktop
 * {@link java.util.Collections} but ones for which are missing in Java ME.
 *
 * @since 2016/02/28
 */
public class MissingCollections
{
	/**
	 * Not initialized.
	 *
	 * @since 2016/02/28
	 */
	private MissingCollections()
	{
	}
	
	/**
	 * This creates a view of the specified map which cannot be modified.
	 *
	 * @param <K> The map key.
	 * @param <V> The map value.
	 * @param __m The map to wrap.
	 * @return An unmodifiable view of the map.
	 * @since 2016/02/28
	 */
	public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> __m)
	{
		// If already one, return it
		if (__m instanceof __UnmodifiableMap__)
			return __m;
		
		// Wrap
		return new __UnmodifiableMap__<K, V>(__m);
	}
	
	/**
	 * This creates a view of the specified set which cannot be modified.
	 *
	 * @param <T> The type of value the set stores.
	 * @param __s The set to wrap to disable modifications of.
	 * @return An unmodifiable view of the set.
	 * @since 2016/02/28
	 */
	public static <T> Set<T> unmodifiableSet(Set<T> __s)
	{
		// If already one, return that set
		if (__s instanceof __UnmodifiableSet__)
			return __s;
		
		// Otherwise create a new one
		return new __UnmodifiableSet__<T>(__s);
	}
}

