// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * General utilities for JDWP Support.
 *
 * @since 2021/04/11
 */
public final class JDWPUtils
{
	/** Interface bit. */
	private static final int _INTERFACE_BIT = 
		0x0200;
	
	/**
	 * Not used.
	 * 
	 * @since 2021/04/11
	 */
	private JDWPUtils()
	{
	}
	
	/**
	 * Returns the type that the given class is.
	 * 
	 * @param __controller The controller.
	 * @param __class The class to check.
	 * @return The type that the class is.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	public static JDWPClassType classType(JDWPController __controller,
		Object __class)
		throws NullPointerException
	{
		if (__controller == null)
			throw new NullPointerException("NARG");
		
		// Array type?
		if (__controller.viewType().signature(__class).startsWith("["))
			return JDWPClassType.ARRAY;
		
		// Is this potentially an interface?
		int flags = __controller.viewType().flags(__class);
		if ((flags & JDWPUtils._INTERFACE_BIT) != 0)
			return JDWPClassType.INTERFACE;
		
		// Just a plain class
		return JDWPClassType.CLASS;
	}
}
