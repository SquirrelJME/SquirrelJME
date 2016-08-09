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
 * This represents multiple classes within an output namespace.
 *
 * @since 2016/08/09
 */
class __Classes__
{
	/**
	 * Creates a new class in the output namespace.
	 *
	 * @param __cn The name of the class.
	 * @return The newly created class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	__Class__ __newClass(ClassNameSymbol __cn)
		throws NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// 
		throw new Error("TODO");
	}
}

