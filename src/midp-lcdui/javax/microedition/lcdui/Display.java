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

import javax.microedition.midlet.MIDlet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.squirreljme.lcduilui.CommonDisplayManager;
import net.multiphasicapps.squirreljme.midp.lcdui.LCDUIDisplay;
import net.multiphasicapps.squirreljme.midp.lcdui.LCDUIDisplayInstance;
import net.multiphasicapps.squirreljme.midp.lcdui.LCDUIDisplayProvider;

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
	
	/** The display manager used to find displays. */
	private static final CommonDisplayManager<Display, LCDUIDisplay,
		LCDUIDisplayProvider> _DISPLAY_MANAGER =
		new CommonDisplayManager<>(Display.class, LCDUIDisplay.class,
		LCDUIDisplayProvider.class);
	
	/** The lock for this display. */
	private final Object _lock =
		new Object();
	
	/** The display instance. */
	private final LCDUIDisplayInstance _instance;
	
	/** The current displayable being shown. */
	private volatile Displayable _show;
	
	/** The displayable to show when the old one is dismissed. */
	private volatile Displayable _dismissed;
	
	/**
	 * Initializes the display instance.
	 *
	 * @param __i The instance of the display.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	Display(LCDUIDisplayInstance __i)
		throws NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._instance = __i;
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
		return (this._instance.isInActiveMode() ? MODE_ACTIVE : MODE_NORMAL);
	}
	
	public int getBestImageHeight(int __a)
	{
		throw new Error("TODO");
	}
	
	public int getBestImageWidth(int __a)
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
		return this._instance.getCapabilities();
	}
	
	public int getColor(int __a)
	{
		throw new Error("TODO");
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
	
	public int getDotPitch()
	{
		throw new Error("TODO");
	}
	
	public int[] getExactPlacementPositions(int __b)
	{
		throw new Error("TODO");
	}
	
	public int getHardwareState()
	{
		throw new Error("TODO");
	}
	
	public int getHeight()
	{
		throw new Error("TODO");
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
	
	public int getWidth()
	{
		throw new Error("TODO");
	}
	
	public boolean hasPointerEvents()
	{
		throw new Error("TODO");
	}
	
	public boolean hasPointerMotionEvents()
	{
		throw new Error("TODO");
	}
	
	public boolean isBuiltIn()
	{
		throw new Error("TODO");
	}
	
	public boolean isColor()
	{
		throw new Error("TODO");
	}
	
	public int numAlphaLevels()
	{
		throw new Error("TODO");
	}
	
	public int numColors()
	{
		throw new Error("TODO");
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
		LCDUIDisplayInstance instance = this._instance;
		if (__m == MODE_ACTIVE)
			instance.setActiveMode(true);
		
		// Normal
		else if (__m == MODE_NORMAL)
			instance.setActiveMode(false);
		
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
					LCDUIDisplayInstance instance = this._instance;
					instance.setVisible(__show != null);
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
		this._instance.setTitle(__s);
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
		// Go through internal display providers
		List<Display> rv = new ArrayList<>();
		for (LCDUIDisplay lcd : Display._DISPLAY_MANAGER)
			if (lcd.hasCapabilities(__caps))
			{
				// If a display has not been initialized, create an instance
				// of it
				Display d = lcd.getRawDisplay();
				if (d == null)
					lcd.bindRawDisplay((d = new Display(
						(LCDUIDisplayInstance)lcd.getInstance())));
				
				// Add it
				rv.add(d);
			}
		
		// Return the list
		return rv.<Display>toArray(new Display[rv.size()]);
	}
	
	public static void removeDisplayListener(DisplayListener __dl)
	{
		throw new Error("TODO");
	}
}


