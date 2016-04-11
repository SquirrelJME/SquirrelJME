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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is an iterator which is empty.
 *
 * @since 2016/04/10
 */
final class __EmptyIterator__
	implements Iterator
{
	/**
	 * Initializes the empty iterator.
	 *
	 * @sicne 2016/04/10
	 */
	__EmptyIterator__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public boolean hasNext()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public Object next()
	{
		// {@squirreljme.error CX06 The empty iterator contains no elements.}
		throw new NoSuchElementException("XC06");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public void remove()
	{
		// {@squirreljme.error XC05 Cannot remove elements from the empty
		// iterator.}
		throw new UnsupportedOperationException("XC05");
	}
}

