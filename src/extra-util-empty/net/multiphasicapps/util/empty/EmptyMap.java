// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.empty;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;
import java.util.Map;

/**
 * This is a map which contains no entries.
 *
 * @since 2016/05/12
 */
public class EmptyMap
	extends AbstractMap
{
	/** The empty map. */
	private static volatile Reference<Map> _EMPTY_MAP;
	
	/**
	 * Initializes the empty map.
	 *
	 * @since 2016/05/12
	 */
	private EmptyMap()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean containsKey(Object __k)
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean containsValue(Object __v)
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Set<Map.Entry> entrySet()
	{
		return EmptySet.<Map.Entry>empty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Object get(Object __k)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Set keySet()
	{
		return EmptySet.<Object>empty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public int size()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Collection values()
	{
		return EmptySet.<Object>empty();
	}
	
	/**
	 * This returns an empty and unmodifiable map.
	 *
	 * @param <K> The key type to use.
	 * @param <V> The value type to use.
	 * @return The unmodifiable and empty map.
	 * @since 2016/05/12
	 */
	@SuppressWarnings({"unchecked"})
	public static <K, V> Map<K, V> empty()
	{
		// Get reference
		Reference<Map> ref = _EMPTY_MAP;
		Map rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
			_EMPTY_MAP = new WeakReference<>((rv = new EmptyMap()));
		
		// Return it
		return (Map<K, V>)rv;
	}
}

