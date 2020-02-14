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

import net.multiphasicapps.javac.syntax.TypeSyntax;

/**
 * This class manages the lookup for names and may recursively lookup what a
 * name means depending on the scope.
 *
 * @since 2018/05/07
 */
@Deprecated
public interface NameLookup
{
	/**
	 * Looks up the specified type syntax and returns the symbol for it.
	 *
	 * @param __ts The type syntax to locate.
	 * @return The symbol for the given type.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If no such symbol exists.
	 * @since 2018/05/10
	 */
	public abstract TypeSymbol lookupType(TypeSyntax __ts)
		throws NullPointerException, StructureException;
}

