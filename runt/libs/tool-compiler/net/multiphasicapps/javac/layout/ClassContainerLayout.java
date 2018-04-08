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
 * This contains the layout for the class container which essentially is the
 * outer portion of the class declaration which includes modifiers (including
 * annotations), classnames, possible generic type declarations, extends, and
 * implements.
 *
 * @since 2018/03/22
 */
public final class ClassContainerLayout
	implements Layout
{
	/**
	 * Parses a single class container and returns it.
	 *
	 * @param __t Where to read class containers from.
	 * @throws LayoutParserException If the class container could not be
	 * parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public static final ClassContainerLayout parse(ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Read modifiers to the class
		Modifiers modifiers = Modifiers.parse(__t);
		
		// Determine class type
		if (true)
			throw new todo.TODO();
		
		// Read generic arguments to the class, if applicable
		if (true)
			throw new todo.TODO();
		
		// Read class name
		if (true)
			throw new todo.TODO();
		
		// Read extends
		if (true)
			throw new todo.TODO();
		
		// Read implements
		if (true)
			throw new todo.TODO();
		
		throw new todo.TODO();
	}
}

