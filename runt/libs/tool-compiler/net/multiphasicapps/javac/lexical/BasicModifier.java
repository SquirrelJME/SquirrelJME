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
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a basic identifier.
 *
 * @since 2018/04/15
 */
public enum BasicModifier
	implements Modifier
{
	/** Public access. */
	PUBLIC,
	
	/** Protected access. */
	PROTECTED,
	
	/** Private access. */
	PRIVATE,
	
	/** Static access. */
	STATIC,
	
	/** Abstract. */
	ABSTRACT,
	
	/** Final. */
	FINAL,
	
	/** Native. */
	NATIVE,
	
	/** Synchronized. */
	SYNCHRONIZED,
	
	/** Transient. */
	TRANSIENT,
	
	/** Volatile. */
	VOLATILE,
	
	/** Strict floating point (should not be used today). */
	STRICTFP,
	
	/** End. */
	;
	
	/**
	 * Parses a single modifier.
	 *
	 * @param __t The input token source.
	 * @return The parsed modifier or {@code null} if no modifier was parsed.
	 * @throws LexicalStructureException If the modifier is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/14
	 */
	public static final Modifier parse(BufferedTokenSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		Token token = __t.peek();
		TokenType type = token.type();
		
		// Is this an annotation? Do not parse @interface as an annotation
		// since that is not valid
		if (type == TokenType.SYMBOL_AT &&
			__t.peek(1).type() != TokenType.KEYWORD_INTERFACE)
			return Annotation.parse(__t);
		
		// Basic modifier?
		BasicModifier rv;
		switch (type)
		{
			case KEYWORD_PUBLIC:
				rv = BasicModifier.PUBLIC;
				break;
				
			case KEYWORD_PROTECTED:
				rv = BasicModifier.PROTECTED;
				break;
				
			case KEYWORD_PRIVATE:
				rv = BasicModifier.PRIVATE;
				break;
				
			case KEYWORD_STATIC:
				rv = BasicModifier.STATIC;
				break;
				
			case KEYWORD_ABSTRACT:
				rv = BasicModifier.ABSTRACT;
				break;
				
			case KEYWORD_FINAL:
				rv = BasicModifier.FINAL;
				break;
				
			case KEYWORD_NATIVE:
				rv = BasicModifier.NATIVE;
				break;
				
			case KEYWORD_SYNCHRONIZED:
				rv = BasicModifier.SYNCHRONIZED;
				break;
				
			case KEYWORD_TRANSIENT:
				rv = BasicModifier.TRANSIENT;
				break;
				
			case KEYWORD_VOLATILE:
				rv = BasicModifier.VOLATILE;
				break;
				
			case KEYWORD_STRICTFP:
				rv = BasicModifier.STRICTFP;
				break;
			
				// Not one
			default:
				rv = null;
				break;
		}
		
		// Consume that token because otherwise there will be an infinite loop
		if (rv != null)
			__t.next();
		return rv;
	}
	
	/**
	 * Parses a single modifier.
	 *
	 * @param __t The input token source.
	 * @return An array containing the parsed modifiers
	 * @throws LexicalStructureException If the modifiers are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/14
	 */
	public static final Modifier[] parseGroup(BufferedTokenSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Parse modifiers
		List<Modifier> rv = new ArrayList<>();
		for (;;)
		{
			// Parse
			Modifier mod = BasicModifier.parse(__t);
			if (mod == null)
				break;
			
			// It is valid and as such it will be used
			rv.add(mod);
		}
		
		return rv.<Modifier>toArray(new Modifier[rv.size()]);
	}
}

