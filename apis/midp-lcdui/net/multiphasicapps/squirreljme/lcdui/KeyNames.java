// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import javax.microedition.lcdui.Canvas;

/**
 * This is used to translate key codes into key names.
 *
 * @since 2017/02/12
 */
public final class KeyNames
{
	/**
	 * Not used.
	 *
	 * @since 2017/02/12
	 */
	private KeyNames()
	{
	}
	
	/**
	 * Returns the name of the given key.
	 *
	 * @param __c The keycode to get the name for.
	 * @return The name of the key.
	 * @throws IllegalArgumentException If the key is not valid.
	 * @since 2017/02/12
	 */
	public static String getKeyName(int __c)
		throws IllegalArgumentException
	{
		throw new Error("TODO");
	}
}

