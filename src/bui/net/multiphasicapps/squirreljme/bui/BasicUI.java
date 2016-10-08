// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bui;

/**
 * This is the base class for basic UI drivers.
 *
 * @since 2016/10/08
 */
public interface BasicUI
{
	/**
	 * Returns {@code true} if the display is in active mode.
	 *
	 * @return {@code true} if in active mode.
	 * @since 2016/10/08
	 */
	public abstract boolean isInActiveMode();
	
	/**
	 * Sets whether or not active mode should be used. When active mode is
	 * enabled, power saving events are suppressed. Otherwise when it is
	 * disabled the UI screen may enter power saving mode (such as
	 * shutting the screen off or using a screensaver).
	 *
	 * @param __active If {@code true} then active mode is enabled.
	 * @since 2016/10/08
	 */
	public abstract void setActiveMode(boolean __active);
	
	/**
	 * Are input events supported by this basic UI?
	 *
	 * @return {@code true} if input events are supported.
	 * @since 2016/10/08
	 */
	public abstract boolean supportsInputEvents();
}

