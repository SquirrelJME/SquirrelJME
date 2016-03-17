// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractList;
import java.util.List;

/**
 * This is a list which cannot be modified.
 *
 * @param <V> The type of value the list stores.
 * @since 2016/03/03
 */
class __UnmodifiableList__<V>
	extends AbstractList<V>
{
	/** The list to wrap. */
	protected final List<V> wrapped;	
	
	/**
	 * Initializes the list which cannot be modified.
	 *
	 * @param __l The list to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/03
	 */
	__UnmodifiableList__(List<V> __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Set
		wrapped = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/03
	 */
	@Override
	public V get(int __i)
	{
		return wrapped.get(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/03
	 */
	@Override
	public int size()
	{
		return wrapped.size();
	}
}

