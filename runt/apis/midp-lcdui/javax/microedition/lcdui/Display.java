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

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;
import cc.squirreljme.runtime.lcdui.common.CommonColors;
import cc.squirreljme.runtime.lcdui.common.CommonMetrics;
import cc.squirreljme.runtime.lcdui.DisplayOrientation;
import cc.squirreljme.runtime.lcdui.DisplayState;
import cc.squirreljme.runtime.lcdui.event.NonStandardKey;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.ui.UIDisplayState;
import cc.squirreljme.runtime.lcdui.ui.UIFramebuffer;
import cc.squirreljme.runtime.lcdui.ui.UIPersist;
import cc.squirreljme.runtime.lcdui.ui.UIStack;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.microedition.midlet.MIDlet;

public class Display
	extends __Widget__
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
	
	/** The number of down keys to store, for a single hand. */
	private static final int _NUM_DOWNKEYS =
		5;
	
	/** The displays which currently exist based on their index. */
	private static final Map<Integer, Display> _DISPLAYS =
		new HashMap<>();
	
	/** Listeners for the display. */
	private static final List<DisplayListener> _LISTENERS =
		new ArrayList<>();
	
	/** Quick reference to the 0th display without needing a lock. */
	private static volatile Display _DISPLAY_ZERO;
	
	/** The display state for this Display. */
	final UIDisplayState _state;
	
	/** Persistent UI state. */
	final UIPersist _uipersist =
		new UIPersist();
	
	/** The Native ID of this display. */
	@Deprecated
	final int _nid;
	
	/** Set of keys which are down already, used to detect repeats on press. */
	private final int[] _downkeys =
		new int[_NUM_DOWNKEYS];
	
	/** The displayable to show. */
	private volatile Displayable _current;
	
	/** The displayable to show on exit. */
	private volatile Displayable _exit;
	
	/** Is this display being shown? */
	private volatile boolean _isshown;
	
	/** The draw stack of this display. */
	volatile UIStack _uistack;
	
	/**
	 * Initializes the probe.
	 *
	 * @since 2018/12/02
	 */
	static
	{
		// Setup the probe that accesses LCDUI internals
		new __LCDUIProbe__();
		
		// Register the callback so events can be executed in the LCDUI code
		NativeDisplayAccess.registerEventCallback(__EventCallback__._CALLBACK);
	}
	
	/**
	 * Initializes the display instance.
	 *
	 * @param __id The native ID of the display.
	 * @since 2018/03/16
	 */
	Display(int __id)
	{
		this._state = new UIDisplayState(__id);
		this._nid = __id;
	}
	
	public void callSerially(Runnable __a)
	{
		// Note that the Runnable.run() will be called as if it were serialized
		// like everything else with @SerializedEvent
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
		// For simplicity only the first display supports input events of
		// any kind
		int mask = 0xFFFF_FFFF;
		if (this._nid != 0)
			mask &= ~(SUPPORTS_INPUT_EVENTS);
		
		// Use the capabilities of the native display, but since SquirrelJME
		// manages pretty much everything in a framebuffer every display will
		// always have certain capabilities
		return (NativeDisplayAccess.capabilities(this._nid) |
			SUPPORTS_COMMANDS | SUPPORTS_FORMS | SUPPORTS_TICKER |
			SUPPORTS_ALERTS | SUPPORTS_LISTS | SUPPORTS_TEXTBOXES |
			SUPPORTS_FILESELECTORS | SUPPORTS_TABBEDPANES |
			SUPPORTS_MENUS) & mask;
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
	 * @return The ARGB color for the specified user interface item, it will
	 * be in the form of {@code 0x00RRGGBB}.
	 * @throws IllegalArgumentException If the specified color is not valid.
	 * @since 2016/10/14
	 */
	public int getColor(int __c)
		throws IllegalArgumentException
	{
		int rv;
		switch (__c)
		{
			case COLOR_BORDER:
				rv = CommonColors.BORDER;
				break;
			
			case COLOR_BACKGROUND:
			case COLOR_IDLE_BACKGROUND:
				rv = CommonColors.BACKGROUND;
				break;
			
			case COLOR_FOREGROUND:
			case COLOR_IDLE_FOREGROUND:
				rv = CommonColors.FOREGROUND;
				break;
			
			case COLOR_HIGHLIGHTED_BORDER:
				rv = CommonColors.HIGHLIGHTED_BORDER;
				break;
				
			case COLOR_HIGHLIGHTED_BACKGROUND:
			case COLOR_IDLE_HIGHLIGHTED_BACKGROUND:
				rv = CommonColors.HIGHLIGHTED_BACKGROUND;
				break;
			
			case COLOR_HIGHLIGHTED_FOREGROUND:
			case COLOR_IDLE_HIGHLIGHTED_FOREGROUND:
				rv = CommonColors.HIGHLIGHTED_FOREGROUND;
				break;
		
				// {@squirreljme.error EB2p Unknown color specifier. (The
				// color specifier)}
			default:
				throw new IllegalArgumentException("EB2p " + __c);
		}
		
		// Clip the alpha away
		return (rv & 0xFFFFFF);
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
		return this._current;
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
		return this._state.framebuffer().bufferheight;
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
		// Landscape just means a longer width
		boolean landscape = this.getWidth() > this.getHeight();
		
		// If it is detected that the display is upsidedown, just say that
		// it was rotated 180 degrees
		if (NativeDisplayAccess.isUpsideDown(this._nid))
			if (landscape)
				return ORIENTATION_LANDSCAPE_180;
			else
				return ORIENTATION_PORTRAIT_180;
		else
			if (landscape)
				return ORIENTATION_LANDSCAPE;
			else
				return ORIENTATION_PORTRAIT;
	}
	
	/**
	 * Returns the maximum width of the display.
	 *
	 * @retrn The maximum display width.
	 * @since 2016/10/14
	 */
	public int getWidth()
	{
		return this._state.framebuffer().bufferheight;
	}
	
	/**
	 * Are mouse/stylus press and release events supported?
	 *
	 * @return {@code true} if they are supported.
	 * @since 2016/10/14
	 */
	public boolean hasPointerEvents()
	{
		// Only the first display supports this
		if (this._nid != 0)
			return false;
		
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
		// Only the first display supports this
		if (this._nid != 0)
			return false;
		
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
	
		// {@squirreljme.error EB1c Unknown activity mode specified.}
		else
			throw new IllegalArgumentException("EB1c");
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
		// {@squirreljme.error EB1d Cannot show another alert when the alert
		// to show is cleared.}
		if (__exit instanceof Alert)
			throw new IllegalStateException("EB1d");
		
		// Check
		if (__show == null || __exit == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB1e The displayable to show on exit after
		// showing an alert cannot be an alert.}
		if (__exit instanceof Alert)
			throw new IllegalStateException("EB1e");
		
		// Perform call on this display
		throw new todo.TODO();
		/*
		try
		{
			// Set widgets
			if (true)
				throw new todo.TODO();
			/*
			LcdServiceCall.<VoidType>call(VoidType.class,
				LcdFunction.WIDGET_ALERT_SHOW, this._handle,
				__show._handle, __exit._handle);
			* /
			
			// Hold onto these so they do not get GCed
			this._heldcurrent = __show;
			this._heldexit = __exit;
		}
		
		// {@squirreljme.error EB1f Could not set the alert and its exit
		// displayable because it is already set on a display.}
		catch (LcdWidgetOwnedException e)
		{
			throw new IllegalStateException("EB1f", e);
		}*/
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
		
		// If we are trying to show the same display, do nothing
		Displayable current = this._current;
		if (current == __show)
			return;
		
		// {@squirreljme.error EB29 The display does not support this type
		// of displayable.}
		if ((this.getCapabilities() & __show.__supportBit()) == 0)
			throw new DisplayCapabilityException("EB29");
		
		// If showing an alert, it gets displayed instead
		if (__show instanceof Alert)
		{
			this.setCurrent((Alert)__show, this.getCurrent());
			return;
		}
		
		// {@squirreljme.error EB28 The displayable to be displayed is already
		// being displayed.}
		if (__show._parent != null)
			throw new IllegalStateException("EB28");
		
		// Clear current's parent
		if (current != null)
		{
			current._parent = null;
			
			// Clear the shown state for this widget
			current._isshown = false;
		}
		// Set new parent
		__show._parent = this;
		this._current = __show;
		
		// For the displayable, inherit the shown state from this display
		// since if the display is not being shown then the widget will not
		// be shown until it happens to be shown.
		__show._isshown = this._isshown;
		
		// Use the title of this thing
		NativeDisplayAccess.setDisplayTitle(this._state.nativeid,
			__show.getTitle());
		
		// Update the UI stack
		this.__updateUIStack(this._uipersist, null);
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
		throw new todo.TODO();
		/*
		// {@squirreljme.error EB1h Cannot vibrate for a negative duration.}
		if (__d < 0)
			throw new IllegalArgumentException("EB1h");
		
		// Send vibrate call
		LcdServiceCall.<VoidType>call(VoidType.class,
			LcdFunction.DISPLAY_VIBRATE, this._handle, __d);
		
		// Always return true because it is faster to just return as quickly
		// as possible than it is to vibrate the display and to see if it is
		// supported or not
		return true;
		*/
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
				
				// {@squirreljme.error EB1i Cannot get the best image size of
				// the specified element. (The element specifier)}
			default:
				throw new IllegalArgumentException(String.format("EB1i %d",
					__e));
		}
		
		// Get
		return this._properties[p.ordinal()];
		*/
	}
	
	/**
	 * Checks that a repaint needs to be done.
	 *
	 * @since 2018/12/09
	 */
	final void __checkRepaint()
	{
		UIPersist uipersist = this._uipersist;
		if (uipersist.repaint)
		{
			// Do not repaint
			uipersist.repaint = false;
			
			// Request it
			NativeDisplayAccess.displayRepaint(
				this._state.nativeid, 0, 0,
				this.getWidth(), this.getHeight());
		}
	}
	
	/**
	 * Performs a command event on the target displayable.
	 *
	 * @param __i The command index.
	 * @since 2018/12/02
	 */
	@SerializedEvent
	final void __doCommandAction(int __i)
	{
		// No current, do nothing
		Displayable current = this._current;
		if (current == null)
			return;
		
		// If there is no command listener then nothing can ever happen
		CommandListener cmdlistener = current._cmdlistener;
		if (cmdlistener == null)
			return;
		
		// Check if the command index is in bounds
		Object[] rawcommands = current._commands.values();
		if (__i < 0 || __i >=  rawcommands.length)
			return;
		
		// Otherwise execute the command
		cmdlistener.commandAction((Command)rawcommands[__i], current);
	}
	
	/**
	 * {@inheritDoc}
	 * @param __shown Is the display being shown?
	 * @since 2018/03/24
	 */
	@SerializedEvent
	final void __doDisplayShown(boolean __shown)
	{
		// If this is being shown, load the framebuffer
		if (__shown)
			this._state.framebuffer();
		
		// Internally set the display as shown or not
		this._isshown = __shown;
		
		// Report that visibility has changed
		int state = (__shown ? Display.STATE_VISIBLE :
			Display.STATE_BACKGROUND);
		for (DisplayListener dl : Display.__listeners())
			dl.displayStateChanged(this, state);
		
		// Show the widget
		Displayable current = this._current;
		if (current != null)
			current.__doShown(__shown);
	}
	
	/**
	 * This is called when the display has changed size.
	 *
	 * @param __w The display width.
	 * @param __h The display height.
	 * @since 2018/03/23
	 */
	@SerializedEvent
	final void __doDisplaySizeChanged(int __w, int __h)
	{
		// Update the framebuffer because everything relies on it
		UIDisplayState state = this._state;
		state.framebuffer();
		
		// Report that the size changed for events
		for (DisplayListener dl : Display.__listeners())
			dl.sizeChanged(this, __w, __h);
		
		// Tell the current displayable that the size has changed
		Displayable d = this.getCurrent();
		if (d != null)
			d.sizeChanged(__w, __h);
		
		// Update the UI stack
		this.__updateUIStack(this._uipersist, null);
	}
	
	/**
	 * Requests that exit is performed. If there is a command which tagged
	 * under the exit type, then that will be launched. Otherwise if there is
	 * no command then it will just do a system exit.
	 *
	 * @since 2018/11/18
	 */
	@SerializedEvent
	final void __doExitRequest()
	{
		// If an exit command is found, run it
		Displayable d = this.getCurrent();
		if (d != null)
		{
			// Only if there is an action to be ran, otherwise ignore
			CommandListener cl = d._cmdlistener;
			if (cl != null)
				for (Command c : d._commands)
					if (c._type == Command.EXIT)
					{
						cl.commandAction(c, d);
						return;
					}
		}
		
		// Otherwise just exit the VM
		System.exit(0);
	}
	
	/**
	 * Performs a key action.
	 *
	 * @param __kt The event type.
	 * @param __kc Key code.
	 * @param __ch Character code.
	 * @param __time Time code.
	 * @throws IllegalArgumentException If the key type is not valid.
	 * @since 2018/12/02
	 */
	@SerializedEvent
	final void __doKeyAction(int __kt, int __kc, char __ch, int __time)
		throws IllegalArgumentException
	{
		// If a function key is pressed, just treat it as a command which
		// has been executed
		if (__kc >= NonStandardKey.F1 && __kc <= NonStandardKey.F24)
		{
			if (__kt == NativeDisplayEventCallback.KEY_PRESSED)
				this.__doCommandAction(__kc - NonStandardKey.F1);
			return;
		}
		
		// This is used to detect repeats from press events
		int[] downkeys = this._downkeys;
		
		// Forward to the displayable
		Displayable current = this._current;
		if (current != null)
		{
			// If the key code is not invalid, then detect if duplicate
			// press events should be turned into repeats
			if (__kc != 0)
			{
				// Turn duplicate key press events into repeat events
				if (__kt == NativeDisplayEventCallback.KEY_PRESSED)
				{
					// Check for key repeat
					boolean isrepeat = false;
					int lastslot = -1;
					for (int i = 0; i < _NUM_DOWNKEYS; i++)
						if (downkeys[i] == 0)
							lastslot = i;
						else if (downkeys[i] == __kc)
						{
							isrepeat = true;
							break;
						}
					
					// Change to repeat event
					if (isrepeat)
						__kt = NativeDisplayEventCallback.KEY_REPEATED;
					
					// Store key which is down for later
					else if (lastslot >= 0)
						downkeys[lastslot] = __kc;
				}
				
				// Remove released keys
				else if (__kt == NativeDisplayEventCallback.KEY_RELEASED)
				{
					// Remove from the down key set
					for (int i = 0; i < _NUM_DOWNKEYS; i++)
						if (__kc == downkeys[i])
						{
							downkeys[i] = 0;
							break;
						}
				}
			}
			
			// Forward
			current.__doKeyAction(__kt, __kc, __ch, __time);
		}
		
		// Check repaint
		this.__checkRepaint();
	}
	
	/**
	 * Performs a mouse pointer action.
	 *
	 * @param __t The event type.
	 * @param __x X coordinate.
	 * @param __y Y coordinate.
	 * @param __time Timecode.
	 * @throws IllegalArgumentException If the event is not valid.
	 * @since 2018/12/02
	 */
	@SerializedEvent
	final void __doPointerAction(int __t, int __x, int __y, int __time)
		throws IllegalArgumentException
	{
		// Forward to the displayable
		Displayable current = this._current;
		if (current != null)
			current.__doPointerAction(__t, __x, __y, __time);
		
		// Check repaint
		this.__checkRepaint();
	}
	
	/**
	 * Performs a repaint of the frame.
	 *
	 * @param __x X clip.
	 * @param __y Y clip.
	 * @param __w Width clip.
	 * @param __h Height clip.
	 * @since 2018/11/18
	 */
	@SerializedEvent
	final void __doRepaint(int __x, int __y, int __w, int __h)
	{
		// Reclaulcate the draw stack?
		UIPersist uipersist = this._uipersist;
		if (uipersist.recalc)
		{
			// Do not recalculate again
			uipersist.recalc = false;
			
			// Update the UI stack
			this.__updateUIStack(uipersist, null);
		}
		
		// A repaint is happening now so always clear it
		uipersist.repaint = false;
		
		// Get graphics for this state
		Graphics g = this._state.graphics();
		
		// Set the initial clipping region
		g.clipRect(__x, __y, __w, __h);
		
		// Call internal paint, which draws our stack
		try
		{
			// Render the draw stack
			UIStack stack = this._uistack;
			if (stack != null)
				stack.render(null, g);
		}
		
		// Catch all of these, but keep drawing!
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __draw(UIStack __parent, UIStack __self, Graphics __g)
	{
		// Nothing needed at all
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __updateUIStack(UIPersist __keep, UIStack __parent)
	{
		UIDisplayState state = this._state;
		UIFramebuffer fb = state.framebuffer();
		int w = fb.bufferwidth,
			h = fb.bufferheight;
		
		// The current thing to be drawn
		Displayable current = this._current;
		
		// This will be initialized depending on if there are commands or not
		UIStack stack;
		
		// If there are no commands or if we are showing a full-screen canvas
		// then there will be no command buttons
		Object[] commands = (current == null ? null :
			current._commands.values());
		int numcommands = (current == null ? 0 : commands.length);
		if (numcommands == 0 || ((current instanceof Canvas) &&
			((Canvas)current)._isfullscreen))
			stack = new UIStack(null, w, h);
		else
		{
			int hw = w / 2,
				ph = h - CommonMetrics.COMMANDBAR_HEIGHT;
			stack = new UIStack(null, w, ph);
			
			// Add the first two commands to be drawn
			for (int i = 0, n = (numcommands == 1 ? 1 : 2); i < n; i++)
			{
				// Calculation position
				UIStack s;
				stack.addExact((s = new UIStack((Command)commands[i],
					hw, CommonMetrics.COMMANDBAR_HEIGHT)),
					0 + (i * hw), ph,
					hw, CommonMetrics.COMMANDBAR_HEIGHT);
				
				// Do not clip to the parent
				s.noclip = true;
			}
		}
		
		// Update the stack of the widget accordingly
		if (current != null)
			current.__updateUIStack(__keep, stack);
		
		// Store the stack for drawing
		this._uistack = stack;
		
		// Tell everything to repaint since everything was calculated
		NativeDisplayAccess.displayRepaint(state.nativeid, 0, 0, w, h);
	}
	
	/**
	 * Adds the specified listener for changes to displays.
	 *
	 * The order in which listeners are executed in is
	 * implementation specified.
	 *
	 * @param __dl The listener to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public static void addDisplayListener(DisplayListener __dl)
		throws NullPointerException
	{
		if (__dl == null)
			throw new NullPointerException("NARG");
		
		List<DisplayListener> listeners = Display._LISTENERS;
		synchronized (listeners)
		{
			// Do nothing if it is already in there
			for (int i = 0, n = listeners.size(); i < n; i++)
				if (listeners.get(i) == __dl)
					return;
			
			// Add it, if it is not there
			listeners.add(__dl);
		}
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
		
		// {@squirreljme.error EB1j Could not get the display for the specified
		// MIDlet because no displays are available.}
		throw new IllegalStateException("EB1j");
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
		// Initially filled with all displays, this will be filtered
		// accordingly
		List<Display> rv = new ArrayList<>();
		
		// Map all display IDs to actual displays
		Map<Integer, Display> displays = Display._DISPLAYS;
		synchronized (displays)
		{
			// Map all displays
			int numdisplays = NativeDisplayAccess.numDisplays();
			for (int i = 0; i < numdisplays; i++)
				rv.add(Display.__mapDisplay(i));
		}
		
		// We only need to filter out displays if we specified capabilities
		// we need
		if (__caps != 0)
		{
			// Filter out all the displays so that only the displays which mat
			for (Iterator<Display> it = rv.iterator(); it.hasNext();)
			{
				Display d = it.next();
				
				// Remove any displays which specifically are lacking the ones
				// we want
				if ((d.getCapabilities() & __caps) != __caps)
					it.remove();
			}
			
			// {@squirreljme.error EB1k No displays are available.}
			if (rv.size() <= 0)
				throw new IllegalStateException("EB1k");
		}
		
		return rv.<Display>toArray(new Display[rv.size()]);
	}
	
	/**
	 * Removes the specified display listener so that it is no longer called
	 * when events occur.
	 *
	 * @param __dl The listener to remove.
	 * @throws IllegalStateException If the listener is not in the display.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public static void removeDisplayListener(DisplayListener __dl)
		throws IllegalStateException, NullPointerException
	{
		if (__dl == null)
			throw new NullPointerException("NARG");	
		
		List<DisplayListener> listeners = Display._LISTENERS;
		synchronized (listeners)
		{
			boolean didremove = false;
			for (int i = 0, n = listeners.size(); i < n; i++)
				if (listeners.get(i) == __dl)
				{
					listeners.remove(i);
					didremove = true;
				}
			
			// {@squirreljme.error EB1l The listener was never added to the
			// listener set.}
			if (!didremove)
				throw new IllegalStateException("EB1l");
		}
	}
	
	/**
	 * Returns an array of all the attached listeners.
	 *
	 * @return An array of listeners.
	 * @since 2018/03/24
	 */
	static DisplayListener[] __listeners()
	{
		List<DisplayListener> listeners = Display._LISTENERS;
		synchronized (listeners)
		{
			return listeners.<DisplayListener>toArray(new DisplayListener[
				listeners.size()]);
		}
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
		// If we are requesting the 0th display, since it is requested so often
		// allow getting it without needing to lock and load from the map
		// This in general should improve performance nicely
		if (__did == 0)
		{
			Display rv = _DISPLAY_ZERO;
			if (rv != null)
				return rv;
		}
		
		// Lock since multiple threads could be messing with the displays
		Map<Integer, Display> displays = Display._DISPLAYS;
		synchronized (displays)
		{
			Integer k = Integer.valueOf(__did);
			Display rv = displays.get(k);
			
			// Create mapping for this display?
			if (rv == null)
			{
				displays.put(k, (rv = new Display(__did)));
				
				// Cache display zero?
				if (__did == 0)
					_DISPLAY_ZERO = rv;
			}
			
			return rv;
		}
	}
}

