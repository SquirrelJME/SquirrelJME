// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax.expr;

import java.util.Arrays;
import net.multiphasicapps.javac.syntax.SyntaxDefinitionException;
import net.multiphasicapps.javac.syntax.SyntaxParseException;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This class contains all of the parsers for each of the expression syntax
 * formats which are available.
 *
 * @since 2018/05/03
 */
public final class ExpressionSyntaxParsers
{
	/**
	 * Not used.
	 *
	 * @since 2018/05/03
	 */
	private ExpressionSyntaxParsers()
	{
	}
	
	/**
	 * Parses an expression.
	 *
	 * @param __in The input token source.
	 * @return The resulting expression.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the expression is not valid.
	 * @since 2018/05/03
	 */
	public static ExpressionSyntax parseExpression(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Parse one expression
		ExpressionOneSyntax one = ExpressionSyntaxParsers.
			parseExpressionOne(__in);
		
		// Check for assignment operation
		Token token = __in.peek();
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses an expression one.
	 *
	 * @param __in The input token source.
	 * @return The resulting expression.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the expression is not valid.
	 * @since 2018/05/03
	 */
	public static ExpressionOneSyntax parseExpressionOne(
		BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Parse an expression two
		ExpressionTwoSyntax two = ExpressionSyntaxParsers.
			parseExpressionTwo(__in);
		
		// May contain ternary operation
		Token token = __in.peek();
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses an expression two.
	 *
	 * @param __in The input token source.
	 * @return The resulting expression.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the expression is not valid.
	 * @since 2018/05/03
	 */
	public static ExpressionTwoSyntax parseExpressionTwo(
		BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Starts with an expression three
		if (true)
			throw new todo.TODO();
		
		// May be followed by instancof
		if (true)
			throw new todo.TODO();
		
		// Otherwise it might be infix operations stacked on each other
		// Note that expression twos are not included in the same region so
		// a bunch of infix operations will stack on each other
		if (true)
			throw new todo.TODO();
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses an expression three.
	 *
	 * @param __in The input token source.
	 * @return The resulting expression.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the expression is not valid.
	 * @since 2018/05/03
	 */
	public static ExpressionThreeSyntax parseExpressionThree(
		BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

