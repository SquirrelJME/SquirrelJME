// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a single annotation.
 *
 * @since 2018/04/21
 */
public final class AnnotationSyntax
	implements ModifierSyntax
{
	/**
	 * {@inheritDoc}
	 * @since 2018/04/21
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/21
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/21
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This parses a single annotation.
	 *
	 * @param __in The input token source.
	 * @return The parsed annotation.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the annotation is not valid.
	 * @since 2018/04/21
	 */
	public static AnnotationSyntax parse(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

