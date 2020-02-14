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
 * This is just a plain identifier.
 *
 * @since 2018/05/03
 */
public final class PlainIdentifierSyntax
	implements PrimarySyntax
{
	/** The identifier used. */
	protected final IdentifierSyntax identifier;
	
	/**
	 * Initializes the plain identifier.
	 *
	 * @param __i The identifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public PlainIdentifierSyntax(IdentifierSyntax __i)
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
		if (__o == this)
			return true;
		
		if (!(__o instanceof PlainIdentifierSyntax))
			return false;
		
		PlainIdentifierSyntax o = (PlainIdentifierSyntax)__o;
		return this.identifier.equals(o.identifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final int hashCode()
	{
		return this.identifier.hashCode();
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

