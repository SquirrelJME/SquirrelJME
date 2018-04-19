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

import net.multiphasicapps.classfile.ClassIdentifier;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents the declaration of a normal class.
 *
 * @since 2018/04/10
 */
public final class NormalClassDeclaration
	extends ClassDeclaration
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
	 * Parses a normal class.
	 *
	 * @param __m The modifiers for the annotation.
	 * @param __t The input token source.
	 * @return The parsed type.
	 * @throws LexicalStructureException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static final NormalClassDeclaration parseNormalClass(Modifier[] __m,
		BufferedTokenSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__m == null || __t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ38 Expected class to appear while attempting
		// to parse a class.}
		Token token = __t.next();
		if (token.type() != TokenType.KEYWORD_CLASS)
			throw new LexicalStructureException(token, "AQ38");
		
		// Parse name
		ClassIdentifier name;
		try
		{
			// {@squirreljme.error AQ3b Expected identifier representing the
			// name of the class after the class keyword.}
			token = __t.next();
			if (token.type() != TokenType.IDENTIFIER)
				throw new LexicalStructureException(token, "AQ3b");
			
			name = new ClassIdentifier(token.characters());
		}
		
		// {@squirreljme.error AQ3a Invalid class name. (The class name)}
		catch (InvalidClassFormatException e)
		{
			throw new LexicalStructureException(token, String.format(
				"AQ3a %s", token.characters()), e);
		}
		
		// Parse type parameters
		TypeParameter[] tparms = null;
		token = __t.peek();
		if (token.type() == TokenType.COMPARE_LESS_THAN)
			tparms = TypeParameter.parseTypeParameters(__t);
		
		// Parse extends
		Type textends = null;
		token = __t.peek();
		if (token.type() == TokenType.KEYWORD_EXTENDS)
		{
			// Consume
			__t.next();
			
			// Parse type
			textends = Type.parseType(__t);
		}
		
		// Parse implements
		Type[] timplements = null;
		token = __t.peek();
		if (token.type() == TokenType.KEYWORD_IMPLEMENTS)
		{
			// Consume
			__t.next();
			
			// Parse types
			timplements = Type.parseTypeList(__t);
		}
		
		// Parse the body of the class
		ClassBodyDeclaration[] body = ClassBodyDeclaration.parseClassBody(__t);
		
		throw new todo.TODO();
	}
}

