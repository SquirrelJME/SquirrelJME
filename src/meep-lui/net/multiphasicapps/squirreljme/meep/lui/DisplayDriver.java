// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui;

import javax.microedition.lui.Display;

/**
 * This is used to drive an actual display.
 *
 * @since 2016/09/09
 */
public abstract class DisplayDriver
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The associated display. */
	private volatile Display _display;
	
	/**
	 * Returns whether the display is currently assigned to the hardware in
	 * a way where it obtains user input and is visible.
	 *
	 * @return {@code true} if it is visible and provides user input.
	 * @since 2016/09/11
	 */
	public abstract boolean isHardwareAssigned();
	
	/**
	 * Returns the display screen that the display uses. The LUI interface does
	 * not support resizing so this very much should return the same value
	 * always.
	 *
	 * @return The display screen to use.
	 * @since 2016/09/11
	 */
	public abstract DisplayScreen screen();
	
	/**
	 * Sets whether the display should be assigned to the hardware or
	 * unassigned from it. A display which is assigned to the hardware gets
	 * user input and has display focus.
	 *
	 * @param __h If {@code true} it should be visible and get user input.
	 * @since 2016/09/11
	 */
	public abstract void setHardwareAssigned(boolean __h);
	
	/**
	 * Returns the associated display with this driver.
	 *
	 * @return The associated display or if it is not associated, it will
	 * associate one.
	 * @since 2016/09/09
	 */
	public final Display getDisplay()
	{
		// Lock
		synchronized (this)
		{
			return this._display;
		}
	}
	
	/**
	 * Associates the given display with this driver.
	 *
	 * @param __d The display to associate.
	 * @since 2016/09/09
	 */
	public final void setDisplay(Display __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this)
		{
			// {@squirreljme.error DA05 A display is already associated with
			// this given driver.}
			Display d = this._display;
			if (d != null)
				throw new IllegalStateException("DA05");
		
			// Set
			this._display = __d;
		}
	}
}

