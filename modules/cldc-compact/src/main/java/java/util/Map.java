// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface Map<K, V>
{
	void clear();
	
	boolean containsKey(Object __a);
	
	boolean containsValue(Object __a);
	
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
	V get(Object __key);
	
	@Override
	int hashCode();
	
	boolean isEmpty();
	
	Set<K> keySet();
	
	V put(K __a, V __b);
	
	void putAll(Map<? extends K, ? extends V> __a);
	
	V remove(Object __a);
	
	int size();
	
	Collection<V> values();
	
	interface Entry<K, V>
	{
		@Api
		@Override
		boolean equals(Object __a);
		
		K getKey();
		
		V getValue();
		
		@Override
		int hashCode();
		
		V setValue(V __a);
	}
}

