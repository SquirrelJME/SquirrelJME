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

import net.multiphasicapps.classfile.ClassIdentifier;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a constructor that is available for a class.
 *
 * @since 2018/04/28
 */
public final class ClassConstructor
	implements MethodStructure
{
	/**
	 * Initializes the constructor information.
	 *
	 * @param __mods Constructor modifiers.
	 * @param __ident The identifier for the constructor.
	 * @param __params Type parameters.
	 * @param __thrown Exceptions which are thrown by the constructor.
	 * @param __code The code which makes up the constructor.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/29
	 */
	public ClassConstructor(Modifiers __mods, ClassIdentifier __ident,
		FormalParameter[] __params, QualifiedIdentifier[] __thrown,
		UnparsedExpressions __code)
		throws NullPointerException
	{
		if (__mods == null || __ident == null || __params == null ||
			__thrown == null || __code == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/29
	 */
	@Override
	public final Modifiers modifiers()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/29
	 */
	@Override
	public final MethodName name()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses a single class constrictor.
	 *
	 * @param __mods The modifiers to the method.
	 * @param __typeparams Type parameters.
	 * @param __in The input tokens.
	 * @return The parsed constructor.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If it is not a valid constructor.
	 * @since 2018/04/28
	 */
	public static ClassConstructor parse(Modifiers __mods,
		TypeParameter[] __typeparams, BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__mods == null || __typeparams == null || __in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ41 Expected identifier at start of
		// constructor.}
		Token token = __in.next();
		if (token.type() != TokenType.IDENTIFIER)
			throw new StructureParseException(token, "AQ41");
		ClassIdentifier identifier = new ClassIdentifier(token.characters());
		
		// Parse formal parameters
		FormalParameter[] params = FormalParameter.parseFormalParameters(__in);
		
		// Parse throws
		QualifiedIdentifier[] thrown;
		token = __in.peek();
		if (token.type() == TokenType.KEYWORD_THROWS)
		{
			__in.next();
			thrown = QualifiedIdentifier.parseList(__in);
		}
		else
			thrown = new QualifiedIdentifier[0];
		
		// Parse constructor block and build
		return new ClassConstructor(__mods, identifier, params, thrown,
			UnparsedExpressions.parseBlock(__in));
	}
}

