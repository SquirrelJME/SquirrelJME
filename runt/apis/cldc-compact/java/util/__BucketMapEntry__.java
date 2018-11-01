// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This represents a single entry within the bucket map.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2018/10/07
 */
final class __BucketMapEntry__<K, V>
	implements Map.Entry<K, V>
{
	/** The key. */
	final K _key;
	
	/** The key hashcode. */
	final int _keyhash;
	
	/** The value here. */
	V _value;		
	
	/**
	 * Initializes the entry.
	 *
	 * @param __k The key.
	 * @since 2018/10/08
	 */
	__BucketMapEntry__(K __k)
	{
		this._key = __k;
		this._keyhash = (__k == null ? 0 : __k.hashCode());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/08
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof Map.Entry))
			return false;
		
		Map.Entry<?, ?> o = (Map.Entry<?, ?>)__o;
		return Objects.equals(this._key, o.getKey()) &&
			Objects.equals(this._value, o.getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/08
	 */
	@Override
	public final K getKey()
	{
		return this._key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/08
	 */
	@Override
	public final V getValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/08
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/08
	 */
	@Override
	public final V setValue(V __v)
	{
		V rv = this._value;
		this._value = __v;
		return rv;
	}
}

