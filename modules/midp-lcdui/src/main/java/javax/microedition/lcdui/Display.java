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

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.DeviceFeedbackType;
import cc.squirreljme.jvm.Framebuffer;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.Poking;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.ExtendedCapabilities;
import cc.squirreljme.runtime.lcdui.common.CommonColors;
import cc.squirreljme.runtime.lcdui.fbui.UIState;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import cc.squirreljme.runtime.lcdui.phoneui.StandardMetrics;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.midlet.MIDlet;

@SuppressWarnings("OverlyComplexClass")
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

	@SuppressWarnings("FieldNamingConvention")
	public static final int COLOR_IDLE_HIGHLIGHTED_BACKGROUND =
		8;

	@SuppressWarnings("FieldNamingConvention")
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

	@SuppressWarnings("FieldNamingConvention")
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
	
	/** The native display instance. */ 
	final cc.squirreljme.jvm.mle.brackets.UIDisplayBracket _uiDisplay;
	
	/** The displayable to show. */
	private volatile Displayable _current;
	
	/** The displayable to show on exit. */
	private volatile Displayable _exit;
	
	/**
	 * Initializes the display instance.
	 *
	 * @param __uiDisplay The native display.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/16
	 */
	Display(cc.squirreljme.jvm.mle.brackets.UIDisplayBracket __uiDisplay)
		throws NullPointerException
	{
		if (__uiDisplay == null)
			throw new NullPointerException("NARG");
		
		this._uiDisplay = __uiDisplay;
	}
	
	public void callSerially(Runnable __a)
	{
		// Note that the Runnable.run() will be called as if it were serialized
		// like everything else with {@link SerializedEvent}
		throw new todo.TODO();
	}
	
	/**
	 * Flashes the display LED for the given number of milliseconds.
	 *
	 * In SquirrelJME this flashes an LED and not the back light, since it is
	 * not a popular means to notify the user and additionally due to
	 * medical concerns such as epilepsy.
	 *
	 * @param __ms The number of milliseconds to flash for.
	 * @return {@code true} if the backlight is controlled by the application
	 * and the display is in the foreground, otherwise {@code false}.
	 * @since 2019/10/05
	 */
	public boolean flashBacklight(int __ms)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB30 Cannot blink for a negative duration.}
		if (__ms < 0)
			throw new IllegalArgumentException("EB30");
		
		// Blink!
		Assembly.sysCall(SystemCallIndex.DEVICE_FEEDBACK,
			DeviceFeedbackType.BLINK_LED, __ms);
		
		// Only return true if no error was generated
		return (SystemCallError.NO_ERROR ==
			Assembly.sysCallV(SystemCallIndex.ERROR_GET,
				SystemCallIndex.DEVICE_FEEDBACK));
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
		return this.__bestImageSize(__a, true);
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
		return this.__bestImageSize(__a, false);
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
		int caps = UIState.getInstance().capabilities();
		boolean hastouch = ((caps & Framebuffer.CAPABILITY_TOUCH) != 0);
		boolean hasinput = hastouch |
			((caps & Framebuffer.CAPABILITY_KEYBOARD) != 0);
		
		// Use the capabilities of the native display, but since SquirrelJME
		// manages pretty much everything in a framebuffer every display will
		// always have certain capabilities
		return (hasinput ? Display.SUPPORTS_INPUT_EVENTS : 0) |
			(hastouch ? ExtendedCapabilities.SUPPORTS_POINTER_EVENTS : 0) | Display.SUPPORTS_COMMANDS | Display.SUPPORTS_FORMS | Display.SUPPORTS_TICKER | Display.SUPPORTS_ALERTS | Display.SUPPORTS_LISTS | Display.SUPPORTS_TEXTBOXES | Display.SUPPORTS_FILESELECTORS | Display.SUPPORTS_TABBEDPANES | Display.SUPPORTS_MENUS;
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
			case Display.COLOR_BORDER:
				rv = CommonColors.BORDER;
				break;
			
			case Display.COLOR_BACKGROUND:
			case Display.COLOR_IDLE_BACKGROUND:
				rv = CommonColors.BACKGROUND;
				break;
			
			case Display.COLOR_FOREGROUND:
			case Display.COLOR_IDLE_FOREGROUND:
				rv = CommonColors.FOREGROUND;
				break;
			
			case Display.COLOR_HIGHLIGHTED_BORDER:
				rv = CommonColors.HIGHLIGHTED_BORDER;
				break;
				
			case Display.COLOR_HIGHLIGHTED_BACKGROUND:
			case Display.COLOR_IDLE_HIGHLIGHTED_BACKGROUND:
				rv = CommonColors.HIGHLIGHTED_BACKGROUND;
				break;
			
			case Display.COLOR_HIGHLIGHTED_FOREGROUND:
			case Display.COLOR_IDLE_HIGHLIGHTED_FOREGROUND:
				rv = CommonColors.HIGHLIGHTED_FOREGROUND;
				break;
		
				// {@squirreljme.error EB1h Unknown color specifier. (The
				// color specifier)}
			default:
				throw new IllegalArgumentException("EB1h " + __c);
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
	
	/**
	 * Returns the current hardware state.
	 *
	 * @return The hardware state.
	 * @since 2018/12/10
	 */
	public int getHardwareState()
	{
		throw new todo.TODO();
		/*
		if (__EventCallback__._CALLBACK._registered)
			return DISPLAY_HARDWARE_ENABLED;
		return DISPLAY_HARDWARE_DISABLED;
		*/
	}
	
	/**
	 * Returns the maximum height of the display.
	 *
	 * @return The maximum display height.
	 * @since 2016/10/14
	 */
	public int getHeight()
	{
		return UIState.getInstance().displayHeight();
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
		if (UIState.getInstance().displayFlipped())
			if (landscape)
				return Display.ORIENTATION_LANDSCAPE_180;
			else
				return Display.ORIENTATION_PORTRAIT_180;
		else
			if (landscape)
				return Display.ORIENTATION_LANDSCAPE;
			else
				return Display.ORIENTATION_PORTRAIT;
	}
	
	/**
	 * Returns the maximum width of the display.
	 *
	 * @return The maximum display width.
	 * @since 2016/10/14
	 */
	public int getWidth()
	{
		return UIState.getInstance().displayWidth();
	}
	
	/**
	 * Are mouse/stylus press and release events supported?
	 *
	 * @return {@code true} if they are supported.
	 * @since 2016/10/14
	 */
	public boolean hasPointerEvents()
	{
		return 0 != (this.getCapabilities() &
			ExtendedCapabilities.SUPPORTS_POINTER_EVENTS);
	}
	
	/**
	 * Are mouse/stylus move/drag events supported?
	 *
	 * @return {@code true} if they are supported.
	 * @since 2016/10/14
	 */
	public boolean hasPointerMotionEvents()
	{
		return 0 != (this.getCapabilities() &
			ExtendedCapabilities.SUPPORTS_POINTER_EVENTS);
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
		return UIState.getInstance().displayIsColor();
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
		// Always return 2 because SquirrelJME operates on a flat framebuffer
		// where there is no such thing as transparency
		return 2;
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
		return UIState.getInstance().displayUniqueColors();
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
		if (__m == Display.MODE_ACTIVE)
			throw new todo.TODO();
	
		// Normal
		else if (__m == Display.MODE_NORMAL)
			throw new todo.TODO();
	
		// {@squirreljme.error EB1i Unknown activity mode specified.}
		else
			throw new IllegalArgumentException("EB1i");
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
		// {@squirreljme.error EB1j Cannot show another alert when the alert
		// to show is cleared.}
		if (__exit instanceof Alert)
			throw new IllegalStateException("EB1j");
		
		// Check
		if (__show == null || __exit == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB1k The displayable to show on exit after
		// showing an alert cannot be an alert.}
		if (__exit instanceof Alert)
			throw new IllegalStateException("EB1k");
		
		// Debug
		todo.DEBUG.note("Showing alert \"%s\"", __show._message);
		
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
		
		// {@squirreljme.error EB1l Could not set the alert and its exit
		// displayable because it is already set on a display.}
		catch (LcdWidgetOwnedException e)
		{
			throw new IllegalStateException("EB1l", e);
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
			todo.TODO.note("Enter background state.");
			/*head.setState(DisplayState.BACKGROUND);*/
			return;
		}
		
		// If we are trying to show the same display, do nothing
		Displayable current = this._current;
		if (current == __show)
		{
			// If we just set current with no actual change, just make sure
			// our callback is the one which is registered so that way we
			// take control of the screen
			this.__doShowCurrent(__show);
			
			return;
		}
		
		// If showing an alert, it gets displayed instead
		if (__show instanceof Alert)
		{
			this.setCurrent((Alert)__show, this.getCurrent());
			return;
		}
		
		// {@squirreljme.error EB1m The displayable to be displayed is already
		// being displayed.}
		if (__show._display != null)
			throw new IllegalStateException("EB1m");
		
		// Clear current's parent
		if (current != null)
		{
			// Stop showing
			current._isshown = false;
			current.hideNotify();
			
			// Not used anymore
			current._display = null;
		}
		
		// Set new parent
		__show._display = this;
		this._current = __show;
		
		// Do common showing stuff
		this.__doShowCurrent(__show);
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
		// {@squirreljme.error EB1n Cannot vibrate for a negative duration.}
		if (__d < 0)
			throw new IllegalArgumentException("EB1n");
		
		// Clear vibration
		Assembly.sysCall(SystemCallIndex.DEVICE_FEEDBACK,
			DeviceFeedbackType.VIBRATE, __d);
		
		// Only return true if no error was generated
		return (SystemCallError.NO_ERROR ==
			Assembly.sysCallV(SystemCallIndex.ERROR_GET,
				SystemCallIndex.DEVICE_FEEDBACK));
	}
	
	/**
	 * This wraps getting the best image size.
	 *
	 * @param __e The element to get it for.
	 * @param __h Return the height?
	 * @return The best image size.
	 * @throws IllegalArgumentException If the element type is not valid.
	 * @since 2016/10/14
	 */
	private int __bestImageSize(int __e, boolean __h)
		throws IllegalArgumentException
	{
		// Depends
		switch (__e)
		{
			case Display.LIST_ELEMENT:
				return StandardMetrics.LIST_ITEM_HEIGHT;
				
			case Display.CHOICE_GROUP_ELEMENT:
				throw new todo.TODO();
				
			case Display.ALERT:
				throw new todo.TODO();
				
			case Display.TAB:
				throw new todo.TODO();
				
			case Display.COMMAND:
				return StandardMetrics.COMMAND_BAR_HEIGHT;
				
			case Display.NOTIFICATION:
				throw new todo.TODO();
				
			case Display.MENU:
				return StandardMetrics.COMMAND_BAR_HEIGHT;
				
				// {@squirreljme.error EB1o Cannot get the best image size of
				// the specified element. (The element specifier)}
			default:
				throw new IllegalArgumentException(String.format("EB1o %d",
					__e));
		}
	}
	
	/**
	 * Do current show logic.
	 *
	 * @param __show The displayable to show.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/16
	 */
	final void __doShowCurrent(Displayable __show)
		throws NullPointerException
	{
		if (__show == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("Showing %s on display.", __show.getClass());
		
		// Get the backend to call on
		UIBackend backend = UIBackendFactory.getInstance();
		
		// Check and ensure that the background thread exists
		synchronized (StaticDisplayState.class)
		{
			// If there is no background thread yet, initialize it
			Thread bgThread = StaticDisplayState.backgroundThread();
			if (bgThread == null)
			{
				// The user interface thread to use
				__MLEUIThread__ uiRunner = new __MLEUIThread__();
				
				// Initialize thread and make it a background worker
				bgThread = new Thread(uiRunner, "SquirrelJME-LCDUI");
				ThreadShelf.javaThreadSetDaemon(bgThread);
				
				// Set background thread state and start it
				StaticDisplayState.setBackgroundThread(bgThread, uiRunner);
				bgThread.start();
			}
			
			// Set callback for the displayed form so it can receive events
			backend.callback(__show._uiForm,
				StaticDisplayState.callback());
		}
		
		// Show the form on the display
		backend.displayShow(this._uiDisplay,
			__show._uiForm);
		
		/*UIState uis = UIState.getInstance();
		
		// Always set as shown, easier to work with
		__show._isshown = true;
		
		// Set title of our display to the title of the Displayable
		uis.setTitle(__show._dtitle);
		
		// Set drawn displayable
		uis.setDisplayable(__show);
		
		// Callback when it is made visible
		__show.showNotify();*/
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
		StaticDisplayState.addListener(__dl);
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
		Display[] all = Display.getDisplays(0);
		if (all.length > 0)
			return all[0];
		
		// {@squirreljme.error EB1p Could not get the display for the specified
		// MIDlet because no displays are available.}
		throw new IllegalStateException("EB1p");
	}
	
	/**
	 * Obtains the displays which have the given capability from all internal
	 * display providers.
	 *
	 * @param __caps The capabilities to use, this is a bitfield and the values
	 * include all of the {@code SUPPORT_} prefixed constants. If {@code 0} is
	 * specified then capabilities are not checked.
	 * @return An array containing the displays with these capabilities.
	 * @throws IllegalStateException If there are no compatible displays.
	 * @since 2016/10/08
	 */
	public static Display[] getDisplays(int __caps)
		throws IllegalStateException
	{
		// Use cached displays, but otherwise load them
		Display[] all = StaticDisplayState.DISPLAYS;
		if (all == null)
		{
			// Poke the VM to initialize things potentially, this is just
			// needed by the native emulator bindings
			Poking.poke();
			
			// Get the displays that are attached to the system
			cc.squirreljme.jvm.mle.brackets.UIDisplayBracket[] uiDisplays =
				UIBackendFactory.getInstance().displays();
			int n = uiDisplays.length;
			
			// Initialize display instances
			all = new Display[n];
			for (int i = 0; i < n; i++)
				all[i] = new Display(uiDisplays[i]);
			
			// Use these for future calls
			StaticDisplayState.DISPLAYS = all;
			
			// Inform any listeners that the displays exist now
			for (DisplayListener listener : StaticDisplayState.listeners())
				for (Display display : all) 
					listener.displayAdded(display);
		}
		
		// If we do not care for the capabilities of the displays then just
		// return all of them
		if (__caps == 0)
			return all.clone();
		
		// Find possible displays
		List<Display> possible = new ArrayList<>(all.length);
		for (Display potential : all)
			if ((potential.getCapabilities() & __caps) == __caps)
				possible.add(potential);
		
		// {@squirreljme.error EB1q No displays are available.}
		if (possible.isEmpty())
			throw new IllegalStateException("EB1q");
		
		return possible.<Display>toArray(new Display[possible.size()]);
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
		StaticDisplayState.removeListener(__dl);
	}
}

