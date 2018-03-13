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
 * This is the base class which stores the state for the parser which is used
 * to store incomplete and area specific data for parsing.
 *
 * @since 2018/03/13
 */
abstract class __State__
{
	/** The area being parsed. */
	public final __State__.Area area;
	
	/**
	 * Initializes the base state.
	 *
	 * @param __a The area being parsed.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/13
	 */
	public __State__(__State__.Area __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		this.area = __a;
	}
	
	/**
	 * The area currently being handled.
	 *
	 * @since 2018/03/13
	 */
	public static enum Area
	{
		/** Valid end of file. */
		END_OF_FILE,
		
		/** Parse the package statement. */
		PACKAGE,
		
		/** End. */
		;
	}
}

