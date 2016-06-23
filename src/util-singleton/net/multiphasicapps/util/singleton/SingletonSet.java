// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.singleton;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * This is a set which contains a single entry and cannot be modified.
 *
 * @param <V> The type of value to store.
 * @since 2016/05/12
 */
public final class SingletonSet<V>
	extends AbstractSet<V>
{
	/** The single value to store. */
	protected final V value;
	
	/**
	 * Initializes the single value storing set.
	 *
	 * @param __v The value to store.
	 * @since 2016/05/12
	 */
	public SingletonSet(V __v)
	{
		// Set
		value = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean contains(Object __o)
	{
		return Objects.equals(value, __o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean isEmpty()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Iterator<V> iterator()
	{
		return new SingletonIterator<>(value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public int size()
	{
		return 1;
	}
}

