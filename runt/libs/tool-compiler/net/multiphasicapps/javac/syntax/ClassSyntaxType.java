// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

/**
 * This represents the type of class.
 *
 * @since 2018/04/21
 */
public enum ClassSyntaxType
{
	/** Normal class. */
	CLASS,
	
	/** Interface. */
	INTERFACE,
	
	/** Enumeration. */
	ENUM,
	
	/** Annotation. */
	ANNOTATION,
	
	/** End. */
	;
	
	/**
	 * Returns the type of inheritence that is used for extends.
	 *
	 * @return The inheritence type used for extends.
	 * @since 2018/04/24
	 */
	public final InheritenceTypeSyntax extendsType()
	{
		if (this == CLASS)
			return InheritenceTypeSyntax.SINGLE;
		else if (this == INTERFACE)
			return InheritenceTypeSyntax.MULTIPLE;
		return InheritenceTypeSyntax.NONE;
	}
	
	/**
	 * Does this type have type parameters?
	 *
	 * @return If it has type parameters.
	 * @since 2018/04/24
	 */
	public final boolean hasTypeParameters()
	{
		return this == CLASS || this == INTERFACE;
	}
	
	/**
	 * Returns the inheritence type used for implements.
	 *
	 * @return The inheritence type for implements.
	 * @since 2018/04/24
	 */
	public final InheritenceTypeSyntax implementsType()
	{
		if (this == CLASS || this == ENUM)
			return InheritenceTypeSyntax.MULTIPLE;
		return InheritenceTypeSyntax.NONE;
	}
}

