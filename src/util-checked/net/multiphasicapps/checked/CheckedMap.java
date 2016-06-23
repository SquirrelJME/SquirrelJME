// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.checked;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This is a checked map which makes sure that when values are set to it that
 * they are of the given class type.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2016/05/08
 */
public class CheckedMap<K, V>
	extends AbstractMap<K, V>
{
	/**
	 * Internal checked map initialization.
	 *
	 * @since 2016/05/08
	 */
	private CheckedMap()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/20
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns a checked map where inserted keys and values must be of the
	 * given class type or a super-class of it.
	 *
	 * @param <K> The key type.
	 * @param <V> The value type.
	 * @param __base The map to wrap.
	 * @param __k The key class, may be {@code null} if not checked.
	 * @param __v The value class, may be {@code null} if not checked.
	 * @return A map with the specified checks.
	 * @throws NullPointerException If no map was specified.
	 * @since 2016/05/08
	 */
	public static <K, V> CheckedMap<? super K, ? super V> contravariant(
		Map<K, V> __base, Class<K> __k, Class<V> __v)
		throws NullPointerException
	{
		// Check
		if (__base == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns a checked map where inserted keys and values must be an instance
	 * of the given classes.
	 *
	 * @param <K> The key type.
	 * @param <V> The value type.
	 * @param __base The map to wrap.
	 * @param __k The key class, may be {@code null} if not checked.
	 * @param __v The value class, may be {@code null} if not checked.
	 * @return A map with the specified checks.
	 * @throws NullPointerException If no map was specified.
	 * @since 2016/05/08
	 */
	public static <K, V> CheckedMap<K, V> covariant(Map<K, V> __base,
		Class<K> __k, Class<V> __v)
		throws NullPointerException
	{
		// Check
		if (__base == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns a checked map where inserted keys and values must exactly be
	 * the given type.
	 *
	 * If interfaces or abstract classes are used for the key and/or value then
	 * keys/values cannot be placed into the map because they can never have
	 * an object which is of that exact class.
	 *
	 * @param <K> The key type.
	 * @param <V> The value type.
	 * @param __base The map to wrap.
	 * @param __k The key class, may be {@code null} if not checked.
	 * @param __v The value class, may be {@code null} if not checked.
	 * @return A map with the specified checks.
	 * @throws NullPointerException If no map was specified.
	 * @since 2016/05/08
	 */
	public static <K, V> CheckedMap<K, V> exact(Map<K, V> __base, Class<K> __k,
		Class<V> __v)
		throws NullPointerException
	{
		// Check
		if (__base == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

