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
 * This represents the method used in an annotation.
 *
 * @since 2018/04/28
 */
public final class AnnotationMethod
	extends MethodStructure
{
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses a single annotation method.
	 *
	 * @param __mods The modifiers to the method.
	 * @param __in The input tokens.
	 * @return The parsed annotation method.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If it is not a valid method.
	 * @since 2018/04/28
	 */
	public static AnnotationMethod parse(Modifiers __mods,
		BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

