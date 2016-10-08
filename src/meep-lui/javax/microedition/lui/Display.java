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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import net.multiphasicapps.squirreljme.lcduilui.CommonDisplayManager;
import net.multiphasicapps.squirreljme.meep.lui.DisplayDriver;
import net.multiphasicapps.squirreljme.meep.lui.DisplayProvider;
import net.multiphasicapps.squirreljme.meep.lui.DisplayScreen;
import net.multiphasicapps.util.empty.EmptyIterator;

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
 * text. Setting of text may enable scrolling if it is available.
 *
 * Color selection should always be the closest matching color that is
 * supported on a given display.
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
	 * {@squirreljme.property
	 * net.multiphasicapps.squirreljme.meep.lui.service=class
	 * This selects the class to use as the display service instead of using
	 * the default display service. This may be used for specific systems to
	 * use a given driver if it has a negative or low priority.}
	 */
	private static final String _SERVICE_CLASS =
		"net.multiphasicapps.squirreljme.meep.lui.service";
	
	/** This contains the array of display providers. */
	private static final DisplayProvider[] _DISPLAY_PROVIDERS;
	
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
	
	/** Internal lock. */
	private final Object _lock =
		new Object();
	
	/** The bound display driver. */
	private final DisplayDriver _driver;
	
	/** The screen used to display text. */
	private final DisplayScreen _screen;
	
	/**
	 * This initializes the array of display providers which are used to give
	 * displays to the current application.
	 *
	 * @since 2016/09/07
	 */
	static
	{
		// Build mapping of all display providers
		Map<Class<?>, DisplayProvider> providers =
			new LinkedHashMap<>();
		
		// If the system property was specified then place it first so that
		// it gets its displayed queried first
		String prop = System.getProperty(_SERVICE_CLASS);
		if (prop != null)
			try
			{
				// Find class
				Class<?> cl = Class.forName(prop);
				
				// Create it
				DisplayProvider dp = (DisplayProvider)cl.newInstance();
				
				// Store it
				providers.put(cl, dp);
			}
			
			// Ignore
			catch (ClassCastException|ClassNotFoundException|
				InstantiationException|IllegalAccessException e)
			{
			}
		
		// Add standard services via the service loader
		for (DisplayProvider dp : ServiceLoader.<DisplayProvider>load(
			DisplayProvider.class))
		{
			// Each class is only placed in once
			Class<?> cl = dp.getClass();
			if (!providers.containsKey(cl))
				providers.put(dp.getClass(), dp);
		}
		
		// Set all providers for potential lookup
		_DISPLAY_PROVIDERS = providers.values().<DisplayProvider>toArray(
			new DisplayProvider[providers.size()]);
	}
	
	/**
	 * Displays are internally managed by this class and as such cannot be
	 * constructed publically or by drivers.
	 *
	 * @param __drv The owning driver.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/30
	 */
	Display(DisplayDriver __drv)
		throws NullPointerException
	{
		// Check
		if (__drv == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._driver = __drv;
		
		// {@squirreljme.error DA06 The display driver did not provide a
		// screen for displaying test.}
		DisplayScreen screen = __drv.screen();
		this._screen = screen;
		if (screen == null)
			throw new IllegalStateException("DA06");
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
		// Forward
		return this._driver.isHardwareAssigned();
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
	 * Sets the background color of the display. The closest matching color
	 * should be selected by the display.
	 *
	 * If lighting is supported then {@code __c} being set to {@code null}
	 * turns off the light. Otherwise the light is turned on when a color is
	 * set.
	 *
	 * @param __c The color to set.
	 * @return The color which was used by the display.
	 * @since 2016/08/30
	 */
	public DisplayColor setBackgroundColor(DisplayColor __c)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the characters used on the display. The entire display is used.
	 *
	 * @param __c The charactes to use.
	 * @param __o The offset in the charater array.
	 * @param __l The number of characters to write.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @since 2016/08/30
	 */
	public void setChars(char[] __c, int __o, int __l)
		throws ArrayIndexOutOfBoundsException
	{
		setChars(__c, __o, __l, false, false);
	}
	
	/**
	 * Sets the characters used on the display. The entire display is used.
	 *
	 * @param __c The charactes to use.
	 * @param __o The offset in the charater array.
	 * @param __l The number of characters to write.
	 * @param __blink Should characters blink, if supported?
	 * @param __inverse Should characters be inverted, if supported?
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @since 2016/08/30
	 */
	public void setChars(char[] __c, int __o, int __l, boolean __blink,
		boolean __inverse)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the characters used on the display, on the given line.
	 *
	 * @param __ln The line to set text for.
	 * @param __c The charactes to use.
	 * @param __o The offset in the charater array.
	 * @param __l The number of characters to write.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds; or the line is not within
	 * bounds of the display.
	 * @since 2016/08/30
	 */
	public void setChars(int __ln, char[] __c, int __o, int __l)
		throws ArrayIndexOutOfBoundsException
	{
		setChars(__ln, __c, __o, __l, false, false);
	}
	
	/**
	 * Sets the characters used on the display, on the given line.
	 *
	 * @param __ln The line to set text for.
	 * @param __c The charactes to use.
	 * @param __o The offset in the charater array.
	 * @param __l The number of characters to write.
	 * @param __blink Should characters blink, if supported?
	 * @param __inverse Should characters be inverted, if supported?
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds; or the line is not within
	 * bounds of the display.
	 * @since 2016/08/30
	 */
	public void setChars(int __ln, char[] __c, int __o, int __l,
		boolean __blink, boolean __inverse)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the color of the text on the display.
	 *
	 * @param __c The color to set.
	 * @return The actual color which has been set.
	 * @since 2016/08/30
	 */
	public DisplayColor setCurrentTextColor(DisplayColor __c)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Requests that a display should be assigned to the hardware so that it
	 * can be displayed and receive user input.
	 *
	 * @param __h If {@code true} then it should be assigned to the hardware,
	 * otherwise {@code false} may unassign it.
	 * @since 2016/08/30
	 */
	public void setHardwareAssigned(boolean __h)
	{
		// Forward call
		this._driver.setHardwareAssigned(__h);
	}
	
	/**
	 * Enables or disables horizontal scrolling for a given line.
	 *
	 * If both directions of scrolling is supported then this scrolls left to
	 * right.
	 *
	 * @param __l The line to set scrolling for.
	 * @param __e If {@code true} then scrolling is enabled.
	 * @throws ArrayIndexOutOfBoundsException If the line is not within the
	 * bounds of the display.
	 * @since 2016/08/30
	 */
	public void setHorizontalScrolling(int __l, boolean __e)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Enables or disables horizontal scrolling for a given line.
	 *
	 * @param __l The line to set scrolling for.
	 * @param __e If {@code true} then scrolling is enabled.
	 * @param __dir If {@code true} then text scrolls left to right while
	 * {@code false} means right to left.
	 * @throws ArrayIndexOutOfBoundsException If the line is not within the
	 * bounds of the display.
	 * @since 2016/08/30
	 */
	public void setHorizontalScrolling(int __l, boolean __e, boolean __dir)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the amount of time in milliseconds before the next character is
	 * shifted on the given line.
	 *
	 * @param __l The line to modify the scrolling interval for.
	 * @param __i The number of milliseconds to use for the interval, a value
	 * of zero indicates the fastest scrolling possible.
	 * @throws ArrayIndexOutOfBoundsException If the line is not within the
	 * bounds of the display.
	 * @throws IllegalArgumentException If the interval is negative.
	 * @since 2016/08/30
	 */
	public void setHorizontalScrollingInterval(int __l, int __i)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the color of the light to use for the given display.
	 *
	 * If {@code null} is specified then the light is turned off, otherwise
	 * it is switched on.
	 *
	 * @param __c The color to use for the light.
	 * @return The actually used color.
	 * @since 2016/08/30
	 */
	public DisplayColor setLightingColor(DisplayColor __l)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the characters used on the display. The entire display is used.
	 *
	 * @param __t The text to display.
	 * @since 2016/08/30
	 */
	public void setText(String __t)
	{
		setText(__t, false, false);
	}
	
	/**
	 * Sets the characters used on the display. The entire display is used.
	 *
	 * @param __t The text to display.
	 * @param __blink Should characters blink, if supported?
	 * @param __inverse Should characters be inverted, if supported?
	 * @since 2016/08/30
	 */
	public void setText(String __t, boolean __blink, boolean __inverse)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the characters used on the display, on the given line.
	 *
	 * @param __ln The line to set text for.
	 * @param __t The text to display.
	 * @throws ArrayIndexOutOfBoundsException If the line is not within
	 * bounds of the display.
	 * @since 2016/08/30
	 */
	public void setText(int __ln, String __t)
		throws ArrayIndexOutOfBoundsException
	{
		setText(__ln, __t, false, false);
	}
	
	/**
	 * Sets the characters used on the display, on the given line.
	 *
	 * @param __ln The line to set text for.
	 * @param __t The text to display.
	 * @param __blink Should characters blink, if supported?
	 * @param __inverse Should characters be inverted, if supported?
	 * @throws ArrayIndexOutOfBoundsException If the line is not within
	 * bounds of the display.
	 * @since 2016/08/30
	 */
	public void setText(int __ln, String __t, boolean __blink,
		boolean __inverse)
		throws ArrayIndexOutOfBoundsException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Enables or disables vertical scrolling.
	 *
	 * If either direction is supported then this scrolls top to bottom.
	 *
	 * @param __e If {@code true} then vertical scrolling is enabled.
	 * @since 2016/08/30
	 */
	public void setVerticalScrolling(boolean __e)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Enables or disables vertical scrolling.
	 *
	 * @param __e If {@code true} then vertical scrolling is enabled.
	 * @param __dir If {@code true} then the display scrolls top to bottom,
	 * otherwise {@code false} scrolls bottom to top.
	 * @since 2016/08/30
	 */
	public void setVerticalScrolling(boolean __e, boolean __dir)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the vertical scrolling interval, the amount of time it takes before
	 * the next line is shifted.
	 *
	 * @param __ms The number of milliseconds to wait before lines are shifted.
	 * A value of {@code 0} indicates that the fastest scrolling should be
	 * used.
	 * @throws IllegalArgumentException If the interval is negative.
	 * @since 2016/08/30
	 */
	public void setVerticalScrollingInterval(int __ms)
		throws IllegalArgumentException
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
		// Build an iterator which goes through display provides to find
		// actual displays
		return new __DisplayIterator__(_DISPLAY_PROVIDERS, __ks);
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

