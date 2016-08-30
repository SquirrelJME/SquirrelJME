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
 * Set text on a display may be lost depending on the implementation such as
 * if it implements scrolling in a specific fashion or the text does not fit.
 * As such, obtained text may be what is visible or might be what was not
 * truncated when set. Thus if {@code "SquirrelJME"} was set to the display
 * and scrolling was enabled, then it might return that text or something
 * such as {@code "JMESquirrel"}. An exception to this is line breaks where
 * if a line contains too much text it flows onto the next line, in this case
 * the text is treated as a single line and the break is not in the returned
 * text.
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
	 * Returns the text color that is currently used on the display. If text
	 * colors are not supported then the default color is returned.
	 *
	 * @return The text color which is used on the display.
	 * @since 2016/08/30
	 */
	public DisplayColor getCurrentTextColor()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the default background color.
	 *
	 * @return The default background color.
	 * @since 2016/08/30
	 */
	public DisplayColor getDefaultBackgroundColor()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the default lighting color.
	 *
	 * @return The default lighting color.
	 * @since 2016/08/30
	 */
	public DisplayColor getDefaultLightingColor()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the default text color.
	 *
	 * @return The default text color.
	 * @since 2016/08/30
	 */
	public DisplayColor getDefaultTextColor()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the number of milliseconds between character shifts for the
	 * given line.
	 *
	 * If the display does not support horizontal scrolling then the value
	 * returned here is meaningless.
	 *
	 * @param __l The line to get the scrolling interval of.
	 * @throws ArrayIndexOutOfBoundsException If the specified line is not
	 * within the bounds of the display.
	 * @since 2016/08/30
	 */
	public int getHorizontalScrollingInterval(int __l)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the identification of the given display. It is recommended for
	 * the same hardware displays to return the same ID even if the given
	 * {@link Display} object has changed.
	 *
	 * @return The display identification.
	 * @since 2016/08/30
	 */
	public String getId()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the current lighting color of the display. If lighting is not
	 * supported then this will return the default color.
	 *
	 * @return The currently set lighting color.
	 * @since 2016/08/30
	 */
	public DisplayColor getLightingColor()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the number of lines which are available for the display.
	 *
	 * @return The number of lines the display uses.
	 * @since 2016/08/30
	 */
	public int getNumberOfLines()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the current text which has previously been set.
	 *
	 * @return The current text display.
	 * @since 2016/08/30
	 */
	public String getText()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the current text which has previously been set on a given line.
	 *
	 * @param __l The line to get the text for.
	 * @return The text on the given line.
	 * @throws ArrayIndexOutOfBoundsException If the line is not within the
	 * bounds of the display.
	 * @since 2016/08/30
	 */
	public String getText(int __l)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the number of milliseconds that must pass before lines are
	 * shifted. If vertical scrolling is not supported then the return value is
	 * meaningless.
	 *
	 * @return The interval used for vertical scrolling.
	 * @since 2016/08/30
	 */
	public int getVerticalScrollingInterval()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if background colors are supported.
	 *
	 * @return {@code true} if background colors are supported.
	 * @since 2016/08/30
	 */
	public boolean isBackgroundColorsSupported()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if this display is built-in and is not auxiliary
	 * meaning that the display will never be removed. Otherwise {@code false}
	 * indicates that it may disappear.
	 *
	 * @return {@code true} if the display is built-in.
	 * @since 2016/08/30
	 */
	public boolean isBuiltIn()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This returns {@code true} if the display has access to the underlying
	 * hardware, which means it is capable of receiving user input and is
	 * currently displayed to the user.
	 *
	 * @return {@code true} if the display has access to the underlying
	 * hardware.
	 * @since 2016/08/30
	 */
	public boolean isHardwareAssigned()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if the specified line has horizontal scrolling
	 * enabled. If horizontal scrolling is not enabled then the return value
	 * is meaningless.
	 *
	 * @param __l The line to check.
	 * @return {@code true} if horizontal scrolling is enabled for the given
	 * line.
	 * @throws ArrayIndexOutOfBoundsException If the line is not within the
	 * bounds of the display.
	 * @since 2016/08/30
	 */
	public boolean isHorizontalScrollingEnabled(int __l)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if horizontal scrolling is supported on the
	 * display.
	 *
	 * @return {@code true} if horizontal scrolling is supported.
	 * @since 2016/08/30
	 */
	public boolean isHorizontalScrollingSupported()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns true if the color of the lighting color is able to be changed.
	 *
	 * @return {@code true} if the lighting color can be changed.
	 * @since 2016/08/30
	 */
	public boolean isLightingColorsSupported()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if lighting is supported.
	 *
	 * @return {@code true} if lighting is supported.
	 * @since 2016/08/30
	 */
	public boolean isLightingSupported()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if text colors are supported.
	 *
	 * @return {@code true} if text colors are supported.
	 * @since 2016/08/30
	 */
	public boolean isTextColorsSupported()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if vertical scrolling is enabled.
	 *
	 * @return {@code true} if vertical scrolling is enabled.
	 * @since 2016/08/30
	 */
	public boolean isVerticalScrollingEnabled()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if vertical scrolling is supported.
	 *
	 * @return {@code true} if vertical scrolling is supported.
	 * @since 2016/08/30
	 */
	public boolean isVerticalScrollingSupported()
	{
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

