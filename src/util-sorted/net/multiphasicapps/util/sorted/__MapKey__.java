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

/**
 * This is a key that is used within the map.
 *
 * @since 2016/09/07
 */
class __MapKey__<K>
{
	/** The key value. */
	protected final K key;
	
	/** The mapping value. */
	volatile Object _value;
	
	/**
	 * Initializes the map key.
	 *
	 * @param __k The key to use.
	 * @since 2016/09/07
	 */
	__MapKey__(K __k)
	{
		// Set
		this.key = __k;
	}
}

