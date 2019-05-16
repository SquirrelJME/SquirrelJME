// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;

/**
 * This interface is used to represent the backend that the UI uses to draw
 * onto the target device. This may be an image or an actual UI.
 *
 * @since 2019/05/16
 */
public interface PhoneDisplayBackend
{
	/**
	 * Returns if the display is upside-down, this is used for orientation
	 * purposes.
	 *
	 * @return If the display is upside-down.
	 * @since 2019/05/16
	 */
	public abstract boolean isUpsidedown();
	
	/**
	 * Returns the pixel format of the display.
	 *
	 * @return The display pixel format.
	 * @since 2019/05/16
	 */
	public abstract PixelFormat pixelFormat();
}

