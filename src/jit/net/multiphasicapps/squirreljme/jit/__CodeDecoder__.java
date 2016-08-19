// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITMethodFlags;

/**
 * This class is used to decode the actual code attribute in the method
 * along with any of its attributes.
 *
 * @since 2016/08/19
 */
final class __CodeDecoder__
{
	/**
	 * Add base code decoder class.
	 *
	 * @param __cd The class decoder being used.
	 * @param __dis The input source.
	 * @param __f The method flags.
	 * @param __t The method type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	__CodeDecoder__(__ClassDecoder__ __cd, DataInputStream __dis,
		JITMethodFlags __f, MethodSymbol __t)
		throws NullPointerException
	{
		// Check
		if (__cd == null || __dis == null || __f == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

