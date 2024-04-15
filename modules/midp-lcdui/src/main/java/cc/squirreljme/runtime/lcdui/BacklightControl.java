// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Control utilities for the backlight.
 *
 * @since 2021/11/30
 */
public final class BacklightControl
{
	/** The minimum backlight level. */
	public static final byte MIN_LEVEL =
		0;
	
	/** The maximum backlight level. */
	public static final byte MAX_LEVEL =
		100;
	
	/**
	 * Sets the level of the backlight.
	 * 
	 * @param __level The backlight level, if outside of bounds it will be
	 * capped accordingly.
	 * @since 2021/11/30
	 */
	public static void setLevel(int __level)
	{
		Debugging.todoNote("Implement backlight set: %d", __level);
	}
}
