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

import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents an import statement.
 *
 * @since 2018/04/10
 */
public final class ImportDeclaration
{
	/**
	 * {@inheritDoc}
	 * @since 2018/04/13
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ImportDeclaration))
			return false;
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/13
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/13
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses an import statement.
	 *
	 * @param __t The input token source.
	 * @return The import statement.
	 * @throws LexicalStructureException If the import is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/13
	 */
	public static final ImportDeclaration parse(ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ30 Expected "import" when parsing an import
		// statement.}
		ExpandedToken token = __t.next();
		if (token.type() != TokenType.KEYWORD_IMPORT)
			throw new LexicalStructureException(token, "AQ30");
		
		throw new todo.TODO();
	}
}

