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
 * This class represents the structure of a class, it will contains members
 * such as fields, methods, and other classes.
 *
 * @since 2018/04/21
 */
public final class ClassStructure
{
	/**
	 * Attempts to parse an entire class.
	 *
	 * @param __mods Class modifiers.
	 * @param __in The input token source.
	 * @return The parsed class structure.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If this is not a class.
	 * @since 2018/04/22
	 */
	public static ClassStructure parseEntireClass(Modifiers __mods,
		BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		Token token;
		
		// Determine the class type based on the next token
		ClassStructureType structtype;
		token = __in.next();
		switch (token.type())
		{
			case KEYWORD_CLASS:
				structtype = ClassStructureType.CLASS;
				break;
				
			case KEYWORD_ENUM:
				structtype = ClassStructureType.ENUM;
				break;
				
			case KEYWORD_INTERFACE:
				structtype = ClassStructureType.INTERFACE;
				break;
			
				// {@squirreljme.error AQ3m Expected interface to follow
				// at symbol for declaring an annotation type.}
			case SYMBOL_AT:
				token = __in.next();
				if (token.type() != TokenType.KEYWORD_INTERFACE)
					throw new StructureParseException(token, "AQ3m");
				break;
			
				// {@squirreljme.error AQ3n Unknown token while parsing class.}
			default:
				throw new StructureParseException(token, "AQ3n");
		}
		
		throw new todo.TODO();
	}
}

