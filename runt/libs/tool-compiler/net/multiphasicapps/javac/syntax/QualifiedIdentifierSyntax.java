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

import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a qualified identifier which represents the name of a class.
 *
 * @since 2018/04/10
 */
public final class QualifiedIdentifierSyntax
{
	/** The class this refers to. */
	protected final BinaryName name;
	
	/**
	 * Initializes the qualified identifier.
	 *
	 * @param __bn The identifier name.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/13
	 */
	public QualifiedIdentifierSyntax(BinaryName __bn)
		throws NullPointerException
	{
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		this.name = __bn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/13
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof QualifiedIdentifierSyntax))
			return false;
		
		return this.name.equals(((QualifiedIdentifierSyntax)__o).name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/13
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/13
	 */
	@Override
	public final String toString()
	{
		return this.name.toString();
	}
	
	/**
	 * Parses a qualified identifier.
	 *
	 * @param __t The input token source.
	 * @return The qualified identifier.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the identifier is not valid.
	 * @since 2018/04/13
	 */
	public static final QualifiedIdentifierSyntax parse(BufferedTokenSource __t)
		throws NullPointerException, SyntaxParseException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// The token is always consumed
		Token token = __t.next();
		
		// {@squirreljme.error AQ3g Expected identifier while parsing qualified
		// identifier.
		if (token.type() != TokenType.IDENTIFIER)
			throw new SyntaxParseException(token, "AQ3g");
		
		// Start with initial base
		StringBuilder sb = new StringBuilder();
		sb.append(token.characters());
		
		// Try to keep reading identifiers
		for (;;)
		{
			// Parsing no more
			token = __t.peek();
			if (token.type() != TokenType.SYMBOL_DOT)
				break;
			
			// Consume the dot
			__t.next();
			
			// {@squirreljme.error AQ3h Expected identifier to follow
			// dot in qualified identifier.}
			token = __t.next();
			if (token.type() != TokenType.IDENTIFIER)
				throw new SyntaxParseException(token, "AQ3h");
			
			// Add in
			sb.append('/');
			sb.append(token.characters());
		}
		
		// Might not be valid
		try
		{
			return new QualifiedIdentifierSyntax(new BinaryName(sb.toString()));
		}
		
		// {@squirreljme.error AQ3i The specified identifier is not a valid
		// binary name. (The identifier)}
		catch (InvalidClassFormatException e)
		{
			throw new SyntaxParseException(token,
				String.format("AQ3i %s", sb), e);
		}
	}
	
	/**
	 * Parses a list of qualified identifiers.
	 *
	 * @param __in The input tokens.
	 * @return The list of qualified identifiers.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the list is not valid.
	 * @since 2018/04/28
	 */
	public static final QualifiedIdentifierSyntax[] parseList(
		BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

