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
import java.util.NoSuchElementException;

/**
 * This iterates over the sorted set.
 *
 * @since 2016/09/06
 */
class __SetIterator__<V>
	implements Iterator<V>
{
	/** The owning set. */
	protected final SortedTreeSet<V> set;
	
	/** The current node position. */
	private volatile __Node__<V> _at;
	
	/** The last visited node (for deletion). */
	private volatile __Node__<V> _last;
	
	/**
	 * Iterates over the given set.
	 *
	 * @param __s The set to iterate over.
	 * @since 2016/09/06
	 */
	__SetIterator__(SortedTreeSet<V> __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.set = __s;
		
		// Start at the root node and go all the way left
		__Node__<V> rover = __s._root, next;
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
	public V next()
	{
		// {@squirreljme.error CE01 No more elements to iterate over.}
		__Node__<V> rv = this._at;
		if (rv == null)
			throw new NoSuchElementException("CE01");
		
		// Move the at pointer to the next node
		__Node__<V> rover = rv;
		if (rover != null)
			for (;;)
			{
				__Node__<V> par = rover._parent;
				if (par != null)
				{
					// Need right side
					__Node__<V> right = par._right;
					
					// If this is on the left side, go to the right if there
					// is a right
					if (par._left == rover && right != null)
						rover = par._right;
				
					// If there is not right, go up
					// If this is on the right then go up
					else if (right == null || right == rover)
						rover = par;
				}
		
				// No more elements to iterate over
				else
				{
					rover = null;
					break;
				}
			}
		
		// Set next
		this._at = rover;
		
		// Return the value
		this._last = rv;
		return rv._value;
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

