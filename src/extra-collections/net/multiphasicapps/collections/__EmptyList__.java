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
import java.util.RandomAccess;

/**
 * This is a list which contains no elements.
 *
 * @since 2016/04/10
 */
final class __EmptyList__
	extends AbstractList
	implements RandomAccess
{
	/**
	 * Initializes the empty list.
	 *
	 * @since 2016/04/10
	 */
	__EmptyList__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public Object get(int __i)
	{
		// {@squirreljme.error CX07 The empty list contains no elements.}
		throw new IndexOutOfBoundsException("CX07");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public int size()
	{
		return 0;
	}
}

