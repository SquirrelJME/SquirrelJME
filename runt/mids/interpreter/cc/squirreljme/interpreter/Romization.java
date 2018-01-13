// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.interpreter;

import cc.squirreljme.jit.VerifiedJITInput;

/**
 * This class is used to make SquirrelJME interpreter and Wintercoat ROMs.
 *
 * @since 2017/10/09
 */
public final class Romization
{
	/**
	 * Initializes the romizer.
	 *
	 * @param __vji The input for the JIT.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	public Romization(VerifiedJITInput __vji)
		throws NullPointerException
	{
		if (__vji == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Builds the ROM from the input.
	 *
	 * @return The resulting ROM.
	 * @since 2017/10/09
	 */
	public final byte[] build()
	{
		throw new todo.TODO();
	}
}

