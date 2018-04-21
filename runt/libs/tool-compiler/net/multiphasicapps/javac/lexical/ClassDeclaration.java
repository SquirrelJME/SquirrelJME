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

import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This repersents the definition of a class.
 *
 * @since 2018/04/10
 */
public abstract class ClassDeclaration
	extends ClassOrInterfaceDeclaration
{
	/**
	 * Parses a class declaration.
	 *
	 * @param __m Modifiers to the class.
	 * @param __t The input token source.
	 * @return The parsed type.
	 * @throws LexicalStructureException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static final ClassDeclaration parseClass(Modifier[] __m,
		BufferedTokenSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__m == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Parsing may fail
		__t.mark();
		
		// Try to parse as a normal class
		ClassDeclaration rv;
		try
		{
			rv = NormalClassDeclaration.parseNormalClass(__m, __t);
			
			// Use this set of input
			__t.commit();
			return rv;
		}
		
		// Could not parse as normal class
		catch (LexicalStructureException e)
		{
			// Parsing failed so revert to the previous mark
			__t.reset();
			
			// Try parsing as enumeration
			try
			{
				rv = EnumDeclaration.parseEnum(__m, __t);
				
				// Use this parse
				__t.commit();
				return rv;
			}
			
			// Could not parse as enumeration
			catch (LexicalStructureException f)
			{
				// {@squirreljme.error AQ36 Could not parse as a normal class
				// or enumeration.}
				LexicalStructureException t = new LexicalStructureException(
					__t, "AQ38");
				
				// Make these suppressed so they always appear
				t.addSuppressed(e);
				t.addSuppressed(f);
				
				throw t;
			}
		}
	}
}

