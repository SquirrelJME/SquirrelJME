// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

public interface Map<K, V>
{
	void clear();
	
	boolean containsKey(Object __a);
	
	boolean containsValue(Object __a);
	
	Set<Map.Entry<K, V>> entrySet();
	
	@Override
	boolean equals(Object __a);
	
	V get(Object __a);
	
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
		@Override
		boolean equals(Object __a);
		
		K getKey();
		
		V getValue();
		
		@Override
		int hashCode();
		
		V setValue(V __a);
	}
}

