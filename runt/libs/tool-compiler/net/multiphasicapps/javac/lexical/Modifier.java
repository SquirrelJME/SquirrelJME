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
import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a single modifier which is used an associated with a
 * package or type.
 *
 * @since 2018/04/10
 */
public final class Modifier
{
	/**
	 * Parses a single modifier.
	 *
	 * @param __t The input token source.
	 * @return The parsed modifier or {@code null} if no modifier was parsed.
	 * @throws LexicalStructureException If the modifier is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/14
	 */
	public static final Modifier parse(ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
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
	public static final Modifier[] parseGroup(ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Parse modifiers
		List<Modifier> rv = new ArrayList<>();
		for (;;)
		{
			// Parse
			Modifier mod = Modifier.parse(__t);
			if (mod == null)
				break;
			
			// It is valid and as such it will be used
			rv.add(mod);
		}
		
		return rv.<Modifier>toArray(new Modifier[rv.size()]);
	}
}

