// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.service.ServiceCaller;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.DisplayOrientation;
import cc.squirreljme.runtime.lcdui.DisplayState;
import cc.squirreljme.runtime.lcdui.LcdDisplayableTakenException;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdServiceCall;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.microedition.midlet.MIDlet;

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
	
	/** The displays which currently exist based on their index. */
	private static final Map<Integer, Reference<Display>> _DISPLAYS =
		new HashMap<>();
	
	/** The index of this display. */
	final int _index;
	
	/** Hold on the displayable to show. */
	private volatile Displayable _heldcurrent;
	
	/** Hold on the displayable to show on exit. */
	private volatile Displayable _heldexit;
	
	/**
	 * Initializes the display instance.
	 *
	 * @param __dx The display index.
	 * @since 2018/03/16
	 */
	Display(int __dx)
	{
		this._index = __dx;
	}
	
	public void callSerially(Runnable __a)
	{
		// Note that the Runnable.run() will be called as if it were serialized
		// like everything else with @__SerializedEvent__
		throw new todo.TODO();
	}
	
	public boolean flashBacklight(int __a)
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	/**
	 * Returns the height of the image that should be used for the given
	 * display element.
	 *
	 * Valid elements are:
	 * {@link #LIST_ELEMENT},
	 * {@link #CHOICE_GROUP_ELEMENT},
	 * {@link #ALERT},
	 * {@link #TAB},
	 * {@link #COMMAND},
	 * {@link #NOTIFICATION}, and
	 * {@link #MENU}.
	 *
	 * @param __a If display element.
	 * @return The height of the image for that element.
	 * @throws IllegalArgumentException On null arguments.
	 * @since 2016/10/14
	 */
	public int getBestImageHeight(int __a)
		throws IllegalArgumentException
	{
		return __bestImageSize(__a) & 0xFFFF;
	}
	
	/**
	 * Returns the width of the image that should be used for the given
	 * display element.
	 *
	 * Valid elements are:
	 * {@link #LIST_ELEMENT},
	 * {@link #CHOICE_GROUP_ELEMENT},
	 * {@link #ALERT},
	 * {@link #TAB},
	 * {@link #COMMAND},
	 * {@link #NOTIFICATION}, and
	 * {@link #MENU}.
	 *
	 * @param __a If display element.
	 * @return The width of the image for that element.
	 * @throws IllegalArgumentException On null arguments.
	 * @since 2016/10/14
	 */
	public int getBestImageWidth(int __a)
		throws IllegalArgumentException
	{
		return __bestImageSize(__a) >>> 16;
	}
	
	public int getBorderStyle(boolean __a)
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
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
		throw new todo.TODO();
		/*
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
				
			case COLOR_FORsEGROUND:
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
				
				// {@squirreljme.error EB17 The specified color is not valid.
				// (The color ID)}
			default:
				throw new IllegalArgumentException(String.format("EB17 %d",
					__c));
		}
		
		// Get color
		return this._properties[p.ordinal()];
		*/
	}
	
	public CommandLayoutPolicy getCommandLayoutPolicy()
	{
		throw new todo.TODO();
	}
	
	public int[] getCommandPreferredPlacements(int __ct)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the current displayable.
	 *
	 * @return The current displayable or {@code null} if it is not set.
	 * @since 2016/10/08
	 */
	public Displayable getCurrent()
	{
		throw new todo.TODO();
	}
	
	public int getDisplayState()
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	public int[] getExactPlacementPositions(int __b)
	{
		throw new todo.TODO();
	}
	
	public int getHardwareState()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the maximum height of the display.
	 *
	 * @return The maximum display height.
	 * @since 2016/10/14
	 */
	public int getHeight()
	{
		throw new todo.TODO();
		/*
		return this._head.displayPhysicalHeightPixels();*/
	}
	
	public IdleItem getIdleItem()
	{
		throw new todo.TODO();
	}
	
	public int[] getMenuPreferredPlacements()
	{
		throw new todo.TODO();
	}
	
	public int[] getMenuSupportedPlacements()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the current orientation of the display.
	 *
	 * @return The display orientation.
	 * @since 2017/10/27
	 */
	public int getOrientation()
	{
		throw new todo.TODO();
		/*
		return __drawSpace().orientation().lcduiValue();*/
	}
	
	/**
	 * Returns the maximum width of the display.
	 *
	 * @retrn The maximum display width.
	 * @since 2016/10/14
	 */
	public int getWidth()
	{
		throw new todo.TODO();
		/*
		return this._head.displayPhysicalWidthPixels();*/
	}
	
	/**
	 * Are mouse/stylus press and release events supported?
	 *
	 * @return {@code true} if they are supported.
	 * @since 2016/10/14
	 */
	public boolean hasPointerEvents()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Are mouse/stylus move/drag events supported?
	 *
	 * @return {@code true} if they are supported.
	 * @since 2016/10/14
	 */
	public boolean hasPointerMotionEvents()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Is this display built into the device or is it an auxiliary display?
	 *
	 * @return {@code true} if it is built-in.
	 * @since 2016/10/14
	 */
	public boolean isBuiltIn()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Is color supported by this display?
	 *
	 * @return {@code true} if color is supported.
	 * @since 2016/10/14
	 */
	public boolean isColor()
	{
		throw new todo.TODO();
		/*
		return this._head.isColor();*/
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
		throw new todo.TODO();
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
		throw new todo.TODO();
		/*
		return this._head.numColors();*/
	}
	
	public void removeCurrent()
	{
		throw new todo.TODO();
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
			throw new todo.TODO();
	
		// Normal
		else if (__m == MODE_NORMAL)
			throw new todo.TODO();
	
		// {@squirreljme.error EB18 Unknown activity mode specified.}
		else
			throw new IllegalArgumentException("EB18");
	}
	
	public void setCommandLayoutPolicy(CommandLayoutPolicy __clp)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Shows the given alert on this display, when the alert is finished the
	 * specified displayable is shown when it exits.
	 *
	 * This follows the same semantics as {@link #setCurrent(Displayable)}.
	 *
	 * @param __show The alert to show.
	 * @param __exit The displayable to show when the alert that is
	 * set is dismissed. This cannot be an {@link Alert}.
	 * @throws DisplayCapabilityException If the display cannot show the given
	 * displayable.
	 * @throws IllegalStateException If the display hardware is missing; If
	 * the displayables are associated with another display or tab pane; or
	 * the next displayable item is an alter.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	public void setCurrent(Alert __show, Displayable __exit)
		throws DisplayCapabilityException, IllegalStateException,
			NullPointerException
	{
		// {@squirreljme.error EB19 Cannot show another alert when the alert
		// to show is cleared.}
		if (__exit instanceof Alert)
			throw new IllegalStateException("EB19");
		
		// Check
		if (__show == null || __exit == null)
			throw new NullPointerException("NARG");
		
		// Forward
		this.__setCurrent(__show, __exit);
	}
	
	/**
	 * Sets the current displayable to be displayed.
	 *
	 * If the value to be passed is an {@link Alert} then this acts as if
	 * {@code setCurrent(__show, getCurrent())} was called.
	 *
	 * The displayable if specified will be put into the foreground state.
	 *
	 * Note that it is unspecified when the displayable is made current, it may
	 * be done when this is called or it may be queued for later.
	 *
	 * @param __show The displayable to show, if {@code null} this tells the
	 * {@link Display} to enter the background state.
	 * @throws DisplayCapabilityException If the display cannot show the given
	 * displayable.
	 * @throws IllegalStateException If the display hardware is missing; If
	 * the displayable is associated with another display or tab pane.
	 * @since 2016/10/08
	 */
	public void setCurrent(Displayable __show)
		throws DisplayCapabilityException, IllegalStateException
	{
		// Enter background state?
		if (__show == null)
		{
			throw new todo.TODO();
			/*head.setState(DisplayState.BACKGROUND);
			return;*/
		}
		
		// Forward
		this.__setCurrent(__show,
			(__show instanceof Alert) ? this.getCurrent() : null);
	}
	
	public void setCurrentItem(Item __a)
	{
		throw new todo.TODO();
	}
	
	public void setIdleItem(IdleItem __i)
	{
		throw new todo.TODO();
	}
	
	public void setPreferredOrientation(int __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Attempts to vibrate the device for the given number of milliseconds.
	 *
	 * The values here only set the duration to vibrate for from the current
	 * point in time and will not increase the length of vibration.
	 *
	 * The return value will be {@code false} if the display is in the
	 * background, the device cannot vibrate, or the vibrator cannot be
	 * controlled.
	 *
	 * Note that excessive vibration may cause the battery life for a device to
	 * be lowered, thus it should be used sparingly.
	 *
	 * @param __d The number of milliseconds to vibrate for, if zero the
	 * vibrator will be switched off.
	 * @return {@code true} if the vibrator is controllable by this application
	 * and the display is active.
	 * @throws IllegalArgumentException If the duration is negative.
	 * @since 2017/02/26
	 */
	public boolean vibrate(int __d)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB1a Cannot vibrate for a negative duration.}
		if (__d < 0)
			throw new IllegalArgumentException("EB1a");
		
		throw new todo.TODO();
		/*
		// Do not vibrate in the background
		DisplayHead head = this._head;
		if (head.displayState() == DisplayState.BACKGROUND)
			return false;
		
		// Vibrate the display head
		return head.vibrate(__d);*/
	}
	
	/**
	 * This wraps getting the best image size.
	 *
	 * @param __e The element to get it for.
	 * @return The best image size.
	 * @throws IllegalArgumentException If the element type is not valid.
	 * @since 2016/10/14
	 */
	private int __bestImageSize(int __e)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
		/*
		// Depends
		DisplayProperty p;
		switch (__e)
		{
			case LIST_ELEMENT:
				p = DisplayProperty.BEST_IMAGE_SIZE_LIST_ELEMENT;
				break;
				
			case CHOICE_GROUP_ELEMENT:
				p = DisplayProperty.BEST_IMAGE_SIZE_CHOICE_GROUP_ELEMENT;
				break;
				
			case ALERT:
				p = DisplayProperty.BEST_IMAGE_SIZE_ALERT;
				break;
				
			case TAB:
				p = DisplayProperty.BEST_IMAGE_SIZE_TAB;
				break;
				
			case COMMAND:
				p = DisplayProperty.BEST_IMAGE_SIZE_COMMAND;
				break;
				
			case NOTIFICATION:
				p = DisplayProperty.BEST_IMAGE_SIZE_NOTIFICATION;
				break;
				
			case MENU:
				p = DisplayProperty.BEST_IMAGE_SIZE_MENU;
				break;
				
				// {@squirreljme.error EB1b Cannot get the best image size of
				// the specified element. (The element specifier)}
			default:
				throw new IllegalArgumentException(String.format("EB1b %d",
					__e));
		}
		
		// Get
		return this._properties[p.ordinal()];
		*/
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
	 * @throws NullPointerException If {@code __show} is null.
	 * @since 2016/10/08
	 */
	private void __setCurrent(Displayable __show, Displayable __exit)
		throws DisplayCapabilityException, IllegalStateException,
			NullPointerException
	{
		// The displayable to show should never be null
		if (__show == null)
			throw new NullPointerException("NARG");
		
		try
		{
			// Set current
			LcdServiceCall.<VoidType>call(VoidType.class,
				LcdFunction.DISPLAY_SET_CURRENT,
				this._index,
				(__show != null ? __show._handle : 0),
				(__exit != null ? __exit._handle : 0));
		}
		
		// {@squirreljme.error EB21 The displayable is already taken
		// by a display or tabbed pane.)
		catch (LcdDisplayableTakenException e)
		{
			throw new IllegalStateException("EB21", e);
		}
	}
	
	/**
	 * Some kind of layout or otherwise changed on the display, so force it
	 * to be updated and refreshed to reflect the latest Displayable changes.
	 *
	 * @since 2017/08/19
	 */
	void __update()
	{
		throw new todo.TODO();
	}
	
	public static void addDisplayListener(DisplayListener __dl)
	{
		throw new todo.TODO();
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
		
		// {@squirreljme.error EB1e Could not get the display for the specified
		// MIDlet because no displays are available.}
		throw new IllegalStateException("EB1e");
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
		ServiceCaller caller = LcdServiceCall.caller();
		
		// This call will always refresh the displays which are currently
		// available to the server
		Map<Integer, Reference<Display>> displays = Display._DISPLAYS;
		synchronized (displays)
		{
			IntegerArray dids = caller.<IntegerArray>serviceCall(
				IntegerArray.class, LcdFunction.QUERY_DISPLAYS);
			
			// Just check to see if the map knows about an index value
			for (int i = 0, n = dids.length(); i < n; i++)
			{
				Integer did = dids.get(i);
				
				// If the map does not contain the ID then it is likely a new
				// display which has been attached
				// The value is handled later if a display needs to be
				// initialized accordingly
				if (!displays.containsKey(did))
					displays.put(did, null);
			}
		}
		
		// {@squirreljme.error EB1f No displays are available.}
		if (displays.size() <= 0)
			throw new IllegalStateException("EB1f");
		
		// Add any displays that meet the capabilities
		List<Display> rv = new ArrayList<>();
		for (int did : displays.keySet())
		{
			Display d = Display.__mapDisplay(did);
			
			if (__caps == 0 || (d.getCapabilities() & __caps) == __caps)
				rv.add(d);
		}
		
		// As an array
		return rv.<Display>toArray(new Display[rv.size()]);
	}
	
	public static void removeDisplayListener(DisplayListener __dl)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Maps the specified display index to a display and creates an object
	 * which represents and provides access to the display.
	 *
	 * @param __did The display index.
	 * @return The display for the given index.
	 * @since 2018/03/16
	 */
	static Display __mapDisplay(int __did)
	{
		// Displays must be premapped before they can be discovered
		Map<Integer, Reference<Display>> displays = Display._DISPLAYS;
		synchronized (displays)
		{
			Integer k = __did;
			
			// {@squirreljme.error EB1w Could not map the given display
			// because it is not a known display key. (The display index)}
			if (!displays.containsKey(k))
				throw new IllegalStateException(String.format("EB1w %d", k));
			
			Reference<Display> ref = displays.get(k);
			Display rv;
			
			if (ref == null || null == (rv = ref.get()))
				displays.put(k, new WeakReference<>(
					(rv = new Display(__did))));
			
			return rv;
		}
	}
	
	/**
	 * Returns the displayable associated with the given ID.
	 *
	 * @param __id The ID of the display.
	 * @return The displayable for the given ID or null otherwise.
	 * @since 2017/10/27
	 */
	@Deprecated
	static Displayable __displayableByHeadId(int __id)
	{
		throw new todo.TODO();
		/*
		Display display = __displayByHeadId(__id);
		if (display != null)
			return display._current;
		return null;*/
	}
	
	/**
	 * Returns the display associated with the given ID.
	 *
	 * @param __id The ID of the display.
	 * @return The display for the given ID or null otherwise.
	 * @since 2017/10/27
	 */
	@Deprecated
	static Display __displayByHeadId(int __id)
	{
		throw new todo.TODO();
		/*
		Display[] displays = _DISPLAYS;
		
		// See if the head directory associates with a head
		int n = displays.length;
		if (__id >= 0 && __id < n)
		{
			Display rv = displays[__id];
			if (rv._head.headId() == __id)
				return rv;
		}
		
		// Search through all heads otherwise
		for (Display d : displays)
			if (d._head.headId() == __id)
				return d;
		
		// Not found
		return null;*/
	}
}

