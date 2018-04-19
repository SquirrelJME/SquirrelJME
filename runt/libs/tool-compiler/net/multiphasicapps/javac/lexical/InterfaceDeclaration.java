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

import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents the declaration of an interface.
 *
 * @since 2018/04/10
 */
public abstract class InterfaceDeclaration
	extends ClassOrInterfaceDeclaration
{
	/**
	 * Parses an interface declaration.
	 *
	 * @param __m Modifiers to the interface.
	 * @param __t The input token source.
	 * @return The parsed type.
	 * @throws LexicalStructureException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static final InterfaceDeclaration parseInterface(Modifier[] __m,
		BufferedTokenSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__m == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

