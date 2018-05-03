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
 * This represents a plain expression two syntax which does not have a block
 * or {@code instanceof}.
 *
 * @since 2018/05/03
 */
public final class PlainExpressionTwoSyntax
	implements ExpressionTwoSyntax
{
	/** The expression used. */
	protected final ExpressionThreeSyntax expression;
	
	/**
	 * Initializes the plain syntax.
	 *
	 * @param __expr The expression used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public PlainExpressionTwoSyntax(ExpressionThreeSyntax __expr)
		throws NullPointerException
	{
		if (__expr == null)
			throw new NullPointerException("NARG");
		
		this.expression = __expr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof PlainExpressionTwoSyntax))
			return false;
		
		PlainExpressionTwoSyntax o = (PlainExpressionTwoSyntax)__o;
		return this.expression.equals(o.expression);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final int hashCode()
	{
		return this.expression.hashCode();
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

