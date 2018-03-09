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
 * This is the base class for context sensitive parsers for input
 * tokens. This interface is just used as a base for state storage.
 *
 * @since 2018/03/07
 */
abstract class __At__
{
	/** The context area. */
	public final ContextArea area;
	
	/**
	 * Initializes the base context.
	 *
	 * @param __a The area this is in.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/07
	 */
	__At__(ContextArea __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		this.area = __a;
	}
}

