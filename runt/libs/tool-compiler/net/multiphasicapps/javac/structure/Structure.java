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
 * This interface represents structure representation.
 *
 * @since 2018/05/07
 */
public interface Structure
{
	/**
	 * Returns the symbol for the structure.
	 *
	 * @return The structure symbol.
	 * @since 2018/05/07
	 */
	public abstract StructureSymbol symbol();
}

