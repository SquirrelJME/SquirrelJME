// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import javax.microedition.lcdui.Display;

/**
 * Java SE implementation of the native display system using Swing.
 *
 * @since 2018/11/16
 */
public final class NativeDisplayAccess
{
	/**
	 * Returns the capabilities of the display.
	 *
	 * @param __id The display ID.
	 * @return The capabilities of the display.
	 * @since 2018/11/17
	 */
	public static final int capabilities(int __id)
	{
		return Display.SUPPORTS_INPUT_EVENTS |
			Display.SUPPORTS_TITLE |
			Display.SUPPORTS_ORIENTATION_PORTRAIT |
			Display.SUPPORTS_ORIENTATION_LANDSCAPE;
	}
	
	public static final boolean isUpsideDown(int __id)
	{
		return false;
	}
	
	/**
	 * Returns the number of permanent displays which are currently attached to
	 * the system.
	 *
	 * @return The number of displays attached to the system.
	 * @since 2018/11/16
	 */
	public static final int numDisplays()
	{
		// There is ever only a single display that is supported
		return 1;
	}
}

