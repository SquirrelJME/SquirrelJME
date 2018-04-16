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
 * This represents the class body declarations.
 *
 * @since 2018/04/10
 */
public final class ClassBodyDeclaration
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
	 * Parses the body of the class which includes the braces.
	 *
	 * @param __t The input token source.
	 * @return The parsed class body declarations.
	 * @throws LexicalStructureException If the class body is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static final ClassBodyDeclaration[] parseClassBody(
		ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ39 Expected open brace at start of class
		// body.}
		ExpandedToken token = __t.next();
		if (token.type() != TokenType.SYMBOL_OPEN_BRACE)
			throw new LexicalStructureException(token, "AQ39");
		
		// Try parsing the body of classes
		List<ClassBodyDeclaration> rv = new ArrayList<>();
		for (;;)
		{
			// End of sequence? Consume it and end
			token = __t.peek();
			if (token.type() == TokenType.SYMBOL_CLOSED_BRACE)
			{
				__t.next();
				break;
			}
			
			// Declares nothing, so do not bother
			else if (token.type() == TokenType.SYMBOL_SEMICOLON)
				continue;
			
			// Otherwise it must be a declaration
			rv.add(ClassBodyDeclaration.parseClassBodyDeclaration(__t));
		}
		
		return rv.<ClassBodyDeclaration>toArray(
			new ClassBodyDeclaration[rv.size()]);
	}
	
	/**
	 * Parses a single class body declaration which in most cases will be
	 * something which may be a member of a class.
	 *
	 * @param __t The input token source.
	 * @return The parsed class body declaration.
	 * @throws LexicalStructureException If the class body declaration is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static final ClassBodyDeclaration parseClassBodyDeclaration(
		ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		ExpandedToken token = __t.next();
		TokenType type = token.type();
		
		// Parse of static initializer?
		if (type == TokenType.KEYWORD_STATIC &&
			__t.peek(1).type() == TokenType.SYMBOL_OPEN_BRACE)
		{
			throw new todo.TODO();
		}
		
		// Just normal common initializer
		else if (type == TokenType.SYMBOL_OPEN_BRACE)
		{
			throw new todo.TODO();
		}
		
		// Is a basic member, so it will have modifiers
		Modifier[] mods = BasicModifier.parseGroup(__t);
		
		throw new todo.TODO();
	}
}

