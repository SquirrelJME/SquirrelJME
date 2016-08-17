// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

/**
 * This contains information on a single entry in the string pool.
 *
 * @since 2016/08/17
 */
class __StringEntry__
{
	/** The index of this entry. */
	final int _index;
	
	/**
	 * Initializes the string entry.
	 *
	 * @param __dx The index to place it at.
	 * @since 2016/08/17
	 */
	__StringEntry__(int __dx)
	{
		// Set
		this._index = __dx;
	}
}
