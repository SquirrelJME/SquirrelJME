// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.singularexport;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This class contains utilities for symbols.
 *
 * @since 2016/09/30
 */
class __SymbolUtil__
{
	/**
	 * Virtualizes the specified class name.
	 *
	 * @param __n The class to virtualize.
	 * @return The virtualized class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/30
	 */
	static ClassNameSymbol __virtualClass(ClassNameSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Virtualize it
		return ClassNameSymbol.of("$squirreljme$/" + __n);
	}
}

