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

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a single formal parameter which may be a part of a class.
 *
 * @since 2018/04/28
 */
public final class FormalParameter
{
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
	 * @since 2018/04/28
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses formal parameters which are part of a description of a method.
	 *
	 * @param __in The input tokens.
	 * @return The parsed formal parameters.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If a formal parameter is not correct.
	 * @since 2018/04/28
	 */
	public static FormalParameter[] parseFormalParameters(
		BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ43 Expected opening parenthesis when parsing
		// formal parameters.}
		Token token = __in.next();
		if (token.type() != TokenType.SYMBOL_OPEN_PARENTHESIS)
			throw new StructureParseException("AQ43");
		
		// Parse each one
		List<FormalParameter> rv = new ArrayList<>();
		for (;;)
		{
			token = __in.peek();
			
			// End of parameters
			if (token.type() == TokenType.SYMBOL_CLOSED_PARENTHESIS)
			{
				__in.next();
				break;
			}
			
			// Parse modifiers
			Modifiers mods = Modifiers.parseForFormalParameter(__in);
			
			// Parse type
			Type type = Type.parseType(__in);
			
			// Is this a variadic argument?
			boolean isvariadic;
			token = __in.peek();
			if ((isvariadic = (token.type() == TokenType.SYMBOL_ELLIPSES)))
				__in.next();
			
			// {@squirreljme.error AQ45 Expected identifier for the parameter
			// name.}
			token = __in.next();
			if (token.type() != TokenType.IDENTIFIER)
				throw new StructureParseException(token, "AQ45");
			FieldName name = new FieldName(token.characters());
			
			// Add arrays accordingly
			while ((token = __in.peek()).type() ==
				TokenType.SYMBOL_OPEN_BRACKET)
			{
				// {@squirreljme.error AQ47 Expected closing bracket to follow
				// opening bracket for array declaration.}
				__in.next();
				if (__in.next().type() != TokenType.SYMBOL_CLOSED_BRACKET)
					throw new StructureParseException(token, "AQ47");
				
				type = type.withArray();
			}
			
			// Setup parameter
			if (true)
				throw new todo.TODO();
			
			// {@squirreljme.error AQ46 Expected closing parenthesis to follow
			// variadic argument in formal parameter.}
			token = __in.peek();
			if (isvariadic && token.type() !=
				TokenType.SYMBOL_CLOSED_PARENTHESIS)
				throw new StructureParseException(token, "AQ46");
			
			// Consume comma
			if (token.type() == TokenType.SYMBOL_COMMA)
				__in.next();
		}
		
		return rv.<FormalParameter>toArray(new FormalParameter[rv.size()]);
	}
}

