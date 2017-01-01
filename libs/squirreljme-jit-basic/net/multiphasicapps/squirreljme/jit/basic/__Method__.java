// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import net.multiphasicapps.squirreljme.classformat.MethodFlags;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This class stores information about methods.
 *
 * @since 2016/09/14
 */
@Deprecated
class __Method__
	extends __Member__<MethodFlags, MethodSymbol>
{
	/** The code to use in the given class. */
	volatile int _codedx =
		-1;
	
	/**
	 * Initializes the method information.
	 *
	 * @param __f The method flags.
	 * @param __n The method name.
	 * @param __t The method type.
	 * @since 2016/09/14
	 */
	__Method__(MethodFlags __f, BasicConstantEntry<IdentifierSymbol> __n,
		BasicConstantEntry<MethodSymbol> __t)
	{
		super(__f, __n, __t);
	}
}

