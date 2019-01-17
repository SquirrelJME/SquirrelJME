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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class contains the structure information.
 *
 * @since 2018/05/05
 */
@Deprecated
public final class Structures
{
	/** Mapping of symbols to structures. */
	private final Map<StructureSymbol, Structure> _structures =
		new LinkedHashMap<>();
	
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
		
		return this._structures.containsKey(__sym);
	}
	
	/**
	 * Puts the specified structure into the structure map.
	 *
	 * @param __s The structure to put.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the structure already exists.
	 * @since 2018/05/07
	 */
	public final void put(Structure __s)
		throws NullPointerException, StructureException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ10 Structure has no symbol.}
		StructureSymbol sym = __s.symbol();
		if (sym == null)
			throw new NullPointerException("AQ10");
		
		// {@squirreljme.error AQ11 A structure already exists with the
		// given symbol. (The symbol)}
		Map<StructureSymbol, Structure> structures = this._structures;
		if (structures.containsKey(sym))
			throw new StructureException(String.format("AQ11 %s", sym));
		
		// Associate that structure with this symbol
		structures.put(sym, __s);
	}
}

