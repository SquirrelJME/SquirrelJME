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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.javac.syntax.expr.ExpressionSyntax;
import net.multiphasicapps.javac.syntax.expr.ExpressionSyntaxParsers;
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
	implements AnnotationValueSyntax, ModifierSyntax
{
	/** The identifier which identifies the annotation. */
	protected final QualifiedIdentifierSyntax identifier;
	
	/** The parameters to the annotation */
	private final AnnotationValueSyntax[] _values;
	
	/**
	 * Initializes the annotation which is just a marker.
	 *
	 * @param __qi The identifier used to identify the annotation.
	 * @param __values The values to the annotation.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/01
	 */
	public AnnotationSyntax(QualifiedIdentifierSyntax __qi,
		AnnotationValueSyntax... __values)
		throws NullPointerException
	{
		this(__qi, Arrays.<AnnotationValueSyntax>asList(
			(__values == null ? new AnnotationValueSyntax[0] : __values)));
	}
	
	/**
	 * Initializes the annotation which contains the given unparsed arguments.
	 *
	 * @param __qi The identifier used to identify the annotation.
	 * @param __values The values to the annotation.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/01
	 */
	public AnnotationSyntax(QualifiedIdentifierSyntax __qi,
		Iterable<AnnotationValueSyntax> __values)
		throws NullPointerException
	{
		if (__qi == null || __values == null)
			throw new NullPointerException("NARG");
		
		this.identifier = __qi;
		
		throw new todo.TODO();
	}
	
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
		
		// {@squirreljme.error AQ4v Expected at symbol at start of annotation.}
		Token token = __in.next();
		if (token.type() != TokenType.SYMBOL_AT)
			throw new SyntaxParseException(token, "AQ4v");
		
		// Read qualified identifier which identifies the type used for the
		// annotation
		QualifiedIdentifierSyntax qi = QualifiedIdentifierSyntax.parse(__in);
		
		// If there is no open parenthesis then it is just a marker
		token = __in.peek();
		if (token.type() != TokenType.SYMBOL_OPEN_PARENTHESIS)
			return new AnnotationSyntax(qi);
		
		// Consume token, not needed
		__in.next();
		
		// Quick check to see if there are no values to parse
		token = __in.peek();
		if (token.type() == TokenType.SYMBOL_CLOSED_PARENTHESIS)
		{
			__in.next();
			return new AnnotationSyntax(qi);
		}
		
		// Annotations may now have values so handle them accordingly
		List<AnnotationValueSyntax> values = new ArrayList<>();
		for (;;)
		{
			// Read single annotation value which may be a key
			values.add(AnnotationSyntax.parseKey(__in));
			
			// No more values?
			token = __in.peek();
			if (token.type() == TokenType.SYMBOL_CLOSED_PARENTHESIS)
			{
				__in.next();
				break;
			}
			
			// Reading more of them
			else if (token.type() == TokenType.SYMBOL_COMMA)
				continue;
			
			// {@squirreljme.error AQ4x Expected comma or closing parenthesis
			// at end of annotation expression.}
			else
				throw new SyntaxParseException(token, "AQ4x");
		}
		
		// Build annotation
		return new AnnotationSyntax(qi, values);
	}
	
	/**
	 * Parses either a value or a key and a value.
	 *
	 * @param __in The input token source.
	 * @return The parsed value.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the value is not valid.
	 * @since 2018/05/03
	 */
	public static AnnotationValueSyntax parseKey(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		Token token = __in.peek(),
			after = __in.peek(1);
		
		// Does this contain a key associated with a value?
		if (token.type() == TokenType.IDENTIFIER &&
			after.type() == TokenType.OPERATOR_ASSIGN)
		{
			// Consume identifier and the equals
			__in.next();
			__in.next();
			
			// Setup key and parse value too
			return new AnnotationKeyValueSyntax(
				new MethodName(token.characters()),
				AnnotationSyntax.parseValue(__in));
		}
		
		// Just a value
		return AnnotationSyntax.parseValue(__in);
	}
	
	/**
	 * This parses a single value.
	 *
	 * @param __in The input token source.
	 * @return The parsed value.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the value is not valid.
	 * @since 2018/05/02
	 */
	public static AnnotationValueSyntax parseValue(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		Token token = __in.peek();
		
		// Recursive read of annotation argument
		if (token.type() == TokenType.SYMBOL_AT)
			return AnnotationSyntax.parse(__in);
		
		// Is a kind of array
		else if (token.type() == TokenType.SYMBOL_OPEN_BRACE)
		{
			// Consume that
			__in.next();
			
			// Quick end of array value?
			token = __in.peek();
			if (token.type() == TokenType.SYMBOL_CLOSED_BRACE)
			{
				__in.next();
				return new AnnotationArrayValueSyntax();
			}
			
			// Read in values
			List<AnnotationValueSyntax> values = new ArrayList<>();
			for (;;)
			{
				// Read in next value
				values.add(AnnotationSyntax.parseValue(__in));
				
				// Stop parsing?
				token = __in.peek();
				if (token.type() == TokenType.SYMBOL_CLOSED_BRACE)
				{
					__in.next();
					break;
				}
				
				// Continue
				else if (token.type() == TokenType.SYMBOL_COMMA)
					continue;
				
				// {@squirreljme.error AQ4y Expected comma or closing brace
				// after annotation value.}
				else
					throw new SyntaxParseException(token, "AQ4y");
			}
			
			// Finish values
			return new AnnotationArrayValueSyntax(values);
		}
		
		// Normal expression
		else
			 return ExpressionSyntaxParsers.parseExpression(__in);
	}	
}

