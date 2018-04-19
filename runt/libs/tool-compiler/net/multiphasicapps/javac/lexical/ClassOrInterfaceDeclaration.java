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
 * This represents a class or interface declaration.
 *
 * @since 2018/04/10
 */
public abstract class ClassOrInterfaceDeclaration
	extends TypeDeclaration
{
	/**
	 * Returns the modifiers for the class or interface.
	 *
	 * @return The modifiers.
	 * @since 2018/04/15
	 */
	public final Modifier[] modifiers()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses a class or interface declaration.
	 *
	 * @param __t The input token source.
	 * @return The parsed type.
	 * @throws LexicalStructureException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static ClassOrInterfaceDeclaration parse(BufferedTokenSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Parse modifiers
		Modifier[] mods = BasicModifier.parseGroup(__t);
		
		// Try to parse as a class
		try (BufferedTokenSource split = __t.split())
		{
			return ClassDeclaration.parseClass(mods, split);
		}
		
		// Failed to parse as a class
		catch (LexicalStructureException e)
		{
			// Try as an interface
			try (BufferedTokenSource split = __t.split())
			{
				return InterfaceDeclaration.parseInterface(mods, split);
			}
			
			// Failed to parse as interface
			catch (LexicalStructureException f)
			{
				// {@squirreljme.error AQ36 Could not parse this part of the
				// class file for a class or interface.}
				LexicalStructureException t = new LexicalStructureException(
					__t, "AQ36");
				
				// Make these suppressed so they always appear
				t.addSuppressed(e);
				t.addSuppressed(f);
				
				throw t;
			}
		}
	}
}

