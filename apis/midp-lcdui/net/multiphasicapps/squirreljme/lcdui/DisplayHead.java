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
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import net.multiphasicapps.squirreljme.lcdui.widget.DisplayableWidget;

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
	
	/** The current displayable attached to this head. */
	private volatile DisplayableWidget _current;
	
	/**
	 * Returns the display height in millimeters.
	 *
	 * @return The display height in millimeters.
	 * @since 2017/10/20
	 */
	public abstract int displayHeightMillimeters();
	
	/**
	 * Returns the display height in pixels.
	 *
	 * @return The display height in pixels.
	 * @since 2017/10/20
	 */
	public abstract int displayHeightPixels();
	
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
	 * Returns the display width in millimeters.
	 *
	 * @return The display width in millimeters.
	 * @since 2017/10/20
	 */
	public abstract int displayWidthMillimeters();
	
	/**
	 * Returns the display width in pixels.
	 *
	 * @return The display width in pixels.
	 * @since 2017/10/20
	 */
	public abstract int displayWidthPixels();
	
	/**
	 * Converts the specified abstract font size to a pixel size.
	 *
	 * @param __s The abstract font size to convert.
	 * @return The pixel size of the font.
	 * @since 2017/10/20
	 */
	public abstract int fontSizeToPixelSize(int __s);
	
	/**
	 * Returns the graphics object which can draw on the entire screen.
	 *
	 * @return The fullscreen graphics object.
	 * @since 2017/10/24
	 */
	public abstract Graphics fullscreenGraphics();
	
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
	 * Is this display head in color?
	 *
	 * @return If this display head is in color, otherwise it will be
	 * grayscale.
	 * @since 2017/10/20
	 */
	public abstract boolean isColor();
	
	/**
	 * Returns the number of colors which are supported in this display head.
	 *
	 * @return The number of supported colors.
	 * @since 2017/10/20
	 */
	public abstract int numColors();
	
	/**
	 * This registers that the given displayable is to be displayed on this
	 * head.
	 *
	 * @param __d The displayable to display, may be {@code null} to clear it.
	 * @since 2017/10/25
	 */
	protected abstract void registerCurrent(DisplayableWidget __d);
	
	/**
	 * Returns the DPI of the display.
	 *
	 * @return The DPI of the display.
	 * @since 2017/10/20
	 */
	public final int displayDpi()
	{
		// dpi = (pixels / mm) * 25.4
		double dw = ((double)displayWidthPixels() /
				(double)displayWidthMillimeters()) * 25.4,
			dh = ((double)displayHeightPixels() /
				(double)displayHeightMillimeters()) * 25.4;
		return (int)((dw + dh) / 2.0);
	}
	
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
	 * Sets the current displayable.
	 *
	 * @param __d The displayable to show, if {@code null} then the current
	 * display is to have its current displayable removed.
	 * @throws IllegalStateException If there is already a current displayable
	 * and one is being set.
	 * @since 2017/10/25
	 */
	public final void setCurrent(DisplayableWidget __d)
		throws IllegalStateException
	{
		// Modifies global state
		synchronized (DisplayManager.GLOBAL_LOCK)
		{
			// Clearing the current displayable?
			if (__d == null)
			{
				// Clearing nothing, do nothing
				if (this._current == null)
					return;
				
				throw new todo.TODO();
			}
			
			// Setting a current displayable
			else
			{
				// {@squirreljme.error EB1u Cannot set a new current
				// displayable because there is already one being displayed.}
				if (this._current != null)
					throw new IllegalStateException("EB1u");
			
				// Set and register
				this._current = __d;
				registerCurrent(__d);
			}
		}
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

