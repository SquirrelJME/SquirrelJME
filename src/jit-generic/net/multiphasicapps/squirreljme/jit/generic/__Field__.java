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

import net.multiphasicapps.squirreljme.classformat.FieldFlags;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;

/**
 * This represents a single field.
 *
 * @since 2016/08/18
 */
class __Field__
	extends __Member__
{
	/** The index of the constant. */
	final int _cvdx;
	
	/**
	 * Initializes the field.
	 *
	 * @param __gcw The owning class writer.
	 * @param __f The flags for the field.
	 * @param __name The name of the field.
	 * @param __type The type of the field.
	 * @param __cv The field constant value, may be {@code null} if there is
	 * none.
	 * @since 2016/08/18
	 */
	__Field__(GenericClassWriter __gcw, FieldFlags __f,
		IdentifierSymbol __name, FieldSymbol __type, Object __cv)
	{
		super(__gcw, __f, __name, __type);
		
		// Create constant
		if (__cv != null)
		{
			// It is always placed in the global pool
			GenericPool gpool = __gcw._gpool;
			
			// Create
			this._cvdx = gpool.__loadObject(false, __cv)._index;
		}
		
		// Do not use one
		else
			this._cvdx = 0;
	}
}

