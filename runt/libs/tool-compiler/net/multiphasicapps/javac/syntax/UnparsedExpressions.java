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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This class represents expressions which have not been parsed and are just
 * a raw set of tokens.
 *
 * @since 2018/04/28
 */
public final class UnparsedExpressions
{
	/** Tokens which make up the expression. */
	private final Token[] _tokens;
	
	/**
	 * Initializes the unparsed expression.
	 *
	 * @param __t The input tokens.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/29
	 */
	public UnparsedExpressions(Iterable<Token> __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Copy tokens
		List<Token> tokens = new ArrayList<>();
		for (Token t : __t)
		{
			if (t == null)
				throw new NullPointerException("NARG");
			
			tokens.add(t);
		}
		
		// Finalize
		this._tokens = tokens.<Token>toArray(new Token[tokens.size()]);
	}
	
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
	 * Parses a block of expressions to not be parsed.
	 *
	 * @param __in The input tokens.
	 * @return The unparsed expressions.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the block is not valid.
	 * @since 2018/04/28
	 */
	public static UnparsedExpressions parseBlock(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Input tokens
		List<Token> tokens = new LinkedList<>();
		
		// {@squirreljme.error AQ48 Expected start of block to start with an
		// opening brace.}
		Token token = __in.next();
		if (token.type() != TokenType.SYMBOL_OPEN_BRACE)
			throw new SyntaxParseException(token, "AQ48");
		tokens.add(token);
		
		// Just count braces and such
		for (int count = 1; count != 0;)
		{
			token = __in.next();
			
			// Add or remove braces
			if (token.type() == TokenType.SYMBOL_OPEN_BRACE)
				count++;
			else if (token.type() == TokenType.SYMBOL_CLOSED_BRACE)
				count--;
			
			// Add token always
			tokens.add(token);
		}
		
		// Build
		return new UnparsedExpressions(tokens);
	}
}

