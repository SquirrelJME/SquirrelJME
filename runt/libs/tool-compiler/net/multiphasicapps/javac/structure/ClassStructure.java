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

import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.classfile.ClassIdentifier;
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
				structtype = ClassStructureType.ANNOTATION;
				break;
			
				// {@squirreljme.error AQ3n Unknown token while parsing class.}
			default:
				throw new StructureParseException(token, "AQ3n");
		}
		
		// {@squirreljme.error AQ3v Expected identifier to name the class as.}
		token = __in.next();
		if (token.type() != TokenType.IDENTIFIER)
			throw new StructureParseException(token, "AQ3v");
		ClassIdentifier name = new ClassIdentifier(token.characters());
		
		// Read type parameters?
		token = __in.peek();
		TypeParameter[] typeparms;
		if (structtype.hasTypeParameters() &&
			token.type() == TokenType.COMPARE_LESS_THAN)
			typeparms = TypeParameter.parseTypeParameters(__in);
		else
			typeparms = new TypeParameter[0];
		
		// Read extends
		token = __in.peek();
		Type[] extending;
		if (token.type() == TokenType.KEYWORD_EXTENDS)
		{
			// Consume and parse list
			__in.next();
			switch (structtype.extendsType())
			{
				case SINGLE:
					extending = new Type[]{Type.parseType(__in)};
					break;
					
				case MULTIPLE:
					extending = Type.parseTypes(__in);
					break;
					
					// {@squirreljme.error AQ3w This type of class cannot
					// extend other classes.}
				default:
					throw new StructureParseException(token, "AQ3w");
			}
		}
		else
			extending = new Type[0];
		
		// Read implements
		token = __in.peek();
		Type[] implementing;
		if (token.type() == TokenType.KEYWORD_IMPLEMENTS)
		{
			// Consume and parse list
			__in.next();
			switch (structtype.extendsType())
			{
				case MULTIPLE:
					implementing = Type.parseTypes(__in);
					break;
					
					// {@squirreljme.error AQ3y This type of class cannot
					// implement interfaces.}
				default:
					throw new StructureParseException(token, "AQ3y");
			}
		}
		else
			implementing = new Type[0];
		
		if (true)
			throw new todo.TODO();
		
		// Read class body which contains all the members
		if (structtype == ClassStructureType.ENUM)
			return new ClassStructure();
		return new ClassStructure();
	}
}

