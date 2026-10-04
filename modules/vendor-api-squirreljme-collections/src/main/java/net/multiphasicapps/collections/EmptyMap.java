// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * This is a map which contains no entries.
 *
 * @since 2016/05/12
 */
public class EmptyMap
	extends AbstractMap
{
	/** The empty map. */
	private static Reference<Map> _EMPTY_MAP;
	
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
	 * @param __k
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
		Reference<Map> ref = EmptyMap._EMPTY_MAP;
		Map rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
			EmptyMap._EMPTY_MAP = new WeakReference<>((rv = new EmptyMap()));
		
		// Return it
		return (Map<K, V>)rv;
	}
}

