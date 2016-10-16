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

import net.multiphasicapps.squirreljme.classformat.MethodFlags;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * Represents a single method to be virtualized.
 *
 * @since 2016/09/30
 */
class __Method__
	extends __Member__<MethodFlags, MethodSymbol>
{
	/**
	 * Initializes the method information.
	 *
	 * @param __f The method flags.
	 * @param __n The method name.
	 * @param __t The method type.
	 * @since 2016/09/30
	 */
	__Method__(MethodFlags __f, IdentifierSymbol __n, MethodSymbol __t)
	{
		super(__f, __SymbolUtil__.__virtualIdentifier(__n),
			__SymbolUtil__.__virtualMethod(__t));
	}
}

