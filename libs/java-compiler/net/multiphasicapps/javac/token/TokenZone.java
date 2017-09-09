// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

/**
 * This represents the zone that a token appeared within.
 *
 * @since 2017/09/09
 */
public enum TokenZone
{
	/** The outer region such as package, imports, and class declarations. */
	OUTER,
	
	/** Inside of a class declaration. */
	IN_CLASS,
	
	/** Inside of a field. */
	IN_FIELD,
	
	/** Inside of a method. */
	IN_METHOD,
	
	/** End. */
	;
	
	/**
	 * Can non-local classes be declared in this zone?
	 *
	 * @return {@code true} if non-local classes can be declared.
	 * @since 2017/09/09
	 */
	public final boolean canDeclareNonLocalClass()
	{
		return this == OUTER || this == IN_CLASS;
	}
	
	/**
	 * Is this part of a member zone.
	 *
	 * @return {@code true} if this is a member zone.
	 * @since 2017/09/09
	 */
	public final boolean isMemberZone()
	{
		return this == IN_FIELD || this == IN_METHOD;
	}
}

