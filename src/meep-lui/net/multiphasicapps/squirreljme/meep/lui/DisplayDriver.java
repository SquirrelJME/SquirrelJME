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

