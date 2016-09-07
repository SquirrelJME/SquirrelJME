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
	
	/** Dipping to the right after an up? */
	private volatile boolean _dipright;
	
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
		__Node__<V> rover = rv, mod = rover;
		boolean dipright = this._dipright;
		
		// Dipping right?
		if (dipright)
		{
			// Go right
			rover = rover._right;
			
			// Go deep left
			__Node__<V> left;
			while ((left = rover._left) != null)
				rover = left;
		}
		
		// Go into the parent
		else
		{
			__Node__<V> par = rover._parent;
			
			// Top of tree, stop
			if (par == null)
				rover = null;
			
			// We are the right side, skip to the parent above this one and
			// then try going right
			else if (par._right == rover)
				throw new Error("TODO");
			
			// If on the left side go to the parent and then dip later
			else
			{
				rover = par;
				this._dipright = true;
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

