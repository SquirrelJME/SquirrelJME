// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

/**
 * This class contains the structure information.
 *
 * @since 2018/05/05
 */
public final class Structures
{
	/**
	 * Checks if the loaded structure contains a symbol for the given class.
	 *
	 * @param __sym The symbol to lookup.
	 * @return If the symbol exists in the structure.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/05
	 */
	public final boolean contains(StructureSymbol __sym)
		throws NullPointerException
	{
		if (__sym == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

