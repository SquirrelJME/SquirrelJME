// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents the compilation unit which contains the package, the
 * import statements, and any declared classes.
 *
 * @since 2018/04/21
 */
public final class CompilationUnit
{
	/**
	 * Parses the compilation unit of the class file.
	 *
	 * @param __rawt The input raw token source.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the structure is not valid.
	 * @since 2018/04/21
	 */
	public static CompilationUnit parse(TokenSource __rawt)
		throws NullPointerException, StructureParseException
	{
		if (__rawt == null)
			throw new NullPointerException("NARG");
		
		// Always buffer it
		BufferedTokenSource input = new BufferedTokenSource(__rawt);
		
		throw new todo.TODO();
	}
}

