// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * This is the base class for stack and local verification type storage.
 *
 * @since 2016/08/28
 */
abstract class __SMTTread__
	extends AbstractList<__SMTType__>
	implements RandomAccess
{
	/** The number of entries in the tread. */
	protected final int count;
	
	/** The variable storage area. */
	protected final __SMTType__[] storage;
	
	/**
	 * Initializes the base tread.
	 *
	 * @param __n The number of entries.
	 * @since 2016/05/12
	 */
	__SMTTread__(int __n)
	{
		// Initialize
		count = __n;
		storage = new __SMTType__[__n];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final __SMTType__ get(int __i)
	{
		return storage[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final int size()
	{
		return count;
	}
}

