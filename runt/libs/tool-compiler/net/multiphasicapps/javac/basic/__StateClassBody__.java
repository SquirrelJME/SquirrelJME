// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

/**
 * This contains the state which is used to parse the class body.
 *
 * @since 2018/03/21
 */
final class __StateClassBody__
	extends __State__
{
	/** The builder to read fields and methods into. */
	protected final BasicClassBuilder builder;
	
	/**
	 * Initializes the class body parser.
	 *
	 * @param __builder Where class parts go.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	__StateClassBody__(BasicClassBuilder __builder)
		throws NullPointerException
	{
		super(__State__.Area.CLASS_BODY);
		
		if (__builder == null)
			throw new NullPointerException("NARG");
		
		this.builder = __builder;
	}
}

