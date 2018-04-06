// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.ui;

import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;

/**
 * This represents the single display head that is provided for clients to
 * use and share with each other when displaying graphics. Since only a single
 * application may show something at a time, there is {@link UiDisplay} which
 * provides a constant widget that represents the display used
 *
 * @since 2018/04/04
 */
public interface UiDisplayHead
	extends UiInterface, UiWidget
{
	/**
	 * Returns the maximum display height in pixels.
	 *
	 * @return The maximum display height in pixels.
	 * @since 2017/10/27
	 */
	public abstract int displayMaximumHeightPixels();
	
	/**
	 * Returns the maximum display width in pixels.
	 *
	 * @return The maximum display width in pixels.
	 * @since 2017/10/27
	 */
	public abstract int displayMaximumWidthPixels();
	
	/**
	 * Returns the physical display height in millimeters.
	 *
	 * @return The physical display height in millimeters.
	 * @since 2017/10/27
	 */
	public abstract int displayPhysicalHeightMillimeters();
	
	/**
	 * Returns the physical display width in millimeters.
	 *
	 * @return The physical display width in millimeters.
	 * @since 2017/10/27
	 */
	public abstract int displayPhysicalWidthMillimeters();
	
	/**
	 * Returns the client display which is currently being shown.
	 *
	 * @return The client display being shown or {@code null} if there is none.
	 * @since 2018/04/04
	 */
	public abstract UiDisplay getCurrentDisplay();
	
	/**
	 * Returns the pixel format that is being used by the display.
	 *
	 * @return The pixel format to use.
	 * @since 2018/04/06
	 */
	public abstract PixelFormat pixelFormat();
	
	/**
	 * Sets the current display to be shown.
	 *
	 * @param __d The display to show, {@code null} will clear it.
	 * @since 2018/04/04
	 */
	public abstract void setCurrentDisplay(UiDisplay __d);
	
	/**
	 * Vibrates the device for the given duration.
	 *
	 * @param __ms The number of milliseconds to vibrate for.
	 * @since 2018/04/04
	 */
	public abstract void vibrate(int __ms);
}

