// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.layout;

import java.io.IOException;
import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents modifiers that may be associated with a class, interface,
 * field or method. Modifiers include modifier keywords along with any
 * annotations which may be split between other modifiers.
 *
 * @since 2018/04/08
 */
public final class Modifiers
{
	/**
	 * Parses the input tokens for any modifiers.
	 *
	 * @param __t Where to read modifiers from.
	 * @throws LayoutParserException If the modifiers could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public static final Modifiers parse(ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Modifier reading loop, since there could be no modifiers to be
		// read at all
__outer:
		for (;;)
		{
			ExpandedToken token = __t.peek();
			TokenType type = token.type();
			
			// Depends on the type
			switch (type)
			{
					// Annotated something
				case SYMBOL_AT:
					throw new todo.TODO();
					
					// Abstract
				case KEYWORD_ABSTRACT:
					throw new todo.TODO();
					
					// Final
				case KEYWORD_FINAL:
					throw new todo.TODO();
					
					// Native
				case KEYWORD_NATIVE:
					throw new todo.TODO();
					
					// Private
				case KEYWORD_PRIVATE:
					throw new todo.TODO();
					
					// Protected
				case KEYWORD_PROTECTED:
					throw new todo.TODO();
					
					// Public
				case KEYWORD_PUBLIC:
					throw new todo.TODO();
					
					// Static
				case KEYWORD_STATIC:
					throw new todo.TODO();
					
					// Strict floating point
				case KEYWORD_STRICTFP:
					throw new todo.TODO();
					
					// Synchronized
				case KEYWORD_SYNCHRONIZED:
					throw new todo.TODO();
					
					// Transient
				case KEYWORD_TRANSIENT:
					throw new todo.TODO();
					
					// Volatile
				case KEYWORD_VOLATILE:
					throw new todo.TODO();
				
					// Do not know what this is, so stop
				default:
					break __outer;
			}
		}
		
		throw new todo.TODO();
	}
}

