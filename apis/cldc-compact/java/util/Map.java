// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

public interface Map<K, V>
{
	public abstract void clear();
	
	public abstract boolean containsKey(Object __a);
	
	public abstract boolean containsValue(Object __a);
	
	public abstract Set<Map.Entry<K, V>> entrySet();
	
	@Override
	public abstract boolean equals(Object __a);
	
	public abstract V get(Object __a);
	
	@Override
	public abstract int hashCode();
	
	public abstract boolean isEmpty();
	
	public abstract Set<K> keySet();
	
	public abstract V put(K __a, V __b);
	
	public abstract void putAll(Map<? extends K, ? extends V> __a);
	
	public abstract V remove(Object __a);
	
	public abstract int size();
	
	public abstract Collection<V> values();
	
	public static interface Entry<K, V>
	{
		@Override
		public abstract boolean equals(Object __a);
		
		public abstract K getKey();
		
		public abstract V getValue();
		
		@Override
		public abstract int hashCode();
		
		public abstract V setValue(V __a);
	}
}

