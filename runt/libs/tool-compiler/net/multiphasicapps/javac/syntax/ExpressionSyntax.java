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
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a single expression which may contain sub-expressions
 * which are assigned to.
 *
 * @since 2018/05/01
 */
public final class ExpressionSyntax
	implements SubExpressionSyntax
{
	/**
	 * Initializes the expression with the given sub-expressions.
	 *
	 * @param __s The sub-expressions that make up this expression.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/01
	 */
	public ExpressionSyntax(SubExpressionSyntax... __s)
		throws NullPointerException
	{
		this(Arrays.<SubExpressionSyntax>asList((__s == null ?
			new SubExpressionSyntax[0] : __s)));
	}
	
	/**
	 * Initializes the expression with the given sub-expressions.
	 *
	 * @param __s The sub-expressions that make up this expression.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/01
	 */
	public ExpressionSyntax(Iterable<SubExpressionSyntax> __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/01
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/01
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/01
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This parses a single expression.
	 *
	 * @param __in The input token source.
	 * @return The parsed expression.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the expression is not valid.
	 * @since 2018/05/01
	 */
	public static AnnotationSyntax parse(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

