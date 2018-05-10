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

import net.multiphasicapps.javac.syntax.UnparsedExpressions;

/**
 * This represents the structure of a class constructor.
 *
 * @since 2018/05/08
 */
public final class ClassConstructorStructure
	implements ClassMemberStructure
{
	/**
	 * Initializes the class constructor structure.
	 *
	 * @param __mods The modifiers to the constructor.
	 * @param __tparms The type parameters used.
	 * @param __parms The formal parameters used.
	 * @param __thrown The thrown types.
	 * @param __code Method code that should exist for the constructor.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the sturcture is not valid.
	 * @since 2018/05/10
	 */
	public ClassConstructorStructure(StructureModifiers __mods,
		TypeParameters __tparms, FormalParameters __parms,
		Iterable<TypeSymbol> __thrown, UnparsedExpressions __code)
		throws NullPointerException, StructureException
	{
		if (__mods == null || __tparms == null || __parms == null ||
			__thrown == null || __code == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/08
	 */
	@Override
	public final ClassConstructorSymbol symbol()
	{
		throw new todo.TODO();
	}
}

