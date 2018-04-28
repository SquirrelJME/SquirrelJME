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
 * This represents a field which may have a value for a class.
 *
 * @since 2018/04/28
 */
public abstract class FieldStructure
	implements MemberStructure
{
	/**
	 * Parses a single field which is appropriate for a given class type.
	 *
	 * @param __ct The structure of the class.
	 * @param __mods The modifiers to the field.
	 * @param __in The input tokens.
	 * @return The parsed field.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If it is not a valid field.
	 * @since 2018/04/28
	 */
	public static FieldStructure parseField(ClassStructureType __ct,
		Modifiers __mods, BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__ct == null || __mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

