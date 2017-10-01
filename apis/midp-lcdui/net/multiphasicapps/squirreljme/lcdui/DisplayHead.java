// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

/**
 * This class represents a display head where instances of
 * {@link javax.microedition.Display} are linked to.
 *
 * @since 2017/08/19
 */
public abstract class DisplayHead
{
	/** The current hardware state of this head. */
	private volatile DisplayHardwareState _hwstate;
	
	/**
	 * Returns the current hardware state.
	 *
	 * @return The hardware state.
	 * @throws RuntimeException If the hardware state was not set.
	 * @since 2017/10/01
	 */
	public final DisplayHardwareState getHardwareState()
		throws RuntimeException
	{
		// {@squirreljme.error EB1n The hardware state has not been set by
		// the display driver.}
		DisplayHardwareState rv = this._hwstate;
		if (rv == null)
			throw new RuntimeException("EB1n");
		return rv;
	}
	
	/**
	 * Sets the current state of the display.
	 *
	 * @param __ds The current display state.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/01
	 */
	public final void setState(DisplayState __ds)
		throws NullPointerException
	{
		if (__ds == null)
			throw new NullPointerException("NARG");
		
		// Modifies global state
		synchronized (DisplayManager.GLOBAL_LOCK)
		{
			// If the hardware state is in disabled mode, then force the
			// display to be disabled
			DisplayHardwareState hws = getHardwareState();
			if (hws.forceDisabled())
				__ds = DisplayState.BACKGROUND;
			
			// If there is no change in state, then do nothing
			if (true)
				throw new todo.TODO();
		
			// If the hardware state of this display is in the background or
			// disabled then the only valid display state is in the background
			// and it will never be the foreground
			if (true)
				throw new todo.TODO();
		
			throw new todo.TODO();
		}
	}
	
	/**
	 * Sets the hardware state.
	 *
	 * @param __s The hardware state to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/01
	 */
	protected final void setHardwareState(DisplayHardwareState __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Modifies global state
		synchronized (DisplayManager.GLOBAL_LOCK)
		{
			throw new todo.TODO();
		}
	}
}

