// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * Represents the set of tokens that represent the given type when it is
 * obtained, this only applies and is only valid for {@link CType}.
 *
 * @see CType
 * @since 2023/06/06
 */
public enum CTokenSet
{
	/** General token display, an alias of any of the below. */
	GENERAL,
	
	/**
	 * Cast to the given type.
	 * 
	 * Example: {@code (jint)squirrel}.
	 */
	CAST,
	
	/**
	 * Declaration of a type.
	 * 
	 * Examples: {@code struct foo};
	 * {@code void squeak(int)};
	 * {@code extern int bar}.
	 */
	DECLARE,
	
	/**
	 * Definition of a type.
	 * 
	 * Examples: {@code struct foo {int a; int b}};
	 * {@code void squeak(int) {boop();}};
	 * {@code int bar}.
	 */
	DEFINE,
	
	/**
	 * Argument to a function.
	 * 
	 * Examples: {@code int boop},
	 */
	FUNCTION_ARGUMENT_TYPE,
	
	/* End. */
	;
	
	/** Enumeration values. */
	static final CTokenSet[] _VALUES =
		CTokenSet.values();
}
