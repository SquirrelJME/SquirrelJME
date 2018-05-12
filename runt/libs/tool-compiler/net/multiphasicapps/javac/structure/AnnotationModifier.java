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

/**
 * This represents a modifier which is associated with a package, class,
 * member, or field and it represents the value of an annotation.
 *
 * @since 2018/05/07
 */
public final class AnnotationModifier
	implements StructureModifier
{
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/07
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses the input annotation and sets the modifier for it.
	 *
	 * @param __syn The input syntax.
	 * @param __nl The name lookup for annotation types.
	 * @return The output modifier.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException On null arguments.
	 * @since 2018/05/12
	 */
	public static AnnotationModifier parse(AnnotationSyntax __syn,
		NameLookup __nl)
		throws NullPointerException, StructureException
	{
		if (__syn == null || __nl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

