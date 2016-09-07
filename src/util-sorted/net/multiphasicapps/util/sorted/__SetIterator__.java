// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.sorted;

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
	private volatile __Node__<K, V> _at;
	
	/** The last visited node (for deletion). */
	private volatile __Node__<K, V> _last;
	
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
		
		// Start at the root node and go all the way left
		__Node__<K, V> rover = __m._root, next;
		if (rover != null)
			while ((next = rover._left) != null)
				rover = next;
		this._at = rover;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean hasNext()
	{
		return (this._at != null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public Map.Entry<K, V> next()
	{
		// {@squirreljme.error CE01 No more elements to iterate over.}
		__Node__<K, V> rv = this._at;
		if (rv == null)
			throw new NoSuchElementException("CE01");
		
		// Move the at pointer to the next node
		__Node__<K, V> rover = rv._right;
		
		// There is a right, just go as deep left as possible
		if (rover != null)
		{
			__Node__<K, V> left;
			while ((left = rover._left) != null)
				rover = left;
		}
		
		// Go to the parent of this node
		else
		{
			rover = rv._parent;
			
			__Node__<K, V> was = rv;
			while (rover != null && (rover._right == was))
			{
				was = rover;
				rover = rover._parent;
			}
		}
		
		// Set next
		this._at = rover;
		
		// Return the value
		this._last = rv;
		
		throw new Error("TODO");
		/*return rv._value;*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public void remove()
	{
		throw new Error("TODO");
	}
}

