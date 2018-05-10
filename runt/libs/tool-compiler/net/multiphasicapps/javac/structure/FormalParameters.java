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

import net.multiphasicapps.javac.syntax.FormalParametersSyntax;
import net.multiphasicapps.javac.syntax.FormalParameterSyntax;

/**
 * This represents a group of formal parameters.
 *
 * @since 2018/05/10
 */
public final class FormalParameters
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
	 * Parses formal parameters.
	 *
	 * @param __syn The syntax used to make up the formal parameters.
	 * @param __nl The name lookup which is used for types and annotations.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the formal parameters are not correct.
	 * @since 2018/05/10
	 */
	public static FormalParameters parseSyntax(FormalParametersSyntax __syn,
		NameLookup __nl)
		throws NullPointerException, StructureException
	{
		if (__syn == null || __nl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

