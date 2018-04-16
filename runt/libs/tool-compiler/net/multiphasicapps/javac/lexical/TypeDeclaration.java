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

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a type declaration which defines a type.
 *
 * @since 2018/04/10
 */
public abstract class TypeDeclaration
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
	 * Parses a type declaration.
	 *
	 * @param __t The input token source.
	 * @return The declared type or {@code null} if no type was declared.
	 * @throws LexicalStructureException If the import is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/14
	 */
	public static TypeDeclaration parse(ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// No type to actually parse?
		ExpandedToken token = __t.peek();
		if (token.type() == TokenType.SYMBOL_SEMICOLON)
		{
			__t.next();
			return null;
		}
		
		// Just the same as the class or interface declaration
		return ClassOrInterfaceDeclaration.parse(__t);
	}
}

