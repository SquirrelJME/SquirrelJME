// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

public abstract class AbstractMap<K, V>
	implements Map<K, V>
{
	protected AbstractMap()
	{
		super();
		throw new Error("TODO");
	}
	
	public abstract Set<Map.Entry<K, V>> entrySet();
	
	public void clear()
	{
		throw new Error("TODO");
	}
	
	@Override
	protected Object clone()
		throws CloneNotSupportedException
	{
		if (false)
			throw new CloneNotSupportedException();
		throw new Error("TODO");
	}
	
	public boolean containsKey(Object __a)
	{
		throw new Error("TODO");
	}
	
	public boolean containsValue(Object __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public V get(Object __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	public boolean isEmpty()
	{
		throw new Error("TODO");
	}
	
	public Set<K> keySet()
	{
		throw new Error("TODO");
	}
	
	public V put(K __a, V __b)
	{
		throw new Error("TODO");
	}
	
	public void putAll(Map<? extends K, ? extends V> __a)
	{
		throw new Error("TODO");
	}
	
	public V remove(Object __a)
	{
		throw new Error("TODO");
	}
	
	public int size()
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public Collection<V> values()
	{
		throw new Error("TODO");
	}
}


