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
 * This represents an identifier.
 *
 * @since 2018/05/03
 */
public final class IdentifierSyntax
{
	/** The string which makes up the identifier. */
	protected final String string;
	
	/**
	 * Initializes the identifier syntax.
	 *
	 * @param __s The identifier string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public IdentifierSyntax(String __s)
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

