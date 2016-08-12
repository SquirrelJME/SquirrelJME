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
	
	/** This is a flag which indicates that a class is extended. */
	volatile boolean _extended;
	
	/** The indicates that the class is implemented. */
	volatile boolean _implemented;
	
	/**
	 * Initializes the class import.
	 *
	 * @param __i The import table.
	 * @param __n The name of the class being imported.
	 * @since 2016/08/11
	 */
	public __ImportClass__(__Imports__ __i, ClassNameSymbol __n)
		throws NullPointerException
	{
		super(__i);
		
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
}

