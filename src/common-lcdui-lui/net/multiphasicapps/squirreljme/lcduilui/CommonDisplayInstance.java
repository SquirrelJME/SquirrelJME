// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcduilui;

/**
 * This is an instance of a common display driver.
 *
 * @since 2016/10/08
 */
public interface CommonDisplayInstance
{
	/**
	 * This returns the capabilities of a given display, which is dependent
	 * on the display this represents.
	 *
	 * @return The bitflag of supported capabilities.
	 * @since 2016/10/08
	 */
	public abstract int getCapabilities();
	
	/**
	 * Returns {@code true} if the display is in active mode.
	 *
	 * @return {@code true} if in active mode.
	 * @since 2016/10/08
	 */
	public abstract boolean isInActiveMode();
	
	/**
	 * Either sets or clears active mode. Active mode when enabled causes
	 * power saving to not occur, while when it is disabled may cause power
	 * saving related events to occur.
	 *
	 * @param __active If {@code true} then active mode is enabled, otherwise
	 * normal mode is used.
	 * @since 2016/10/08
	 */
	public abstract void setActiveMode(boolean __active);
	
	/**
	 * Sets the title to use for the current display.
	 *
	 * @param __v The title to use, {@code null} clears it.
	 * @since 2016/10/08
	 */
	public abstract void setTitle(String __s);
	
	/**
	 * Should this display be made visible?
	 *
	 * Note that if only one display may exist at a time, it may override
	 * other displays. Also, the focus state is allowed to be modified by a
	 * call to this method.
	 *
	 * @param __v If {@code true} then this display is made active.
	 * @since 2016/10/08
	 */
	public abstract void setVisible(boolean __v);
}

