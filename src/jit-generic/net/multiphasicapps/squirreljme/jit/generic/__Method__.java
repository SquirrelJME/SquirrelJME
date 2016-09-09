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

import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.ClassMethodFlags;
import net.multiphasicapps.squirreljme.jit.ClassMethodReference;

/**
 * This represents a single method.
 *
 * @since 2016/08/18
 */
class __Method__
	extends __Member__
{
	/** The method reference for this method. */
	final ClassMethodReference _mref;
	
	/** The start position of code. */
	volatile int _codestart;
	
	/** The end position of code. */
	volatile int _codeend;
	
	/**
	 * Initializes the method.
	 *
	 * @param __gcw The owning class writer.
	 * @param __f The flags for the method.
	 * @param __name The name of the method.
	 * @param __type The type of the method.
	 * @since 2016/08/18
	 */
	__Method__(GenericClassWriter __gcw, ClassMethodFlags __f,
		IdentifierSymbol __name, MethodSymbol __type)
	{
		super(__gcw, __f, __name, __type);
		
		// Set used reference
		this._mref = new ClassMethodReference(__gcw._classname, __name,
			__type, false);
	}
}

