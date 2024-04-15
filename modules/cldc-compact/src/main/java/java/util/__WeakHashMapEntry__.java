// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Wraps weak hash map entry keys.
 *
 * @param <K> The key type.
 * @param <V> The value stored.
 * @since 2023/02/09
 */
final class __WeakHashMapEntry__<K, V>
	implements Map.Entry<K, V>
{
	/** The entry used. */
	private final Map.Entry<__WeakHashMapKey__<K>, V> _entry;
	
	/** The key used. */
	private final K _key;
	
	/**
	 * Initializes the entry wrapper.
	 * 
	 * @param __key The key used.
	 * @param __entry The entry used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/09
	 */
	__WeakHashMapEntry__(K __key, Map.Entry<__WeakHashMapKey__<K>, V> __entry)
		throws NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		this._key = __key;
		this._entry = __entry;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof Map.Entry))
			return false;
		
		Map.Entry<?, ?> o = (Map.Entry<?, ?>)__o;
		return Objects.equals(this.getKey(), o.getKey()) &&
			Objects.equals(this.getValue(), o.getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public final int hashCode()
	{
		K key = this.getKey();
		V value = this.getValue();
		return (key == null ? 0 : key.hashCode()) ^
			(value == null ? 0 : value.hashCode());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public K getKey()
	{
		return this._key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public V getValue()
	{
		return this._entry.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public V setValue(V __v)
	{
		return this._entry.setValue(__v);
	}
}
