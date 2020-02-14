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

/**
 * This represents an expression which is prefixed by a prefix operation.
 *
 * @since 2018/05/03
 */
public final class PrefixOpExpressionSyntax
	implements ExpressionThreeSyntax
{
	/** The prefix operator. */
	protected final PrefixOperatorType operator;
	
	/** The expression being operated on. */
	protected final ExpressionThreeSyntax expression;
	
	/**
	 * Initializes the prefix operation.
	 *
	 * @param __op The operation being performed.
	 * @param __expr The expression to be operated on.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public PrefixOpExpressionSyntax(PrefixOperatorType __op,
		ExpressionThreeSyntax __expr)
		throws NullPointerException
	{
		if (__op == null || __expr == null)
			throw new NullPointerException("NARG");
		
		this.operator = __op;
		this.expression = __expr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

