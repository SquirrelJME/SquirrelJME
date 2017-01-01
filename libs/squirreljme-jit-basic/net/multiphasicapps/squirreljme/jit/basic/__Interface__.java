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

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This represents a single interface that is implemented.
 *
 * @since 2016/09/14
 */
@Deprecated
class __Interface__
{
	/** The implemented interface. */
	final BasicConstantEntry<ClassNameSymbol> _interface;
	
	/**
	 * Initializes the interface reference.
	 *
	 * @param __i The interface to implement.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	__Interface__(BasicConstantEntry<ClassNameSymbol> __i)
		throws NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._interface = __i;
	}
}

