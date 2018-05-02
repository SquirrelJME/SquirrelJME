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

/**
 * Represents an expression that is acting as a cast.
 *
 * @since 2018/05/02
 */
public final class ExpressionCastSyntax
	implements SubExpressionSyntax
{
	/** The type to cast to. */
	protected final TypeSyntax type;
	
	/**
	 * Initializes the cast expression.
	 *
	 * @param __type The type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/02
	 */
	public ExpressionCastSyntax(TypeSyntax __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/02
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/02
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/02
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

