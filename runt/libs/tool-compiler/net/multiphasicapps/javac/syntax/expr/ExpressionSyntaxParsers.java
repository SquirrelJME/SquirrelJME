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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.javac.syntax.QualifiedIdentifierSyntax;
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
		if (token.type().isAssignmentOperator())
			throw new todo.TODO();
		
		// Just a plain expression syntax
		else
			return new PlainExpressionSyntax(one);
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
		if (token.type() == TokenType.SYMBOL_QUESTION)
			throw new todo.TODO();
		
		// Just a plain expression one
		else
			return new PlainExpressionOneSyntax(two);
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
		ExpressionThreeSyntax three = ExpressionSyntaxParsers.
			parseExpressionThree(__in);
		
		// May be followed by instanceof
		Token token = __in.peek();
		if (token.type() == TokenType.KEYWORD_INSTANCEOF)
			throw new todo.TODO();
		
		// Otherwise it might be infix operations stacked on each other
		// Note that expression twos are not included in the same region so
		// a bunch of infix operations will stack on each other
		else if (token.type().isInfixOperation())
			throw new todo.TODO();
		
		// Is just a plain expression two syntax
		else
			return new PlainExpressionTwoSyntax(three);
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
		
		// This could be a prefix operation
		Token token = __in.peek();
		PrefixOperatorType prefix;
		switch (token.type())
		{
			case OPERATOR_INCREMENT:
				prefix = PrefixOperatorType.INCREMENT;
				break;
			
			case OPERATOR_DECREMENT:
				prefix = PrefixOperatorType.DECREMENT;
				break;
			
			case OPERATOR_NOT:
				prefix = PrefixOperatorType.NOT;
				break;
			
			case OPERATOR_COMPLEMENT:
				prefix = PrefixOperatorType.ONES_COMPLEMENT;
				break;
			
			case OPERATOR_PLUS:
				prefix = PrefixOperatorType.POSITIVE;
				break;
			
			case OPERATOR_MINUS:
				prefix = PrefixOperatorType.NEGATIVE;
				break;
			
			default:
				prefix = null;
				break;
		}
		
		// This is a prefixed operation
		if (prefix != null)
		{
			__in.next();
			return new PrefixOpExpressionSyntax(prefix,
				ExpressionSyntaxParsers.parseExpressionThree(__in));
		}
		
		// Will be a type cast or an expression in a parenthesis (the latter
		// I am not too sure about as it does not make much sense, it could
		// be a language design decision for future proofing?)
		token = __in.peek();
		if (token.type() == TokenType.SYMBOL_OPEN_PARENTHESIS)
		{
			throw new todo.TODO();
		}
		
		// Parse a primary
		PrimarySyntax primary = ExpressionSyntaxParsers.parsePrimary(__in);
		
		// Read in selectors
		List<SelectorSyntax> selectors = new ArrayList<>();
		for (;;)
		{
			// Is not going to be a selector?
			token = __in.peek();
			if (token.type() != TokenType.SYMBOL_DOT &&
				token.type() != TokenType.SYMBOL_OPEN_BRACE)
				break;
			
			throw new todo.TODO();
		}
		
		// Read in postfix operations, if any
		List<PostfixOperatorType> postfixes = new ArrayList<>();
		for (;;)
		{
			token = __in.peek();
			
			// Increment?
			if (token.type() == TokenType.OPERATOR_INCREMENT)
			{
				__in.next();
				postfixes.add(PostfixOperatorType.INCREMENT);
			}
			
			// Decrement?
			else if (token.type() == TokenType.OPERATOR_DECREMENT)
			{
				__in.next();
				postfixes.add(PostfixOperatorType.DECREMENT);
			}
			
			// Unknown, stop parsing
			else
				break;
		}
		
		// Build expression
		return new PrimarySelectorAndPostfixExpression(primary,
			selectors, postfixes);
	}
	
	/**
	 * Parses a primary syntax.
	 *
	 * @param __in The input token source.
	 * @return The primary token.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If it is not a valid primary syntax.
	 * @since 2018/05/03
	 */
	public static PrimarySyntax parsePrimary(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// It could be a literal value
		Token token = __in.peek();
		switch (token.type())
		{
			case LITERAL_NULL:
			case LITERAL_FALSE:
			case LITERAL_TRUE:
			case LITERAL_BINARY_INTEGER:
			case LITERAL_OCTAL_INTEGER:
			case LITERAL_DECIMAL_INTEGER:
			case LITERAL_HEXADECIMAL_INTEGER:
			case LITERAL_DECIMAL_FLOAT:
			case LITERAL_HEXADECIMAL_FLOAT:
			case LITERAL_STRING:
			case LITERAL_CHARACTER:
				__in.next();
				return new LiteralSyntax(token.characters());
			
				// Is not one
			default:
				break;
		}
		
		// Parenthesis operation
		token = __in.peek();
		if (token.type() == TokenType.SYMBOL_OPEN_PARENTHESIS)
			throw new todo.TODO();
		
		// This... something potentially
		else if (token.type() == TokenType.KEYWORD_THIS)
			throw new todo.TODO();
		
		// Super something
		else if (token.type() == TokenType.KEYWORD_SUPER)
			throw new todo.TODO();
		
		// New something
		else if (token.type() == TokenType.KEYWORD_NEW)
			throw new todo.TODO();
		
		// void.class
		else if (token.type() == TokenType.KEYWORD_VOID)
			throw new todo.TODO();
		
		// Non-wildcard type arguments
		else if (token.type() == TokenType.COMPARE_LESS_THAN)
			throw new todo.TODO();
		
		// Is a basic type (which could be an array) followed by .class
		else if (token.type().isBasicType())
			throw new todo.TODO();
		
		// Will be an identifier followed by something
		else if (token.type() == TokenType.IDENTIFIER)
		{
			// This is going to be a qualified identifier but modified slightly
			// since .<non-identifier> can appear which would break
			// QualifiedIdentifierSyntax
			StringBuilder sb = new StringBuilder();
			for (;;)
			{
				// {@squirreljme.error AQ51 Expected identifier while parsing
				// a primary expression.}
				token = __in.next();
				if (token.type() != TokenType.IDENTIFIER)
					throw new SyntaxParseException(token, "AQ51");
				
				// Add that
				sb.append(token.characters());
				
				// Need to peek ahead because it might not be an identifier
				token = __in.peek();
				Token after = __in.peek(1);
				
				// Stop if this is not a .identifier but something else
				if (token.type() != TokenType.SYMBOL_DOT ||
					after.type() != TokenType.IDENTIFIER)
					break;
				
				// Add dot
				sb.append('.');
				__in.next();
			}
			
			// Build that
			IdentifierSyntax qi = new IdentifierSyntax(sb.toString());
			
			// Opening of an array
			token = __in.peek();
			if (token.type() == TokenType.SYMBOL_OPEN_BRACKET)
				throw new todo.TODO();
			
			// Method arguments
			else if (token.type() == TokenType.SYMBOL_OPEN_PARENTHESIS)
				throw new todo.TODO();
			
			// Sub-something on the argument
			else if (token.type() == TokenType.SYMBOL_DOT)
				throw new todo.TODO();
			
			// Is just going to be a plain identifier
			else
				return new PlainIdentifierSyntax(qi);
		}
		
		// {@squirreljme.error AQ50 This is not a valid primary expression.}
		else
			throw new SyntaxParseException(token, "AQ50");
	}
}

