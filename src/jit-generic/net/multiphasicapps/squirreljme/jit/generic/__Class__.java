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
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;

/**
 * This represents a single class in the output namespace.
 *
 * @since 2016/08/09
 */
class __Class__
{
	/** The name of this class. */
	protected final ClassNameSymbol name;
	
	/** The class flags. */
	volatile JITClassFlags _flags;
	
	/** The super class. */
	volatile __ImportClass__ _super;
	
	/** The interfaces implemented. */
	volatile __ImportClass__[] _interfaces;
	
	/**
	 * Initializes a class.
	 *
	 * @param __n The name of the class to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	__Class__(ClassNameSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The name of this class.
	 * @since 2016/08/09
	 */
	ClassNameSymbol __className()
	{
		return this.name;
	}
}

