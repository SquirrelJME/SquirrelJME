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
		
		// Parse any type parameters which are used
		TypeParameter[] typeparams;
		Token token = __in.peek();
		if (token.type() == TokenType.COMPARE_LESS_THAN)
			typeparams = TypeParameter.parseTypeParameters(__in);
		else
			typeparams = new TypeParameter[0];
		
		throw new todo.TODO();
	}
}

