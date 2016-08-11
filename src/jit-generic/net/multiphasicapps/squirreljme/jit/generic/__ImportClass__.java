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

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * Represents an import of a single class.
 *
 * @since 2016/08/11
 */
class __ImportClass__
	extends __Import__
{
	/** The name of the class to import. */
	protected final ClassNameSymbol name;
	
	/**
	 * Initializes the class import.
	 *
	 * @since 2016/08/11
	 */
	public __ImportClass__(ClassNameSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
}

