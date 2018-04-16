// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.lexical;

import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a class or interface declaration.
 *
 * @since 2018/04/10
 */
public abstract class ClassOrInterfaceDeclaration
{
	/**
	 * {@inheritDoc}
	 * @since 2018/04/15
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/15
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/15
	 */
	@Override
	public abstract String toString();
	
	/**
	 * Parses a class or interface declaration.
	 *
	 * @param __t The input token source.
	 * @return The parsed type.
	 * @throws LexicalStructureException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static ClassOrInterfaceDeclaration parse(ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

