// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lui;

import java.util.Iterator;

/**
 * This provides an interface to basic text based displays.
 *
 * There may be multiple displays associated with a device and the displays
 * that are available may change at any rate. It is possible for a display to
 * support user input, which in this case it will generate key events.
 *
 * A single hardware display may be shared by multiple instances of these
 * classes, which one is actually visible is implementation dependent.
 *
 * Primary displays are ones that will never be removed. Primary displays
 * are always listed first.
 *
 * Auxiliary displays are ones that may be dynamically added and removed.
 *
 * Internally to SquirrelJME, display drivers provide an implementation of
 * the methods here. If a display supports user input then it must implement
 * the {@link javax.microedition.key.InputDevice} interface.
 *
 * @since 2016/08/30
 */
public class Display
{
	/**
	 * This is used as the event name to indicate the lighting level.
	 *
	 * The value will be either {@link #LIGHTING_ON}, {@link #LIGHTING_OFF},
	 * or {@link #LIGHTING_DIM}.
	 */
	public static final String LIGHTING =
		"LIGHTING";
	
	/**
	 * This indicates that the light level is not fully bright nor off, but
	 * is somewhere inbetween.
	 */
	public static final int LIGHTING_DIM =
		2;
	
	/** This indicates that the light level for a display is zero (off). */
	public static final int LIGHTING_OFF =
		0;
	
	/** The indicates that the light level for a display is bright. */
	public static final int LIGHTING_ON =
		1;
	
	/**
	 * This is used to specify that any power saving actions for a given
	 * display are to be deferred for as long as possible to increase the
	 * amount of time that the display is visible for.
	 */
	public static final int MODE_ACTIVE =
		1;
	
	/**
	 * This is used to specify that the display may enter a power saving mode
	 * for example when the device is idle and no input has been generated.
	 */
	public static final int MODE_NORMAL =
		0;
	
	/**
	 * This is used to register a listener for when displays are added,
	 * removed, or their state has been changed.
	 *
	 * @param __l The listener to add, if it has already been added then this
	 * has no effect.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/30
	 */
	public static void addDisplayListener(DisplayListener __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * This returns an iterator containing any displays which are available
	 * for usage. Primary displays are always returned first.
	 *
	 * The returned iterator will be empty if all displays are taken by other
	 * applications or if they are non-attached auxiliary displays.
	 *
	 * It is recommended to call {@link #isHardwareAssigned()} to determine
	 * if the display is associated with the underlying hardware.
	 *
	 * @param __ks If {@code true} then only displays which support key input
	 * events will be returned, otherwise all displays will be used.
	 * @return An iterator over the displays which are available, the returned
	 * iterator may be empty (if there are no displays) and will never contain
	 * the same display multiple times.
	 * @since 2016/08/30
	 */
	public static Iterator<Display> getDisplays(boolean __ks)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Removes the specified listener so that it no longer receives display
	 * events.
	 *
	 * @param __l The listener to remove, if it has never been added then this
	 * has no effect.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/30
	 */
	public static void removeDisplayListener(DisplayListener __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

