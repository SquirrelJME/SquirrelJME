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

import net.multiphasicapps.javac.syntax.QualifiedIdentifierSyntax;

/**
 * This is just a plain identifier.
 *
 * @since 2018/05/03
 */
public final class PlainIdentifierSyntax
	implements PrimarySyntax
{
	/** The identifier used. */
	protected final QualifiedIdentifierSyntax identifier;
	
	/**
	 * Initializes the plain identifier.
	 *
	 * @param __i The identifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public PlainIdentifierSyntax(QualifiedIdentifierSyntax __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.identifier = __i;
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

