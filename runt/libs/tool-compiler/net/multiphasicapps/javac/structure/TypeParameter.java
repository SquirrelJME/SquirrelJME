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

import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a single type parameter which may be associated with a
 * class or method.
 *
 * @since 2018/04/24
 */
public final class TypeParameter
{
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses a single type parameter.
	 *
	 * @param __in The input tokens.
	 * @return The parsed type parameter.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the type parameter could not be
	 * parsed.
	 * @since 2018/04/24
	 */
	public static TypeParameter parseTypeParameter(BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses a list of multiple type parameters.
	 *
	 * @param __in The input tokens.
	 * @return The parsed type parameters.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the type parameters could not be
	 * parsed.
	 * @since 2018/04/24
	 */
	public static TypeParameter[] parseTypeParameters(
		BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

