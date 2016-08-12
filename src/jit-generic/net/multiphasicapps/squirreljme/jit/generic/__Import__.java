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
 * This represents a single class, method, field, or string which has been
 * imported.
 *
 * @since 2016/08/09
 */
abstract class __Import__
{
	/** The placement index of the import. */
	final int _index;
	
	/**
	 * Initializes the single import.
	 *
	 * @param __i The import table.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	__Import__(__Imports__ __i)
		throws NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._index = __i.__declareImport(this);
	}
}

