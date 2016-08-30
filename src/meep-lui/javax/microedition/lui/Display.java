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
import java.util.ServiceLoader;

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
 * Internally to SquirrelJME, this class acts as a wrapper around display
 * driver interfaces which are implemented via the service loader.
 * If a display supports user input then it must implement
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
	 * Displays are internally managed by this class and as such cannot be
	 * constructed.
	 *
	 * @since 2016/08/30
	 */
	Display()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the color which represents the color which is actually being
	 * display by a given display.
	 *
	 * If background colors are not supported then this returns the default
	 * background color.
	 *
	 * @return The closest matching background color.
	 * @since 2016/08/30
	 */
	public DisplayColor getBackgroundColor()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the number of characters which appear on a single line.
	 *
	 * @return The characters per line.
	 * @since 2016/08/30
	 */
	public int getCharacterNumberPerLine()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Reads all of the characters on a given display starting at index zero.
	 *
	 * If the array contains more characters than the display then the extra
	 * characters remain untouched.
	 *
	 * This only returns text which has been set to a display up to the bounds
	 * of lines and columns, not what is actually displayed. This means that
	 * text which is scrolled off the display will be returned and that text
	 * which could not be set on a display to not be returned.
	 *
	 * It is recommended to pass an array that is large enough to read all
	 * characters from the display.
	 *
	 * @param __o The output array to read characters into.
	 * @return The number of read characters.
	 * @throws ArrayIndexOutOfBoundsException If the array is too small to
	 * fit every character.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/30
	 */
	public int getChars(char[] __o)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Reads all of the characters on a given line starting at index zero.
	 *
	 * If the array contains more characters than the display then the extra
	 * characters remain untouched.
	 *
	 * This only returns text which has been set to a display up to the bounds
	 * of columns, not what is actually displayed. This means that
	 * text which is scrolled off the display will be returned and that text
	 * which could not be set on a display to not be returned.
	 *
	 * It is recommended to pass an array that is large enough to read all
	 * characters from the display.
	 *
	 * @param __l The line number to get characters from.
	 * @param __o The output array to read characters into.
	 * @return The number of read characters.
	 * @throws ArrayIndexOutOfBoundsException If the array is too small to
	 * fit the entire line; or the line exceeds the bounds of the display.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/30
	 */
	public int getChars(int __l, char[] __o)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
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

