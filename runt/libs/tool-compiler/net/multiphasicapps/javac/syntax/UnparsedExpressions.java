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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	/** The hashcode for these expressions. */
	private final int _hashcode;
	
	/** String form. */
	private Reference<String> _string;
	
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
		this._hashcode = tokens.hashCode();
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
		return this._hashcode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				Arrays.asList(this._tokens).toString()));
		
		return rv;
	}
	
	/**
	 * Parses arguments which are included in a block of parenthesis.
	 *
	 * @param __in The input tokens.
	 * @return The unparsed expressions.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If it could not be parsed.
	 * @since 2018/05/01
	 */
	public static UnparsedExpressions parseArguments(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		return UnparsedExpressions.stackTypeParse(__in,
			TokenType.SYMBOL_OPEN_PARENTHESIS,
			TokenType.SYMBOL_CLOSED_PARENTHESIS);
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
		
		return UnparsedExpressions.stackTypeParse(__in,
			TokenType.SYMBOL_OPEN_BRACE, TokenType.SYMBOL_CLOSED_BRACE);
	}
	
	/**
	 * Parses expressions keeping a stack of which ones that were opened and
	 * closed.
	 *
	 * @param __in The input tokens.
	 * @param __open The open type.
	 * @param __close The close type.
	 * @return The unparsed expressions.
	 * @since 2018/05/01
	 */
	public static UnparsedExpressions stackTypeParse(BufferedTokenSource __in,
		TokenType __open, TokenType __close)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null || __open == null || __close == null)
			throw new NullPointerException("NARG");
		
		// Input tokens
		List<Token> tokens = new LinkedList<>();
		
		// {@squirreljme.error AQ2l Expected start of expression to start with
		// a given symbol. (The expected symbol)}
		Token token = __in.next(),
			base = token;
		if (token.type() != __open)
			throw new SyntaxParseException(token, String.format("AQ2l %s",
				__open));
		tokens.add(token);
		
		// Just count braces and such
		for (int count = 1; count != 0;)
		{
			token = __in.next();
			
			// {@squirreljme.error AQ2m This token might not have been
			// closed properly as the end of file was reached.}
			if (token.type() == TokenType.END_OF_FILE)
				throw new SyntaxParseException(base, "AQ2m");
			
			// Add or remove braces
			else if (token.type() == __open)
				count++;
			else if (token.type() == __close)
				count--;
			
			// Add token always
			tokens.add(token);
		}
		
		// Build
		return new UnparsedExpressions(tokens);
	}
}

