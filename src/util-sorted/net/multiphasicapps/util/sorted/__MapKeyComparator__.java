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

import java.util.Comparator;

/**
 * This compares map keys.
 *
 * @param <K> The map key.
 * @since 2016/09/07
 */
class __MapKeyComparator__<K>
	implements Comparator<Object>
{
	/** The comparator to use for keys. */
	protected final Comparator<? extends Object> _compare;
	
	/**
	 * Initializes the comparator.
	 *
	 * @param __c The comparator to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/07
	 */
	__MapKeyComparator__(Comparator<? extends Object> __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._compare = __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public int compare(Object __a, Object __b)
	{
		throw new Error("TODO");
	}
}

