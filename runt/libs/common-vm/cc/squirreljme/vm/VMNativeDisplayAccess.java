// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import javax.microedition.lcdui.Display;

/**
 * This class provides the framebuffer needed by SquirrelJME which is backed
 * on top of LCDUI itself. This just provides a single display.
 *
 * @since 2018/11/17
 */
public class VMNativeDisplayAccess
{
	/** The display to back on, lazily initialized to prevent crashing. */
	private Display _usedisplay;

	/**
	 * Returns the capabilities of the display.
	 *
	 * @param __id The display ID.
	 * @return The capabilities of the display.
	 * @since 2018/11/17
	 */
	public final int capabilities(int __id)
	{
		// Only a single display is supported
		if (__id != 0)
			return 0;
		
		// Just directly pass the capabilities of this display
		return this.__display().getCapabilities();
	}
	
	/**
	 * Is the specified display upsidedown?
	 *
	 * @param __id The ID of the display.
	 * @return If the display is upsidedown.
	 * @since 2018/11/17
	 */
	public final boolean isUpsideDown(int __id)
	{
		if (__id != 0)
			return false;
		
		// Only certain orientations are considered upside-down
		switch (this.__display().getOrientation())
		{
			case Display.ORIENTATION_LANDSCAPE_180:
			case Display.ORIENTATION_PORTRAIT_180:
				return true;
			
				// Not upsidedown
			default:
				return false;
		}
	}
	
	/**
	 * Returns the number of displays which are available.
	 *
	 * @return The number of available displays.
	 * @since 2018/11/17
	 */
	public final int numDisplays()
	{
		return 1;
	}
	
	/**
	 * Returns the display this is currently using.
	 *
	 * @return The currently backed display.
	 * @since 2018/11/17
	 */
	private final Display __display()
	{
		Display rv = this._usedisplay;
		if (rv == null)
			this._usedisplay = (rv = Display.getDisplays(0)[0]);
		return rv;
	}
}

