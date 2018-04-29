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
 * This represents a method which is a member of a class and may contain Java
 * code to be compiled.
 *
 * @since 2018/04/27
 */
public abstract class MethodStructure
	implements MemberStructure
{
	/**
	 * Parses a single method which is appropriate for a given class type.
	 *
	 * @param __ct The structure of the class.
	 * @param __mods The modifiers to the method.
	 * @param __in The input tokens.
	 * @return The parsed method.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If it is not a valid method.
	 * @since 2018/04/28
	 */
	public static MethodStructure parseMethod(ClassStructureType __ct,
		Modifiers __mods, BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__ct == null || __mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Annotations only have a single format
		if (__ct == ClassStructureType.ANNOTATION)
			return AnnotationMethod.parse(__mods, __in);
		
		// Initializer method?
		Token token = __in.peek();
		if ((__ct == ClassStructureType.CLASS ||
			__ct == ClassStructureType.ENUM) &&
			token.type() == TokenType.SYMBOL_OPEN_BRACE)
			try
			{
				// Might not be one
				__in.mark();
				ClassInitializer rv = ClassInitializer.parse(__mods, __in);
				
				// Is one
				__in.commit();
				return rv;
			}
			catch (StructureParseException e)
			{
				__in.reset();
			}
		
		// Parse any type parameters which are used
		TypeParameter[] typeparams;
		token = __in.peek();
		if (__ct != ClassStructureType.ANNOTATION &&
			token.type() == TokenType.COMPARE_LESS_THAN)
			typeparams = TypeParameter.parseTypeParameters(__in);
		else
			typeparams = new TypeParameter[0];
		
		// Constructor?
		token = __in.peek();
		if (__ct != ClassStructureType.INTERFACE &&
			token.type() == TokenType.IDENTIFIER &&
			__in.peek(1).type() == TokenType.SYMBOL_OPEN_PARENTHESIS)
			try
			{
				// Could be one
				__in.mark();
				ClassConstructor rv = ClassConstructor.parse(__mods,
					typeparams, __in);
				
				// Is one
				__in.commit();
				return rv;
			}
			catch (StructureParseException e)
			{
				__in.reset();
			}
		
		// General method
		return SimpleMethod.parse(__mods, typeparams, __in);
	}
}

