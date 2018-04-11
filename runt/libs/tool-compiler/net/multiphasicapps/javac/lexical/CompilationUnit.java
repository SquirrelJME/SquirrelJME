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
 * This represents a compilation unit which contains the package declaration,
 * all imports, and any classes to be declared.
 *
 * @since 2018/04/10
 */
public class CompilationUnit
{
	/**
	 * Parses this given lexical structure.
	 *
	 * @param __t The input token source.
	 * @return The parsed structure.
	 * @throws LexicalStructureException If the structure is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/11
	 */
	public static final CompilationUnit parse(ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		ExpandedToken token = __t.peek();
		TokenType type = token.type();
		
		// There are potentially two states when it comes to parsing a file,
		// the file could start with annotations and have a package statement
		// or it could instead declare an actual class. So in this case try
		// to parse a class first just to see if it is one
		if (type == TokenType.SYMBOL_AT)
			try (ExpandingSource split = __t.split())
			{
				throw new todo.TODO();
			}
			catch (LexicalStructureException e)
			{
				// Ignore because this was parsed to not be a class at all
			}
		
		// Make valid again
		token = __t.peek();
		type = token.type();
		
		// Parsing annotations?
		boolean musthavepackage = false;
		if (type == TokenType.SYMBOL_AT)
		{
			// There must be a package statement!
			musthavepackage = true;
			
			throw new todo.TODO();
		}
		
		// Clear for package read
		token = __t.next();
		type = token.type();
		
		// Read package statement
		if (type == TokenType.KEYWORD_PACKAGE)
		{
			throw new todo.TODO();
		}
		
		// {@squirreljme.error AQ2x There must be a package statement
		// following any declared annotations for a package.}
		else if (musthavepackage)
			throw new LexicalStructureException(token, "AQ2x");
		
		throw new todo.TODO();
	}
}

