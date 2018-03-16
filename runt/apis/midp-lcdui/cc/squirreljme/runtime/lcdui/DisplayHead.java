// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.lcdui.event.EventQueue;
import java.util.Objects;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

/**
 * This class represents a display head where instances of
 * {@link javax.microedition.Display} are linked to.
 *
 * @since 2017/08/19
 */
@Deprecated
public abstract class DisplayHead
{
	/** The current hardware state of this head. */
	private volatile DisplayHardwareState _hwstate;
	
	/** The current display state, defaults to the background. */
	private volatile DisplayState _displaystate =
		DisplayState.BACKGROUND;
	
	/**
	 * Returns the physical display height in millimeters.
	 *
	 * @return The physical display height in millimeters.
	 * @since 2017/10/27
	 */
	public abstract int displayPhysicalHeightMillimeters();
	
	/**
	 * Returns the physical display height in pixels.
	 *
	 * @return The physical display height in pixels.
	 * @since 2017/10/27
	 */
	public abstract int displayPhysicalHeightPixels();
	
	/**
	 * Returns the physical display width in millimeters.
	 *
	 * @return The physical display width in millimeters.
	 * @since 2017/10/27
	 */
	public abstract int displayPhysicalWidthMillimeters();
	
	/**
	 * Returns the physical display width in pixels.
	 *
	 * @return The physical display width in pixels.
	 * @since 2017/10/27
	 */
	public abstract int displayPhysicalWidthPixels();
	
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
	 * Returns the virtual display height in pixels.
	 *
	 * @return The virtual display height in pixels.
	 * @since 2017/10/27
	 */
	public abstract int displayVirtualHeightPixels();
	
	/**
	 * Returns the virtual display width in pixels.
	 *
	 * @return The virtual display width in pixels.
	 * @since 2017/10/27
	 */
	public abstract int displayVirtualWidthPixels();
	
	/**
	 * Converts the specified abstract font size to a pixel size.
	 *
	 * @param __s The abstract font size to convert.
	 * @return The pixel size of the font.
	 * @since 2017/10/20
	 */
	public abstract int fontSizeToPixelSize(int __s);
	
	/**
	 * Returns the graphics object which can draw on this display head
	 *
	 * @return The graphics instance for drawing on this display head.
	 * @since 2017/10/24
	 */
	public abstract Graphics graphics();
	
	/**
	 * This is called when the graphics on the display head have been painted.
	 *
	 * @since 2017/10/27
	 */
	public abstract void graphicsPainted();
	
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
	 * Returns the display head identifier.
	 *
	 * @return The ID of the current display head.
	 * @since 2017/10/27
	 */
	public abstract int headId();
	
	/**
	 * Is this display head in color?
	 *
	 * @return If this display head is in color, otherwise it will be
	 * grayscale.
	 * @since 2017/10/20
	 */
	public abstract boolean isColor();
	
	/**
	 * Checks whether the given orientation is a natural orientation.
	 *
	 * Natural means that the framebuffer is in the given orientation and that
	 * it is not required to rotate the framebuffer in software.
	 *
	 * @param __o The orientation to check.
	 * @return If the given orientation is a natural one.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/27
	 */
	public abstract boolean isNaturalOrientation(DisplayOrientation __o)
		throws NullPointerException;
	
	/**
	 * Returns the number of colors which are supported in this display head.
	 *
	 * @return The number of supported colors.
	 * @since 2017/10/20
	 */
	public abstract int numColors();
	
	/**
	 * Returns the current display orientation.
	 *
	 * @return The display orientation.
	 * @since 2017/10/27
	 */
	public abstract DisplayOrientation orientation();
	
	/**
	 * Returns the DPI of the display.
	 *
	 * @return The DPI of the display.
	 * @since 2017/10/20
	 */
	public final int displayDpi()
	{
		// dpi = (pixels / mm) * 25.4
		float dw = ((float)displayPhysicalWidthPixels() /
				(float)displayPhysicalWidthMillimeters()) * 25.4F,
			dh = ((float)displayPhysicalHeightPixels() /
				(float)displayPhysicalHeightMillimeters()) * 25.4F;
		return (int)((dw + dh) / 2.0F);
	}
	
	/**
	 * Returns the current display state.
	 *
	 * @return The display state.
	 * @since 2017/10/25
	 */
	public final DisplayState displayState()
	{
		return this._displaystate;
	}
	
	/**
	 * Returns the virtual display height in millimeters.
	 *
	 * @return The virtual display height in millimeters.
	 * @since 2017/10/27
	 */
	public final int displayVirtualHeightMillimeters()
	{
		return (int)(displayPhysicalHeightMillimeters() * 
			((float)displayVirtualHeightPixels() /
			(float)displayPhysicalHeightPixels()));
	}
	
	/**
	 * Returns the virtual display width in millimeters.
	 *
	 * @return The virtual display width in millimeters.
	 * @since 2017/10/27
	 */
	public final int displayVirtualWidthMillimeters()
	{
		return (int)(displayPhysicalWidthMillimeters() * 
			((float)displayVirtualWidthPixels() /
			(float)displayPhysicalWidthPixels()));
	}
	
	/**
	 * Returns the event queue.
	 *
	 * @return The event queue.
	 * @since 2017/10/27
	 */
	public final EventQueue eventQueue()
	{
		return DisplayManager.DISPLAY_MANAGER.eventQueue();
	}
	
	/**
	 * Returns the current hardware state.
	 *
	 * @return The hardware state.
	 * @throws RuntimeException If the hardware state was not set.
	 * @since 2017/10/01
	 */
	public final DisplayHardwareState hardwareState()
		throws RuntimeException
	{
		// {@squirreljme.error EB01 The hardware state has not been set by
		// the display driver.}
		DisplayHardwareState rv = this._hwstate;
		if (rv == null)
			throw new RuntimeException("EB01");
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
			DisplayHardwareState hws = hardwareState();
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
	
	/**
	 * Vibrates the device associated with this display head.
	 *
	 * @param __d The number of milliseconds to vibrate for, if zero the
	 * vibrator will be switched off.
	 * @return {@code true} if the vibrator is controllable.
	 * @since 2017/10/25
	 */
	public boolean vibrate(int __d)
	{
		// Default implemenation does nothing
		return false;
	}
}

