// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A set which is unmodifiable and contains only a single item.
 *
 * @param <T> The type of value this stores.
 * @since 2023/08/21
 */
final class __SingletonSet__<T>
	extends AbstractSet<T>
{
	/** The stored value. */
	private final T _value;
	
	/**
	 * Initializes the singleton set.
	 *
	 * @param __value The value to store.
	 * @since 2023/08/21
	 */
	__SingletonSet__(T __value)
	{
		this._value = __value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public boolean contains(Object __v)
	{
		// Is there no value in the set?
		T value = this._value;
		if (value == null)
			return __v == null;
		
		// Otherwise if the single value is equal
		return value.equals(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator()
	{
		return UnmodifiableIterator.<T>of(this._value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/21
	 */
	@Override
	public int size()
	{
		return 1;
	}
}
