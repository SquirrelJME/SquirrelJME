// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

/**
 * Defines a MIDlet that can be launched.
 *
 * @since 2020/02/15
 */
public final class JavaMEMidlet
{
	/**
	 * Initializes the MIDlet information.
	 *
	 * @param __title The title of the MIDlet.
	 * @param __icon The icon of the MIDlet.
	 * @param __main The main class.
	 * @throws NullPointerException If no title or main class was specified.
	 * @since 2020/02/15
	 */
	public JavaMEMidlet(String __title, String __icon, String __main)
		throws NullPointerException
	{
		if (__title == null || __main == null)
			throw new NullPointerException("No title or main specified.");
		
		throw new Error("TODO");
	}
}

