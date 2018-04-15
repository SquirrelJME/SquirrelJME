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
 * This represents a type declaration which defines a class.
 *
 * @since 2018/04/10
 */
public final class TypeDeclaration
{
	/**
	 * {@inheritDoc}
	 * @since 2018/04/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/15
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/15
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses a type declaration.
	 *
	 * @param __t The input token source.
	 * @return The declared type or {@code null} if no type was declared.
	 * @throws LexicalStructureException If the import is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/14
	 */
	public static final TypeDeclaration parse(ExpandingSource __t)
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
		
		// Parse modifiers
		Modifier[] mods = BasicModifier.parseGroup(__t);
		
		throw new todo.TODO();
	}
}

