// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.AbstractMap;
import java.util.Set;

/**
 * An enumeration based map.
 *
 * @since 2021/03/13
 */
public final class EnumTypeMap<E extends Enum<E>, V>
	extends AbstractMap<E, V>
{
	/** The type of value to store. */
	protected final Class<E> type;
	
	/** The stored keys. */
	private final E[] _keys;
	
	/** Stored values. */
	private final Object[] _values;
	
	/**
	 * Initializes the enum type map.
	 * 
	 * @param __type The type used.
	 * @param __keys Enumeration keys.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/13
	 */
	public EnumTypeMap(Class<E> __type, E... __keys)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this._keys = __keys.clone();
		this._values = new Object[__keys.length];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public boolean containsKey(Object __key)
	{
		// Not this type, so it will never exist
		if (__key == null || !this.type.isInstance(__key))
			return false;
		
		// Has the key been set?
		for (E key : this._keys)
			if (key == __key)
				return true;
		
		// Does not exist otherwise
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public Set<Entry<E, V>> entrySet()
	{
		return new __ArrayEntrySet__<E, V>(this._keys, this._values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 * @param __key
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V get(Object __key)
	{
		if (!this.type.isInstance(__key))
			return null;
		
		Enum<?> key = (Enum<?>)__key;
		
		return (V)this._values[key.ordinal()];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V put(E __key, V __value)
	{
		/* {@squirreljme.error ZZ1i Cannot store key into map.} */ 
		if (!this.type.isInstance(__key))
			throw new IllegalArgumentException("ZZ1i");
		
		Object[] values = this._values;
		Object old = values[__key.ordinal()];
		values[__key.ordinal()] = __value;
		return (V)old;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V remove(Object __key)
	{
		if (!this.type.isInstance(__key))
			return null;
		
		Enum<?> key = (Enum<?>)__key;
		
		Object[] values = this._values;
		Object old = values[key.ordinal()];
		values[key.ordinal()] = null;
		return (V)old;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public int size()
	{
		return this._values.length;
	}
}
