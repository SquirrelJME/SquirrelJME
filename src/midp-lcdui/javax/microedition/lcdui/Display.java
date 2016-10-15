// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.microedition.io.Connector;
import javax.microedition.io.IMCConnection;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer;
import net.multiphasicapps.squirreljme.midp.lcdui.DisplayProperty;
import net.multiphasicapps.squirreljme.midp.lcdui.DisplayProtocol;
import net.multiphasicapps.squirreljme.unsafe.SquirrelJME;

public class Display
{
	public static final int ALERT =
		3;

	public static final int CHOICE_GROUP_ELEMENT =
		2;

	public static final int COLOR_BACKGROUND =
		0;

	public static final int COLOR_BORDER =
		4;

	public static final int COLOR_FOREGROUND =
		1;

	public static final int COLOR_HIGHLIGHTED_BACKGROUND =
		2;

	public static final int COLOR_HIGHLIGHTED_BORDER =
		5;

	public static final int COLOR_HIGHLIGHTED_FOREGROUND =
		3;

	public static final int COLOR_IDLE_BACKGROUND =
		6;

	public static final int COLOR_IDLE_FOREGROUND =
		7;

	public static final int COLOR_IDLE_HIGHLIGHTED_BACKGROUND =
		8;

	public static final int COLOR_IDLE_HIGHLIGHTED_FOREGROUND =
		9;

	public static final int COMMAND =
		5;

	public static final int DISPLAY_HARDWARE_ABSENT =
		2;

	public static final int DISPLAY_HARDWARE_DISABLED =
		1;

	public static final int DISPLAY_HARDWARE_ENABLED =
		0;

	public static final int LIST_ELEMENT =
		1;

	public static final int MENU =
		7;

	/** This is the activity mode that enables power saving inhibition. */
	public static final int MODE_ACTIVE =
		1;
	
	/** This is the activity mode that is the default behavior. */
	public static final int MODE_NORMAL =
		0;

	public static final int NOTIFICATION =
		6;

	public static final int ORIENTATION_LANDSCAPE =
		2;

	public static final int ORIENTATION_LANDSCAPE_180 =
		8;

	public static final int ORIENTATION_PORTRAIT =
		1;

	public static final int ORIENTATION_PORTRAIT_180 =
		4;

	public static final int SOFTKEY_BOTTOM =
		800;

	public static final int SOFTKEY_INDEX_MASK =
		15;

	public static final int SOFTKEY_LEFT =
		820;

	public static final int SOFTKEY_OFFSCREEN =
		880;

	public static final int SOFTKEY_RIGHT =
		860;

	public static final int SOFTKEY_TOP =
		840;

	public static final int STATE_BACKGROUND =
		0;

	public static final int STATE_FOREGROUND =
		2;

	public static final int STATE_VISIBLE =
		1;

	public static final int SUPPORTS_ALERTS =
		32;

	public static final int SUPPORTS_COMMANDS =
		2;

	public static final int SUPPORTS_FILESELECTORS =
		512;

	public static final int SUPPORTS_FORMS =
		4;

	public static final int SUPPORTS_IDLEITEM =
		2048;

	/** This specifies that the display supports user input. */
	public static final int SUPPORTS_INPUT_EVENTS =
		1;

	public static final int SUPPORTS_LISTS =
		64;

	public static final int SUPPORTS_MENUS =
		1024;

	public static final int SUPPORTS_ORIENTATION_LANDSCAPE =
		8192;

	public static final int SUPPORTS_ORIENTATION_LANDSCAPE180 =
		32768;

	public static final int SUPPORTS_ORIENTATION_PORTRAIT =
		4096;

	public static final int SUPPORTS_ORIENTATION_PORTRAIT180 =
		16384;

	public static final int SUPPORTS_TABBEDPANES =
		256;

	public static final int SUPPORTS_TEXTBOXES =
		128;

	public static final int SUPPORTS_TICKER =
		8;

	public static final int SUPPORTS_TITLE =
		16;

	public static final int TAB =
		4;
	
	/** The display descriptor cache. */
	private static final Map<Byte, Reference<Display>> _DISPLAY_CACHE =
		new HashMap<>();
	
	/** The lock for this display. */
	private final Object _lock =
		new Object();
	
	/** The display descriptor. */
	private final byte _descriptor;
	
	/** Properties of the display. */
	private final int[] _properties;
	
	/** The current displayable being shown. */
	private volatile Displayable _show;
	
	/** The displayable to show when the old one is dismissed. */
	private volatile Displayable _dismissed;
	
	/**
	 * Initializes the display instance.
	 *
	 * @param __d The display descriptor.
	 * @param __props The display properties.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	Display(byte __d, int[] __props)
		throws NullPointerException
	{
		// Set
		this._descriptor = __d;
		this._properties = __props.clone();
	}
	
	public void callSerially(Runnable __a)
	{
		throw new Error("TODO");
	}
	
	public boolean flashBacklight(int __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the current activity mode that the display is within, if
	 * active mode is set then the display will inhibit power saving features.
	 *
	 * @return Either {@link #MODE_ACTIVE} or {@link #MODE_NORMAL}.
	 * @since 2016/10/08
	 */
	public int getActivityMode()
	{
		throw new Error("TODO");
	}
	
	public int getBestImageHeight(int __a)
		throws IllegalArgumentException
	{
		return __b
		throw new Error("TODO");
	}
	
	public int getBestImageWidth(int __a)
		throws IllegalArgumentException
	{
		throw new Error("TODO");
	}
	
	public int getBorderStyle(boolean __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * This returns the capabilities that the display supports. This means that
	 * displays which do not support specific widget types can be known so that
	 * potential alternative handling may be performed.
	 *
	 * The capabilities are the constants starting with {@code SUPPORTS_}
	 *
	 * @return A bit field where set bits indicate supported capabilities, if
	 * {@code 0} is returned then only a {@link Canvas} is supported.
	 * @since 2016/10/08
	 */
	public int getCapabilities()
	{
		return this._properties[DisplayProperty.CAPABILITIES.ordinal()];
	}
	
	/**
	 * Returns the color used for the specified interface item.
	 *
	 * The value values are:
	 * {@link #COLOR_BACKGROUND},
	 * {@link #COLOR_BORDER},
	 * {@link #COLOR_FOREGROUND},
	 * {@link #COLOR_HIGHLIGHTED_BACKGROUND},
	 * {@link #COLOR_HIGHLIGHTED_BORDER},
	 * {@link #COLOR_HIGHLIGHTED_FOREGROUND},
	 * {@link #COLOR_IDLE_BACKGROUND},
	 * {@link #COLOR_IDLE_FOREGROUND},
	 * {@link #COLOR_IDLE_HIGHLIGHTED_BACKGROUND}, and
	 * {@link #COLOR_IDLE_HIGHLIGHTED_FOREGROUND}
	 *
	 * @param __c The color to get.
	 * @return The RGB color for the specified user interface item.
	 * @throws IllegalArgumentException If the specified color is not valid.
	 * @since 2016/10/14
	 */
	public int getColor(int __c)
		throws IllegalArgumentException
	{
		// Depends
		DisplayProperty p;
		switch (__c)
		{
			case COLOR_BACKGROUND:
				p = DisplayProperty.COLOR_BACKGROUND;
				break;
				
			case COLOR_BORDER:
				p = DisplayProperty.COLOR_BORDER;
				break;
				
			case COLOR_FOREGROUND:
				p = DisplayProperty.COLOR_FOREGROUND;
				break;
				
			case COLOR_HIGHLIGHTED_BACKGROUND:
				p = DisplayProperty.COLOR_HIGHLIGHTED_BACKGROUND;
				break;
				
			case COLOR_HIGHLIGHTED_BORDER:
				p = DisplayProperty.COLOR_HIGHLIGHTED_BORDER;
				break;
				
			case COLOR_HIGHLIGHTED_FOREGROUND:
				p = DisplayProperty.COLOR_HIGHLIGHTED_FOREGROUND;
				break;
				
			case COLOR_IDLE_BACKGROUND:
				p = DisplayProperty.COLOR_IDLE_BACKGROUND;
				break;
				
			case COLOR_IDLE_FOREGROUND:
				p = DisplayProperty.COLOR_IDLE_FOREGROUND;
				break;
				
			case COLOR_IDLE_HIGHLIGHTED_BACKGROUND:
				p = DisplayProperty.COLOR_IDLE_HIGHLIGHTED_BACKGROUND;
				break;
				
			case COLOR_IDLE_HIGHLIGHTED_FOREGROUND:
				p = DisplayProperty.COLOR_IDLE_HIGHLIGHTED_FOREGROUND;
				break;
				
				// {@squirreljme.error EB08 The specified color is not valid.
				// (The color ID)}
			default:
				throw new IllegalArgumentException(String.format("EB08 %d",
					__c));
		}
		
		// Get color
		return this._properties[p.ordinal()];
	}
	
	public CommandLayoutPolicy getCommandLayoutPolicy()
	{
		throw new Error("TODO");
	}
	
	public int[] getCommandPreferredPlacements(int __ct)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the current displayable.
	 *
	 * @return The current displayable or {@code null} if it is not set.
	 * @since 2016/10/08
	 */
	public Displayable getCurrent()
	{
		// Lock
		synchronized (this._lock)
		{
			return this._show;
		}
	}
	
	public int getDisplayState()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the dot pitch of the display in microns (also known as
	 * micrometers or um).
	 *
	 * If pixels are not square then the pitch should be the average of the
	 * two.
	 *
	 * @return The dot pitch in microns.
	 * @since 2016/10/14
	 */
	public int getDotPitch()
	{
		return this._properties[DisplayProperty.DOT_PITCH.ordinal()];
	}
	
	public int[] getExactPlacementPositions(int __b)
	{
		throw new Error("TODO");
	}
	
	public int getHardwareState()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the height of the display.
	 *
	 * @return The display height.
	 * @since 2016/10/14
	 */
	public int getHeight()
	{
		return this._properties[
			DisplayProperty.DISPLAY_SIZE.ordinal()] & 0xFFFF;
	}
	
	public IdleItem getIdleItem()
	{
		throw new Error("TODO");
	}
	
	public int[] getMenuPreferredPlacements()
	{
		throw new Error("TODO");
	}
	
	public int[] getMenuSupportedPlacements()
	{
		throw new Error("TODO");
	}
	
	public int getOrientation()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the width of the display.
	 *
	 * @retrn The display width.
	 * @since 2016/10/14
	 */
	public int getWidth()
	{
		return this._properties[
			DisplayProperty.DISPLAY_SIZE.ordinal()] >>> 16;
	}
	
	/**
	 * Are mouse/stylus press and release events supported?
	 *
	 * @return {@code true} if they are supported.
	 * @since 2016/10/14
	 */
	public boolean hasPointerEvents()
	{
		return 0 != this._properties[
			DisplayProperty.POINTER_PRESS_RELEASE.ordinal()];
	}
	
	/**
	 * Are mouse/stylus move/drag events supported?
	 *
	 * @return {@code true} if they are supported.
	 * @since 2016/10/14
	 */
	public boolean hasPointerMotionEvents()
	{
		return 0 != this._properties[
			DisplayProperty.POINTER_DRAG_MOVE.ordinal()];
	}
	
	/**
	 * Is this display built into the device or is it an auxiliary display?
	 *
	 * @return {@code true} if it is built-in.
	 * @since 2016/10/14
	 */
	public boolean isBuiltIn()
	{
		return 0 != this._properties[
			DisplayProperty.BUILT_IN.ordinal()];
	}
	
	/**
	 * Is color supported by this display?
	 *
	 * @return {@code true} if color is supported.
	 * @since 2016/10/14
	 */
	public boolean isColor()
	{
		return 0 != this._properties[
			DisplayProperty.IS_COLOR.ordinal()];
	}
	
	/**
	 * Returns the number of alpha-transparency levels.
	 *
	 * Alpha levels range from fully transparent to fully opaue.
	 *
	 * There will always be at least two levels.
	 *
	 * @return The alpha transparency levels.
	 * @since 2016/10/14
	 */
	public int numAlphaLevels()
	{
		return Math.max(2,
			this._properties[DisplayProperty.NUM_ALPHA_LEVELS.ordinal()]);
	}
	
	/**
	 * Returns the number of colors available to the display.
	 *
	 * Monochrome (black and white) displays only have two colors.
	 *
	 * There will always be at least two colors.
	 *
	 * @return The number of available colors.
	 * @since 2016/10/14
	 */
	public int numColors()
	{
		return Math.max(2,
			this._properties[DisplayProperty.NUM_COLORS.ordinal()]);
	}
	
	public void removeCurrent()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the activity mode of the display. If active mode is set then
	 * power saving features are inhibited.
	 *
	 * @param __m The activity mode, either {@link #MODE_ACTIVE} or
	 * {@link #MODE_NORMAL}.
	 * @throws IllegalArgumentException If the specified mode is not valid.
	 * @since 2016/10/08
	 */
	public void setActivityMode(int __m)
		throws IllegalArgumentException
	{
		// Active?
		if (__m == MODE_ACTIVE)
			throw new Error("TODO");
		
		// Normal
		else if (__m == MODE_NORMAL)
			throw new Error("TODO");
		
		// {@squirreljme.error EB02 Unknown activity mode specified.}
		else
			throw new IllegalArgumentException("EB02");
	}
	
	public void setCommandLayoutPolicy(CommandLayoutPolicy __clp)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Shows the given alert on this display.
	 *
	 * @param __show The alert to show.
	 * @param __exit The displayable to show when the alert that is
	 * set is dismissed.
	 * @throws DisplayCapabilityException If the display cannot show the given
	 * displayable.
	 * @throws IllegalStateException If the display hardware is missing; If
	 * the displayables are associated with another display or tab pane. 
	 * @since 2016/10/08
	 */
	public void setCurrent(Alert __show, Displayable __exit)
		throws DisplayCapabilityException, IllegalStateException
	{
		__setCurrent(__show, __exit);
	}
	
	/**
	 * Sets the current displayable to be displayed.
	 *
	 * @param __show The displayable to show.
	 * @throws DisplayCapabilityException If the display cannot show the given
	 * displayable.
	 * @throws IllegalStateException If the display hardware is missing; If
	 * the displayable is associated with another display or tab pane.
	 * @since 2016/10/08
	 */
	public void setCurrent(Displayable __show)
		throws DisplayCapabilityException, IllegalStateException
	{
		// Only use the old display if not an alert
		__setCurrent(__show,
			((__show instanceof Alert) ? getCurrent() : null));
	}
	
	public void setCurrentItem(Item __a)
	{
		throw new Error("TODO");
	}
	
	public void setIdleItem(IdleItem __i)
	{
		throw new Error("TODO");
	}
	
	public void setPreferredOrientation(int __o)
	{
		throw new Error("TODO");
	}
	
	public boolean vibrate(int __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the current item to be displayed.
	 *
	 * @param __show The displayable to show.
	 * @param __exit The displayable to show when the displayable that is
	 * set is dismissed.
	 * @throws DisplayCapabilityException If the display cannot show the given
	 * displayable.
	 * @throws IllegalStateException If the display hardware is missing; If
	 * the displayables are associated with another display or tab pane. 
	 * @since 2016/10/08
	 */
	private void __setCurrent(Displayable __show, Displayable __exit)
		throws DisplayCapabilityException, IllegalStateException
	{
		// If there is nothing to show then the dismissed one is also never
		// shown
		if (__show == null)
			__exit = null;
		
		// Need to lock on both displayables, however they are optional so if
		// they are missing then lock on a dummy object
		Object dummy = ((__show == null || __exit == null) ? new Object() :
			null);
		synchronized (this._lock)
		{
			// Get the old displayable
			Displayable oldshow = this._show;
			Displayable oldexit = this._dismissed;
			
			// Lock on them
			synchronized ((__show != null ? __show._lock : dummy))
			{
				synchronized ((__exit != null ? __exit._lock : dummy))
				{
					// {@squirreljme.error EB03 The displayable is already
					// associated with a display.}
					if (__show != oldshow && __show != null &&
						__show._display != null)
						throw new IllegalStateException("EB03");
				
					// {@squirreljme.error EB04 The displayable to show on
					// dismiss is already associated with a display.}
					if (__exit != oldexit &&
						__exit != null && __exit._display != null)
						throw new IllegalStateException("EB04");
					
					// Unown the old shown displayable
					if (oldshow != null)
						oldshow._display = null;
					
					// Set the new display
					if (__show != null)
						__show._display = this;
					this._show = __show;
					
					// Remove old association
					if (oldexit != null)
						oldexit._display = null;
					
					// Own this one
					if (__exit != null)
						__exit._display = this;
					this._dismissed = __exit;
					
					// Use the title of the given displayable
					if (__show != null)
						__updateTitle(__show.getTitle());
					
					// Change the visibility state depending on whether show
					// exists or not
					throw new Error("TODO");
					/*LCDUIDisplayInstance instance = this._instance;
					instance.setVisible(__show != null);*/
				}
			}
		}
	}
	
	/**
	 * Sets the title to be used by the display.
	 *
	 * @param __s The title to use.
	 * @since 2016/10/08
	 */
	void __updateTitle(String __s)
	{
		throw new Error("TODO");
	}
	
	public static void addDisplayListener(DisplayListener __dl)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Obtains the display that is associated with the given MIDlet.
	 *
	 * @param __m The display to get the midlet for.
	 * @return The display for the given midlet.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public static Display getDisplay(MIDlet __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Use the first display that is available.
		// In the runtime, each program only ever gets a single MIDlet and
		// creating new MIDlets is illegal. Thus since getDisplays() has zero
		// be the return value for this method, that is used here.
		Display[] disp = getDisplays(0);
		if (disp.length > 0)
			return disp[0];
		
		// {@squirreljme.error EB01 Could not get the display for the specified
		// MIDlet because no displays are available.}
		throw new RuntimeException("EB01");
	}
	
	/**
	 * Obtains the displays which have the given capability from all internal
	 * display providers.
	 *
	 * @param __caps The capabities to use, this is a bitfield and the values
	 * include all of the {@code SUPPORT_} prefixed constans. If {@code 0} is
	 * specified then capabilities are not checked.
	 * @return An array containing the displays with these capabilities.
	 * @since 2016/10/08
	 */
	public static Display[] getDisplays(int __caps)
	{
		// Open connection to the display server
		try (IMCConnection sock = (IMCConnection)Connector.open(
			DisplayServer.CLIENT_URI);
			DataInputStream in = sock.openDataInputStream();
			DataOutputStream out = sock.openDataOutputStream())
		{
			// Request number of displays
			out.writeByte(DisplayProtocol.COMMAND_REQUEST_NUMDISPLAYS);
			out.flush();
			
			// Display count can be variable, especially with caps
			List<Display> rv = new ArrayList<>();
			
			// Read the number of displays
			int n = in.readUnsignedByte();
			for (int i = 0; i < n; i++)
			{
				// Read variables
				byte desc = (byte)in.readUnsignedByte();
				
				// Decode properties
				int pcount = DisplayProperty.values().length;
				int[] props = new int[pcount];
				for (;;)
				{
					// Skip stop ID
					int id = in.readUnsignedByte();
					if (id == 0)
						break;
					
					// Read value
					int val = in.readInt();
					
					// Only store if it is in range
					if (id >= 0 && id < pcount)
						props[id] = val;
				}
				
				// Cache descriptor
				Display d = __cacheDisplay(desc, props);
				
				// Do not care about capabilities, always use it
				// Otherwise, the requested ones must be set
				if (__caps == 0 ||
					(d.getCapabilities() & __caps) == __caps)
					rv.add(d);
			}
			
			// Return display mappings
			return rv.<Display>toArray(new Display[rv.size()]);
		}
		
		// If the data cannot be read then use an empty set of displays
		catch (IOException e)
		{
			// Just log it
			e.printStackTrace();
			
			// And use no displays
			return new Display[0];
		}
	}
	
	public static void removeDisplayListener(DisplayListener __dl)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Caches a display descriptor.
	 *
	 * @param __d The descriptor to cache.
	 * @param __props The display properties.
	 * @return The cached display.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	private static Display __cacheDisplay(byte __d, int[] __props)
		throws NullPointerException
	{
		// Check
		if (__props == null)
			throw new NullPointerException("NARG");
		
		// Lock on the cache
		Map<Byte, Reference<Display>> cache = Display._DISPLAY_CACHE;
		synchronized (cache)
		{
			// See if the display has been cached
			Byte b = Byte.valueOf(__d);
			Reference<Display> ref = cache.get(b);
			Display rv;
			
			// Needs to be cached?
			if (ref == null || null == (rv = ref.get()))
				cache.put(b, new WeakReference<>((rv = new Display(__d,
					__props))));
			
			// Used cached value
			return rv;
		}
	}
}

