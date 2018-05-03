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
 * This represents a literal value which may be a {@code boolean}, number,
 * string, or {@code null}.
 *
 * @since 2018/05/03
 */
public final class LiteralSyntax
	implements PrimarySyntax
{
	/** The characters that make up the literal. */
	protected final String string;
	
	/**
	 * Initializes the literal.
	 *
	 * @param __s The characters that make up the literal.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public LiteralSyntax(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.string = __s;
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
		
		if (!(__o instanceof LiteralSyntax))
			return false;
		
		LiteralSyntax o = (LiteralSyntax)__o;
		return this.string.equals(o.string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final int hashCode()
	{
		return this.string.hashCode();
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

