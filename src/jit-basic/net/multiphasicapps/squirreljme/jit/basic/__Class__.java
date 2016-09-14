// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import net.multiphasicapps.squirreljme.classformat.ClassFlags;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This represents a class which was written.
 *
 * @since 2016/09/11
 */
class __Class__
	extends __Positioned__
{
	/** The name of the class. */
	protected final BasicConstantEntry<ClassNameSymbol> name;
	
	/** Flags for the current class. */
	volatile ClassFlags _flags;
	
	/** The super class, which is optional (Object has none). */
	volatile BasicConstantEntry<ClassNameSymbol> _super;
	
	/**
	 * Initializes the class reference.
	 *
	 * @param __n The name of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	__Class__(BasicConstantEntry<ClassNameSymbol> __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
}

