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

import net.multiphasicapps.javac.syntax.AnnotationSyntax;
import net.multiphasicapps.javac.syntax.ModifiersSyntax;
import net.multiphasicapps.javac.syntax.ModifierSyntax;

/**
 * Represents modifiers that may be associated with a structure.
 *
 * @since 2018/05/10
 */
public final class StructureModifiers
{
	/**
	 * {@inheritDoc}
	 * @since 2018/05/10
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/10
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/10
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses the given modifiers syntax and builds modifier structures from
	 * them.
	 *
	 * @param __syn The input syntax.
	 * @param __nl The lookup for names (used for annotation).
	 * @return The parsed modifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the modifiers are not valid.
	 * @since 2018/05/10
	 */
	public static StructureModifiers parse(ModifiersSyntax __syn,
		NameLookup __nl)
		throws NullPointerException, StructureException
	{
		if (__syn == null || __nl == null)
			throw new NullPointerException("NARG");
		
		// Go through all modifiers
		for (ModifierSyntax mod : __syn)
		{
			if (mod instanceof AnnotationSyntax)
				throw new todo.TODO();
			
			throw new todo.TODO();
		}
		
		throw new todo.TODO();
	}
}

