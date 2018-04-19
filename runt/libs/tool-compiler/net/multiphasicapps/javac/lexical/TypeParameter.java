// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.lexical;

import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a single type parameter.
 *
 * @since 2018/04/10
 */
public final class TypeParameter
{
	/**
	 * {@inheritDoc}
	 * @since 2018/04/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/15
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/15
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses a single type parameter
	 *
	 * @param __t The input token source.
	 * @return The parsed type parameter.
	 * @throws LexicalStructureException If the type parameter is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static final TypeParameter parseTypeParameter(BufferedTokenSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses multiple type parameters
	 *
	 * @param __t The input token source.
	 * @return The parsed type parameters.
	 * @throws LexicalStructureException If the type parameters are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static final TypeParameter[] parseTypeParameters(
		BufferedTokenSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

