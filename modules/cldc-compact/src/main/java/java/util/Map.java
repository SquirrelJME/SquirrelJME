// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface Map<K, V>
{
	@Api
	void clear();
	
	@Api
	boolean containsKey(Object __a);
	
	@Api
	boolean containsValue(Object __a);
	
	@Api
	Set<Map.Entry<K, V>> entrySet();
	
	@Override
	boolean equals(Object __a);
	
	/**
	 * Obtains the key from the given map.
	 * 
	 * @param __key The key to get.
	 * @return The value of the given key or {@code null} if not found.
	 * @since 2022/07/12
	 */
	@Api
	V get(Object __key);
	
	@Override
	int hashCode();
	
	@Api
	boolean isEmpty();
	
	@Api
	Set<K> keySet();
	
	@Api
	V put(K __a, V __b);
	
	@Api
	void putAll(Map<? extends K, ? extends V> __a);
	
	@Api
	V remove(Object __a);
	
	@Api
	int size();
	
	@Api
	Collection<V> values();
	
	@Api
	interface Entry<K, V>
	{
		@Override
		boolean equals(Object __a);
		
		@Api
		K getKey();
		
		@Api
		V getValue();
		
		@Override
		int hashCode();
		
		@Api
		V setValue(V __v);
	}
}

