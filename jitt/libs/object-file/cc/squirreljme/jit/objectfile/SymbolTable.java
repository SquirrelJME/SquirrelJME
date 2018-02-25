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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This represents the symbol table which stores imported and exported
 * symbols.
 *
 * @since 2018/02/24
 */
public final class SymbolTable
{
	/** Imported symbols. */
	private final Set<ImportedSymbol> _imports =
		new LinkedHashSet<>();
	
	/** Exported symbols. */
	private final Set<ExportedSymbol> _exports =
		new LinkedHashSet<>();
	
	/**
	 * Internally initialized.
	 *
	 * @since 2018/02/24
	 */
	SymbolTable()
	{
	}
}

