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

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a single formal parameter which may be a part of a class.
 *
 * @since 2018/04/28
 */
public final class FormalParameter
{
	/** The modifiers used. */
	protected final Modifiers modifiers;
	
	/** The type. */
	protected final Type type;
	
	/** The name of the field. */
	protected final FieldName name;
	
	/**
	 * Initializes the formal parameter.
	 *
	 * @param __mods The modifiers for the parameter.
	 * @param __type The type used.
	 * @param __name The name of the parameter.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureDefinitionException If the definition is not valid.
	 * @since 2018/04/30
	 */
	public FormalParameter(Modifiers __mods, Type __type, FieldName __name)
		throws NullPointerException, StructureDefinitionException
	{
		if (__mods == null || __type == null || __name == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ4f Illegal modifiers specified for formal
		// parameter. (The modifiers}}
		if (__mods.isPublic() || __mods.isProtected() || __mods.isPrivate() ||
			__mods.isStatic() || __mods.isAbstract() || __mods.isNative() ||
			__mods.isSynchronized() || __mods.isTransient() ||
			__mods.isVolatile() || __mods.isStrictFloatingPoint())
			throw new StructureDefinitionException(
				String.format("AQ4f %s", __mods));
		
		this.modifiers = __mods;
		this.type = __type;
		this.name = __name;
	}
	
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
}

