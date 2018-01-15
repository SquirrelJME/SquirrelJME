// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This iterates over the sorted set.
 *
 * @since 2016/09/06
 */
class __MapIterator__<K, V>
	implements Iterator<Map.Entry<K, V>>
{
	/** The owning map. */
	protected final SortedTreeMap<K, V> map;
	
	/** The current node position. */
	private volatile __Data__<K, V> _at;
	
	/** The last visited node (for deletion). */
	private volatile __Data__<K, V> _last;
	
	/**
	 * Iterates over the given map.
	 *
	 * @param __m The map to iterate over.
	 * @since 2016/09/06
	 */
	__MapIterator__(SortedTreeMap<K, V> __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.map = __m;
		this._at = __m._min;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean hasNext()
	{
		__Data__<K, V> at = this._at;
		if (at == null)
			return false;
		return (__detect(at) != null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public Map.Entry<K, V> next()
	{
		// {@squirreljme.error AC06 No more elements to iterate over.}
		__Data__<K, V> rv = this._at;
		if (rv == null)
			throw new NoSuchElementException("AC06");
		
		// Make sure the value was not removed
		rv = __detect(rv);
		
		// Store last node (for removal) and iterate to the next node value
		this._last = rv;
		this._at = rv._next;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/25
	 */
	@Override
	public void remove()
	{
		// {@squirreljme.error AC07 No last entry exists for deletion.}
		__Data__<K, V> last = this._last;
		if (last == null)
			throw new IllegalStateException("AC07");
		
		// Remove the entry
		this._last = null;
		this.map.remove(last._key);
	}
	
	/**
	 * Detects if concurrent modification has occured.
	 *
	 * @param __data The data to check.
	 * @return {@code __data}.
	 * @throws ConcurrentModificationException If modification was detected.
	 * @throws NullPointerException On null arguments. 
	 * @since 2017/03/30
	 */
	private final __Data__<K, V> __detect(__Data__<K, V> __data)
		throws ConcurrentModificationException, NullPointerException
	{
		// Check
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AC08 Referenced node was deleted.}
		if (__data._node == null)
			throw new ConcurrentModificationException("AC08");
		
		return __data;
	}
}

