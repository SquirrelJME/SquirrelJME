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
 * This represents a generic type with a qualified identifier with potential
 * type bounds.
 *
 * @since 2018/04/30
 */
public final class GenericTypeSyntax
	implements SimpleTypeSyntax
{
	/** The identifier. */
	protected final QualifiedIdentifierSyntax identifier;
	
	/** Is there generic info? */
	protected final boolean hasgenerics;
	
	/**
	 * Initializes the generic type with no generic information.
	 *
	 * @param __id The identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/30
	 */
	public GenericTypeSyntax(QualifiedIdentifierSyntax __id)
		throws NullPointerException
	{
		if (__id == null)
			throw new NullPointerException("NARG");
		
		this.identifier = __id;
		this.hasgenerics = false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/30
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/30
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/30
	 */
	@Override
	public final String toString()
	{
		if (!this.hasgenerics)
			return this.identifier.toString();
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses the generic type.
	 *
	 * @param __in The input token source.
	 * @return The generic type.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the generic type is not valid.
	 * @since 2018/04/30
	 */
	public static GenericTypeSyntax parse(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// There will always be a qualified identifier
		QualifiedIdentifierSyntax ident =
			QualifiedIdentifierSyntax.parse(__in);
		
		// Is there generic type information?
		Token token = __in.peek();
		if (token.type() == TokenType.COMPARE_LESS_THAN)
			throw new todo.TODO();
		
		// Just a plain type
		return new GenericTypeSyntax(ident);
	}
}

