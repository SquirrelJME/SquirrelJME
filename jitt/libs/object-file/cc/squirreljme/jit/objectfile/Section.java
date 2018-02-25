// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

/**
 * This represents a single section within an object file which contains either
 * executable data or program data.
 *
 * @since 2018/02/23
 */
public final class Section
{
	/** The name of this section. */
	protected final String name;
	
	/** The symbol table for sections. */
	protected final SymbolTable symbols;
	
	/**
	 * Initializes the section.
	 *
	 * @param __name The name of the section.
	 * @param __st The symbol table.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	Section(String __name, SymbolTable __st)
		throws NullPointerException
	{
		if (__name == null || __st == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this.symbols = __st;
	}
}

