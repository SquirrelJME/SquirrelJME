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

import java.util.Objects;

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
	
	/** The current display state, defaults to the background. */
	private volatile DisplayState _displaystate =
		DisplayState.BACKGROUND;
	
	/**
	 * Specifies that the display state has changed.
	 *
	 * @param __old The old display state.
	 * @param __new The new display state.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/01
	 */
	protected abstract void displayStateChanged(DisplayState __old,
		DisplayState __new)
		throws NullPointerException;
	
	/**
	 * Specifies that the hardware state has changed.
	 *
	 * @param __old The old hardware state.
	 * @param __new The new hardware state.
	 * @throws NullPointerException If {@code __new} is {@code null}.
	 * @since 2017/10/01
	 */
	protected abstract void hardwareStateChanged(DisplayHardwareState __old,
		DisplayHardwareState __new)
		throws NullPointerException;
	
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
			// display to be disabled always
			DisplayHardwareState hws = getHardwareState();
			if (hws.forceDisabled())
				__ds = DisplayState.BACKGROUND;
			
			// If there is no change in state, then do nothing
			DisplayState oldstate = this._displaystate;
			if (Objects.equals(oldstate, __ds))
				return;
			
			// Set the display state to change, an exception may be thrown
			// to cancel the change
			displayStateChanged(oldstate, __ds);
			this._displaystate = __ds;
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
			// If the hardware state is not changing then ignore
			DisplayHardwareState oldhwstate = this._hwstate;
			if (Objects.equals(oldhwstate, __s))
				return;
			
			// Say that the hardware state changed, an exception can be
			// thrown to disallow it
			hardwareStateChanged(oldhwstate, __s);
			this._hwstate = __s;
		}
	}
}

