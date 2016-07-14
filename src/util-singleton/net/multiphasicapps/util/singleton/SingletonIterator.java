// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.singleton;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is an iterator which contains only a single entry, once this is
 * iterated
 *
 * The iterator is read-only.
 *
 * @param <V> The value to iterate over.
 * @since 2016/05/12
 */
public final class SingletonIterator<V>
	implements Iterator<V>
{
	/** The iterator value. */
	private volatile V _value;
	
	/** The current read state. */
	private volatile int _state;
	
	/**
	 * Initializes the single iteration sequence.
	 *
	 * @param __v The value to return.
	 * @since 2016/05/12
	 */
	public SingletonIterator(V __v)
	{
		_value = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean hasNext()
	{
		return _state == 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public V next()
	{
		// End of the iteration?
		int s = _state;
		if (s != 0)
			throw new NoSuchElementException("NSEE");
		
		// Set next state
		_state = 1;
		
		// Get and clear value so it gets GCed
		V rv = _value;
		_value = null;
		
		// Return the vlaue
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("RORO");
	}
}

