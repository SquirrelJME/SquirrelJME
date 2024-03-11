// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.constants.UIInputFlag;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchLAFInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import cc.squirreljme.runtime.lcdui.mle.Vibration;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayIdentityScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayManager;
import cc.squirreljme.runtime.lcdui.scritchui.ScritchLcdUiUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.microedition.midlet.MIDlet;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NonBlocking;

@SuppressWarnings("OverlyComplexClass")
@Api
public class Display
	implements DisplayWidget
{
	/** The soft-key for the left command. */
	static final int _SOFTKEY_LEFT_COMMAND =
		Display.SOFTKEY_BOTTOM + 1;
	
	/** The soft-key for the right command. */
	static final int _SOFTKEY_RIGHT_COMMAND =
		Display.SOFTKEY_BOTTOM + 2;
	
	@Api
	public static final int ALERT =
		3;

	@Api
	public static final int CHOICE_GROUP_ELEMENT =
		2;

	@Api
	public static final int COLOR_BACKGROUND =
		0;

	@Api
	public static final int COLOR_BORDER =
		4;

	@Api
	public static final int COLOR_FOREGROUND =
		1;

	@Api
	public static final int COLOR_HIGHLIGHTED_BACKGROUND =
		2;

	@Api
	public static final int COLOR_HIGHLIGHTED_BORDER =
		5;

	@Api
	public static final int COLOR_HIGHLIGHTED_FOREGROUND =
		3;

	@Api
	public static final int COLOR_IDLE_BACKGROUND =
		6;

	@Api
	public static final int COLOR_IDLE_FOREGROUND =
		7;

	@Api
	@SuppressWarnings("FieldNamingConvention")
	public static final int COLOR_IDLE_HIGHLIGHTED_BACKGROUND =
		8;

	@Api
	@SuppressWarnings("FieldNamingConvention")
	public static final int COLOR_IDLE_HIGHLIGHTED_FOREGROUND =
		9;

	@Api
	public static final int COMMAND =
		5;

	@Api
	public static final int DISPLAY_HARDWARE_ABSENT =
		2;

	@Api
	public static final int DISPLAY_HARDWARE_DISABLED =
		1;
	
	@Api
	public static final int DISPLAY_HARDWARE_ENABLED =
		0;

	@Api
	public static final int LIST_ELEMENT =
		1;

	@Api
	public static final int MENU =
		7;

	/** This is the activity mode that enables power saving inhibition. */
	@Api
	public static final int MODE_ACTIVE =
		1;
	
	/** This is the activity mode that is the default behavior. */
	@Api
	public static final int MODE_NORMAL =
		0;

	@Api
	public static final int NOTIFICATION =
		6;

	@Api
	public static final int ORIENTATION_LANDSCAPE =
		2;

	@Api
	public static final int ORIENTATION_LANDSCAPE_180 =
		8;

	@Api
	public static final int ORIENTATION_PORTRAIT =
		1;

	@Api
	public static final int ORIENTATION_PORTRAIT_180 =
		4;

	/** The mask and number of items that are permitted for soft-key items. */
	@Api
	public static final int SOFTKEY_INDEX_MASK =
		15;

	/** Displayed at the bottom of the screen. */
	@Api
	public static final int SOFTKEY_BOTTOM =
		800;

	/** Displayed on the left side of the screen. */
	@Api
	public static final int SOFTKEY_LEFT =
		820;

	/** Displayed at the top of the screen. */
	@Api
	public static final int SOFTKEY_TOP =
		840;

	/** Displayed on the right side of the screen. */
	@Api
	public static final int SOFTKEY_RIGHT =
		860;

	/** Displayed off-screen, using physical hardware buttons. */
	@Api
	public static final int SOFTKEY_OFFSCREEN =
		880;

	@Api
	public static final int STATE_BACKGROUND =
		0;

	@Api
	public static final int STATE_FOREGROUND =
		2;

	@Api
	public static final int STATE_VISIBLE =
		1;

	@Api
	public static final int SUPPORTS_ALERTS =
		32;

	@Api
	public static final int SUPPORTS_COMMANDS =
		2;

	@Api
	public static final int SUPPORTS_FILESELECTORS =
		512;

	@Api
	public static final int SUPPORTS_FORMS =
		4;

	@Api
	public static final int SUPPORTS_IDLEITEM =
		2048;

	/** This specifies that the display supports user input. */
	@Api
	public static final int SUPPORTS_INPUT_EVENTS =
		1;

	@Api
	public static final int SUPPORTS_LISTS =
		64;

	@Api
	public static final int SUPPORTS_MENUS =
		1024;

	@Api
	public static final int SUPPORTS_ORIENTATION_LANDSCAPE =
		8192;

	@Api
	@SuppressWarnings("FieldNamingConvention")
	public static final int SUPPORTS_ORIENTATION_LANDSCAPE180 =
		32768;

	@Api
	public static final int SUPPORTS_ORIENTATION_PORTRAIT =
		4096;

	@Api
	public static final int SUPPORTS_ORIENTATION_PORTRAIT180 =
		16384;

	@Api
	public static final int SUPPORTS_TABBEDPANES =
		256;

	@Api
	public static final int SUPPORTS_TEXTBOXES =
		128;

	@Api
	public static final int SUPPORTS_TICKER =
		8;

	@Api
	public static final int SUPPORTS_TITLE =
		16;

	@Api
	public static final int TAB =
		4;
	
	/** The display state. */
	final DisplayState _state;
	
	/** The associated screen this display is for. */
	private final ScritchScreenBracket _screen;
	
	/** The owning native window. */
	private final ScritchWindowBracket _window;
	
	/** The ScritchUI interface in use. */
	private final ScritchInterface _scritch;
	
	/** Display scaling. */
	private final DisplayScale _scale;
	
	/** Serial runs of a given method for this display. */
	@Deprecated
	final Map<Integer, Runnable> _serialRuns =
		new LinkedHashMap<>();
	
	/** The number of times there has been a non-unique serial run. */
	@Deprecated
	private static volatile int _NON_UNIQUE_SERIAL_RUNS;
	
	/** The displayable to show. */
	@Deprecated
	private volatile Displayable _current;
	
	/** The displayable to show on exit. */
	@Deprecated
	private volatile Displayable _exit;
	
	/** The layout policy of this display. */
	@Deprecated
	private CommandLayoutPolicy _layoutPolicy;
	
	/**
	 * Initializes the display instance.
	 *
	 * @param __scritch The ScritchUI interface used.
	 * @param __window The ScritchUI Window to use.
	 * @param __screen The screen this displays on.
	 * @param __uiDisplay The native display.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/16
	 */
	Display(ScritchInterface __scritch, ScritchWindowBracket __window,
		ScritchScreenBracket __screen, UIDisplayBracket __uiDisplay)
		throws NullPointerException
	{
		if (__scritch == null || __window == null || __screen == null ||
			__uiDisplay == null)
			throw new NullPointerException("NARG");
		
		// Initialize Display state
		this._state = new DisplayState(this, __window);
		this._scritch = __scritch;
		this._screen = __screen;
		this._window = __window;
		this._scale = new DisplayIdentityScale(this._scritch,
			this._screen, this._window);
	}
	
	/**
	 * Calls the given runner within the event handler serially.
	 * 
	 * Note that the {@link Runnable#run()} will be called as if it were
	 * serialized like everything else with {@link SerializedEvent}.
	 * 
	 * Calls to this method will never block and wait for the {@link Runnable}
	 * to complete.
	 * 
	 * @param __run The method to run.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/03
	 */
	@Api
	@NonBlocking
	@Async.Schedule
	public void callSerially(Runnable __run)
		throws NullPointerException
	{
		// Enqueue serialized call
		this.__queueSerialRunner(__run);
	}
	
	/**
	 * Flashes the backlight for the purpose of getting attention from the
	 * user. In SquirrelJME this just uses the system's notification system
	 * to call attention to the display window.
	 *
	 * @param __ms The number of milliseconds to flash for.
	 * @return {@code true} if the backlight is controlled by the application
	 * and the display is in the foreground, otherwise {@code false}.
	 * @since 2019/10/05
	 */
	@Api
	public boolean flashBacklight(int __ms)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB30 Cannot blink for a negative duration.} */
		if (__ms < 0)
			throw new IllegalArgumentException("EB30");
		
		// Just call attention to the user
		this._scritch.window().callAttention(this._window);
		return false;
	}
	
	/**
	 * Returns the current activity mode that the display is within, if
	 * active mode is set then the display will inhibit power saving features.
	 *
	 * @return Either {@link #MODE_ACTIVE} or {@link #MODE_NORMAL}.
	 * @see Display#setActivityMode(int) 
	 * @since 2016/10/08
	 */
	@Api
	public int getActivityMode()
	{
		if (this._scritch.environment().isInhibitingSleep())
			return Display.MODE_ACTIVE;
		return Display.MODE_NORMAL;
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
	 * @param __elem If display element.
	 * @return The height of the image for that element.
	 * @throws IllegalArgumentException On null arguments.
	 * @since 2016/10/14
	 */
	@Api
	public int getBestImageHeight(int __elem)
		throws IllegalArgumentException
	{
		ScritchLAFInterface laf =
			this._scritch.environment().lookAndFeel();
		
		return laf.imageSize(
			ScritchLcdUiUtils.scritchElementType(__elem), true);
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
	 * @param __elem If display element.
	 * @return The width of the image for that element.
	 * @throws IllegalArgumentException On null arguments.
	 * @since 2016/10/14
	 */
	@Api
	public int getBestImageWidth(int __elem)
		throws IllegalArgumentException
	{
		ScritchLAFInterface laf =
			this._scritch.environment().lookAndFeel();
		
		return laf.imageSize(
			ScritchLcdUiUtils.scritchElementType(__elem), false);
	}
	
	/**
	 * Returns the border style for items which are highlighted or otherwise
	 * not.
	 *
	 * @param __highlight If {@code true} then this should return the style
	 * for borders which are highlighted.
	 * @return The style for the given item.
	 * @since 2024/03/09
	 */
	@Api
	public int getBorderStyle(boolean __highlight)
	{
		ScritchLAFInterface laf =
			this._scritch.environment().lookAndFeel();
		
		return ScritchLcdUiUtils.lcduiLineStyle(
			laf.focusBorderStyle(__highlight));
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
	@Api
	public int getCapabilities()
	{
		throw Debugging.todo();
		/*
		// These are all standard and expected to always be supported
		int rv = Display.__defaultCapabilities();
			
		UIBackend backend = UIBackendFactory.getInstance(true);
		
		// Supports any kind of input?
		if (0 != backend.metric(_uiDisplay, UIMetricType.INPUT_FLAGS))
			rv |= Display.SUPPORTS_INPUT_EVENTS;
		
		return rv;*/
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
	@Api
	public int getColor(int __c)
		throws IllegalArgumentException
	{
		ScritchLAFInterface laf =
			this._scritch.environment().lookAndFeel();
		
		return laf.elementColor(
			ScritchLcdUiUtils.scritchElementColor(__c)) & 0xFFFFFF;
	}
	
	/**
	 * Returns the current command layout policy. The policy of the
	 * {@link Displayable} takes precedence.
	 * 
	 * @return The current command layout policy, may be {@code null}.
	 * @since 2020/09/27
	 */
	@Api
	public CommandLayoutPolicy getCommandLayoutPolicy()
	{
		return this._layoutPolicy;
	}
	
	/**
	 * Returns the preferred placement for commands.
	 * 
	 * @param __ct The command type, see {@link Command}.
	 * @return The preferred placements or {@code null} if there are none.
	 * @throws IllegalArgumentException If the command type is not valid.
	 * @since 2020/09/27
	 */
	@Api
	public int[] getCommandPreferredPlacements(int __ct)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB3l Invalid command type. (The type)} */
		if (__ct < Command.SCREEN || __ct > Command.ITEM)
			throw new IllegalArgumentException("EB3l " + __ct);
		
		// In SquirrelJME, commands are in the same places as menu items
		return this.getMenuPreferredPlacements();
	}
	
	/**
	 * Returns the current displayable.
	 *
	 * @return The current displayable or {@code null} if it is not set.
	 * @since 2016/10/08
	 */
	@Api
	public Displayable getCurrent()
	{
		return this._current;
	}
	
	/**
	 * Gets the state of the display. 
	 *
	 * @return One of {@link #STATE_BACKGROUND}, {@link #STATE_VISIBLE},
	 * or {@link #STATE_FOREGROUND}.
	 * @since 2024/03/09
	 */
	@Api
	public int getDisplayState()
	{
		ScritchWindowInterface scritch = this._scritch.window();
		
		if (scritch.isVisible(this._window))
		{
			if (scritch.hasFocus(this._window))
				return Display.STATE_FOREGROUND;
			return Display.STATE_VISIBLE;
		}
		
		return Display.STATE_BACKGROUND;
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
	@Api
	public int getDotPitch()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns all the possible exact placements where items may go on
	 * a given border.
	 * 
	 * The orientation of the display does affect the border positions, if
	 * the orientation has changed then this must be called again.
	 * 
	 * For top/bottom borders, the order is from left to right.
	 * 
	 * For left/right borders, the order is top to bottom.
	 * 
	 * The first possible placement on a border is always {@code BORDER + 1}.
	 * 
	 * @param __b The border to get, must be one of {@link #SOFTKEY_TOP},
	 * {@link #SOFTKEY_BOTTOM}, {@link #SOFTKEY_LEFT}, {@link #SOFTKEY_RIGHT},
	 * or {@link #SOFTKEY_OFFSCREEN}.
	 * @return The valid placements for the given border, or {@code null}
	 * if the border is not supported.
	 * @throws IllegalArgumentException If the border is not valid.
	 * @since 2020/09/27
	 */
	@Api
	public int[] getExactPlacementPositions(int __b)
		throws IllegalArgumentException
	{
		// Un-project the layout to get the correct order
		__b = this.__layoutProject(__b);
		
		// Depends on the border that was requested
		switch (__b)
		{
				// None of these positions are valid in SquirrelJME
			case Display.SOFTKEY_OFFSCREEN:
			case Display.SOFTKEY_TOP:
			case Display.SOFTKEY_LEFT:
			case Display.SOFTKEY_RIGHT:
				return null;
				
				// There are only two slots along the bottom of the screen
			case Display.SOFTKEY_BOTTOM:
				return new int[]{
					this.__layoutProject(Display.SOFTKEY_BOTTOM + 1),
					this.__layoutProject(Display.SOFTKEY_BOTTOM + 2)};
			
				/* {@squirreljme.error EB1p Invalid border. (The border)} */
			default:
				throw new IllegalArgumentException("EB1p " + __b);
		}
	}
	
	/**
	 * Returns the current hardware state.
	 *
	 * @return The hardware state.
	 * @since 2018/12/10
	 */
	@Api
	public int getHardwareState()
	{
		throw Debugging.todo();
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
	@Api
	public int getHeight()
	{
		return this._scale.textureMaxH();
	}
	
	@Api
	public IdleItem getIdleItem()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the preferred placement for menus.
	 * 
	 * @return The preferred placements or {@code null} if there are none.
	 * @since 2020/09/27
	 */
	@Api
	public int[] getMenuPreferredPlacements()
	{
		// The preferred placements for menus are the same as the supported
		// ones
		return this.getMenuSupportedPlacements();
	}
	
	/**
	 * Returns all the placements which support menu items.
	 *  
	 * @return The list of supported menu item placements.
	 * @since 2020/09/27
	 */
	@Api
	public int[] getMenuSupportedPlacements()
	{
		// In SquirrelJME, commands and menus can only be placed along the
		// bottom of the screen
		return this.getExactPlacementPositions(Display.SOFTKEY_BOTTOM);
	}
	
	/**
	 * Returns the current orientation of the display.
	 *
	 * @return The display orientation.
	 * @since 2017/10/27
	 */
	@Api
	public int getOrientation()
	{
		if (this._scritch.screen().isPortrait(this._screen))
			return Display.ORIENTATION_PORTRAIT;
		return Display.ORIENTATION_LANDSCAPE;
	}
	
	/**
	 * Returns the maximum width of the display.
	 *
	 * @return The maximum display width.
	 * @since 2016/10/14
	 */
	@Api
	public int getWidth()
	{
		return this._scale.textureMaxW();
	}
	
	/**
	 * Are mouse/stylus press and release events supported?
	 *
	 * @return {@code true} if they are supported.
	 * @since 2016/10/14
	 */
	@Api
	public boolean hasPointerEvents()
	{
		return (UIBackendFactory.getInstance(true).metric(_uiDisplay,
			UIMetricType.INPUT_FLAGS) & UIInputFlag.POINTER) ==
			(UIInputFlag.POINTER);
	}
	
	/**
	 * Are mouse/stylus move/drag events supported?
	 *
	 * @return {@code true} if they are supported.
	 * @since 2016/10/14
	 */
	@Api
	public boolean hasPointerMotionEvents()
	{
		return (UIBackendFactory.getInstance(true).metric(_uiDisplay,
			UIMetricType.INPUT_FLAGS) &
			(UIInputFlag.POINTER | UIInputFlag.POINTER_MOTION)) ==
			(UIInputFlag.POINTER | UIInputFlag.POINTER_MOTION);
	}
	
	/**
	 * Is this display built into the device or is it an auxiliary display?
	 *
	 * @return {@code true} if it is built-in.
	 * @since 2016/10/14
	 */
	@Api
	public boolean isBuiltIn()
	{
		return this._scritch.screen().isBuiltIn(this._screen);
	}
	
	/**
	 * Is color supported by this display?
	 *
	 * @return {@code true} if color is supported.
	 * @since 2016/10/14
	 */
	@Api
	public boolean isColor()
	{
		return UIBackendFactory.getInstance(true).metric(_uiDisplay,
			UIMetricType.DISPLAY_MONOCHROMATIC) == 0;
	}
	
	/**
	 * Returns the number of alpha-transparency levels. Alpha levels range
	 * from fully transparent to fully opaque.
	 * 
	 * It is required by implementations to support at least 16 levels of
	 * alpha transparency.
	 *
	 * @return The alpha transparency levels.
	 * @since 2016/10/14
	 */
	@Api
	@SuppressWarnings({"MagicNumber", "SwitchStatementWithTooFewBranches"})
	public int numAlphaLevels()
	{
		switch (UIBackendFactory.getInstance(true).metric(_uiDisplay,
			UIMetricType.DISPLAY_PIXEL_FORMAT))
		{
				// If the display format is 16-bit, just use this here
			case UIPixelFormat.SHORT_RGBA4444:
				return 16;
			
				// Use 256 since all other image formats would get their
				// alpha colors calculated.
			default:
				return 256;
		}
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
	@Api
	@SuppressWarnings("MagicNumber")
	public int numColors()
	{
		int pf;
		switch ((pf = UIBackendFactory.getInstance(true).metric(_uiDisplay,
			UIMetricType.DISPLAY_PIXEL_FORMAT)))
		{
			case UIPixelFormat.INT_RGB888:
			case UIPixelFormat.INT_RGBA8888:
				return 16_777_216;
			
			case UIPixelFormat.SHORT_INDEXED65536:
				return 65536;
			
			case UIPixelFormat.SHORT_RGB565:
				return 8192;
			
			case UIPixelFormat.SHORT_RGBA4444:
				return 4096;
			
			case UIPixelFormat.BYTE_INDEXED256:
				return 256;
			
			case UIPixelFormat.PACKED_INDEXED4:
				return 16;
			
			case UIPixelFormat.PACKED_INDEXED2:
				return 4;
			
			case UIPixelFormat.PACKED_INDEXED1:
				return 2;
			
				/* {@squirreljme.error EB3j Unhandled pixel format. (Format)} */
			default:
				throw Debugging.oops("EB3j", pf);
		}
	}
	
	/**
	 * Removes the currently displayed displayable.
	 * 
	 * @since 2020/10/04
	 */
	@Api
	public void removeCurrent()
	{
		// Just performs the internal hiding logic
		this.__doHideCurrent();
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
	@Api
	public void setActivityMode(int __m)
		throws IllegalArgumentException
	{
		// Valid?
		if (__m == Display.MODE_ACTIVE || __m == Display.MODE_NORMAL)
			this._scritch.environment().setInhibitSleep(
				__m == Display.MODE_ACTIVE);
	
		/* {@squirreljme.error EB1i Unknown activity mode specified.} */
		else
			throw new IllegalArgumentException("EB1i");
	}
	
	@Api
	public void setCommandLayoutPolicy(CommandLayoutPolicy __clp)
	{
		throw Debugging.todo();
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
	@Api
	public void setCurrent(Alert __show, Displayable __exit)
		throws DisplayCapabilityException, IllegalStateException,
			NullPointerException
	{
		/* {@squirreljme.error EB1j Cannot show another alert when the alert
		to show is cleared.} */
		if (__exit instanceof Alert)
			throw new IllegalStateException("EB1j");
		
		// Check
		if (__show == null || __exit == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB1k The displayable to show on exit after
		showing an alert cannot be an alert.} */
		if (__exit instanceof Alert)
			throw new IllegalStateException("EB1k");
		
		// Debug
		Debugging.debugNote("Showing alert \"%s\"", __show._message);
		
		// Perform call on this display
		throw Debugging.todo();
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
		
		/* {@squirreljme.error EB1l Could not set the alert and its exit
		displayable because it is already set on a display.} * /
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
	@Api
	public void setCurrent(Displayable __show)
		throws DisplayCapabilityException, IllegalStateException
	{
		// There technically is no background state in SquirrelJME, a
		// background state being one that turns off the display or
		// otherwise
		if (__show == null)
			return;
		
		// If we are trying to show the same display, force foreground
		Displayable current = this._current;
		if (current == __show)
		{
			// This will force the form to be currently shown on the screen
			// even if another task has set the form. Since displays may only
			// have a single form associated with them, this effectively
			// retakes control accordingly
			this.__doShowCurrent(__show);
			
			return;
		}
		
		// If showing an alert, it gets displayed instead
		if (__show instanceof Alert)
		{
			this.setCurrent((Alert)__show, this.getCurrent());
			return;
		}
		
		/* {@squirreljme.error EB1m The displayable to be displayed is already
		being displayed.} */
		if (__show._display != null)
			throw new IllegalStateException("EB1m");
		
		// Hide the current display then show the new one
		this.__doHideCurrent();
		this.__doShowCurrent(__show);
	}
	
	@Api
	public void setCurrentItem(Item __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setIdleItem(IdleItem __i)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setPreferredOrientation(int __o)
	{
		throw Debugging.todo();
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
	@Api
	public boolean vibrate(int __d)
		throws IllegalArgumentException
	{
		// Forward
		return Vibration.vibrate(__d);
	}
	
	/**
	 * Hides the current displayable.
	 * 
	 * @return The currently displayed displayable unless not set.
	 * @since 2020/09/27
	 */
	private Displayable __doHideCurrent()
	{
		// Nothing was ever visible on this display?
		Displayable current = this._current;
		if (current == null)
			return null;
		
		// Hide the form on the display, but only if it is currently shown
		// as we do not want to un-hide another form being displayed if it
		// is from another process
		if (current.__isShown())
			UIBackendFactory.getInstance(true)
				.displayShow(this._uiDisplay, null);
		
		// Unlink display
		current._display = null;
		this._current = null;
		
		// Inform canvases that they are now hidden
		if (current instanceof Canvas)
			((Canvas)current).hideNotify();
		
		return current;
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
		UIBackend backend = UIBackendFactory.getInstance(true);
		
		// Use the global callback thread
		synchronized (StaticDisplayState.class)
		{
			// Set callback for the displayed form so it can receive events
			backend.callback(
				__show.__state(Displayable.__DisplayableState__.class)._uiForm,
				StaticDisplayState.callback());
		}
		
		// Show the form on the display, as long as it is not already on there
		UIDisplayBracket uiDisplay = this._uiDisplay;
		UIFormBracket wasForm = backend.displayCurrent(uiDisplay);
		if (wasForm == null || !backend.equals(
			__show.__state(Displayable.__DisplayableState__.class)._uiForm, wasForm))
			backend.displayShow(uiDisplay,
				__show.__state(Displayable.__DisplayableState__.class)._uiForm);
		
		// Set new parent
		__show._display = this;
		this._current = __show;
		
		// Notify that it was shown
		this.__queueSerialRunner(new __NotifyShow__(__show));
	}
	
	/**
	 * Projects the border for the given layout according to the screen
	 * orientation. Calling this method twice with a previously projected item
	 * will result in the projection being reversed.
	 * 
	 * @param __b The border or item to project.
	 * @return The projection of the border.
	 * @since 2020/09/27
	 */
	final int __layoutProject(int __b)
	{
		// Get the true border and the item position
		int border = __b & (~Display.SOFTKEY_INDEX_MASK);
		int position = __b & Display.SOFTKEY_INDEX_MASK;
		
		// Depends if the display is flipped or not
		switch (this.getOrientation())
		{
				// Normal layout, does not get modified
			case Display.ORIENTATION_PORTRAIT:
			case Display.ORIENTATION_LANDSCAPE:
				return __b;
			
				// Rotation will adjust which border items appear on
			case Display.ORIENTATION_PORTRAIT_180:
			case Display.ORIENTATION_LANDSCAPE_180:
				switch (border)
				{
						// If the item is offscreen, it never gets adjusted
					case Display.SOFTKEY_OFFSCREEN:
						return __b;
					
					case Display.SOFTKEY_TOP:
						return Display.SOFTKEY_BOTTOM + position;
						
					case Display.SOFTKEY_BOTTOM:
						return Display.SOFTKEY_TOP + position;
						
					case Display.SOFTKEY_LEFT:
						return Display.SOFTKEY_RIGHT + position;
						
					case Display.SOFTKEY_RIGHT:
						return Display.SOFTKEY_LEFT + position;
					
						/* {@squirreljme.error EB3k Invalid border position.
						(The border position).} */
					default:
						throw new IllegalArgumentException("EB3k " + border);
				}
			
				// This should not occur
			default:
				throw Debugging.oops("Invalid orientation.");
		}
	}
	
	/**
	 * Queues the serial runner.
	 *
	 * @param __run The method to run.
	 * @return The identifier for the runner item.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/27
	 */
	@SuppressWarnings({"WrapperTypeMayBePrimitive"})
	final int __queueSerialRunner(Runnable __run)
		throws NullPointerException
	{
		if (__run == null)
			throw new NullPointerException("NARG");
		
		// Get next ID to use.
		Integer idRunner;
		synchronized (Display.class)
		{
			idRunner = (++Display._NON_UNIQUE_SERIAL_RUNS);
		}
		
		// Perform the serialization call
		synchronized (this)
		{
			// Store into the serial runner
			Map<Integer, Runnable> serialRuns = this._serialRuns;
			serialRuns.put(idRunner, __run);
		}
		
		// Perform the call so it is done later
		UIBackendFactory.getInstance(true)
			.later(this._uiDisplay, idRunner);
		
		// This is the ID used to refer to this runner
		return idRunner;
	}
	
	/**
	 * Performs a serial run.
	 * 
	 * @param __serialId The serial run ID.
	 * @since 2023/01/14
	 */
	@SerializedEvent
	@Async.Execute
	protected void __serialRun(int __serialId)
	{
		// Look to see if it is a valid call
		Integer key = __serialId;
		Runnable runner;
		synchronized (this)
		{
			// Locate the runner
			runner = this._serialRuns.get(key);
		}
		
		// Run it
		try
		{
			if (runner != null)
				runner.run();
		}
		finally
		{
			synchronized (this)
			{
				// Always clear it, even with failures
				this._serialRuns.remove(key);
				
				// Notify all the display threads that something happened
				this.notifyAll();
			}
		}
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
	@Api
	public static void addDisplayListener(DisplayListener __dl)
		throws NullPointerException
	{
		DisplayManager.instance().displayListenerAdd(__dl);
	}
	
	/**
	 * Obtains the display that is associated with the given MIDlet.
	 *
	 * @param __m The display to get the midlet for.
	 * @return The display for the given midlet.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/08
	 */
	@Api
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
		
		/* {@squirreljme.error EB1p Could not get the display for the specified
		MIDlet because no displays are available.} */
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
	@Api
	public static Display[] getDisplays(int __caps)
		throws IllegalStateException
	{
		// Get display interface
		DisplayManager manager = DisplayManager.instance();
		
		// Get tracker so we can map to actual displays and windows
		Display[] all = manager.mapScreens(new __NewDisplay__());
		
		// If we do not care for the capabilities of the displays then just
		// return all of them
		if (__caps == 0)
			return all.clone();
		
		// Find possible displays
		List<Display> possible = new ArrayList<>(all.length);
		for (Display potential : all)
			if ((potential.getCapabilities() & __caps) == __caps)
				possible.add(potential);
		
		/* {@squirreljme.error EB1q No displays are available.} */
		if (possible.isEmpty())
			throw new IllegalStateException("EB1q");
		
		return possible.<Display>toArray(new Display[possible.size()]);
	}
	
	/**
	 * Removes the specified display listener so that it is no longer called
	 * when events occur.
	 *
	 * @param __dl The listener to remove.
	 * @throws IllegalStateException If the listener is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	@Api
	public static void removeDisplayListener(DisplayListener __dl)
		throws IllegalStateException, NullPointerException
	{
		if (__dl == null)
			throw new NullPointerException("NARG");
		
		DisplayManager.instance().displayListenerRemove(__dl);
	}
	
	/**
	 * The default display capabilities.
	 * 
	 * @return The default display capabilities.
	 * @since 2021/11/30
	 */
	static int __defaultCapabilities()
	{
		return Display.SUPPORTS_COMMANDS | Display.SUPPORTS_FORMS |
			Display.SUPPORTS_TICKER | Display.SUPPORTS_ALERTS |
			Display.SUPPORTS_LISTS | Display.SUPPORTS_TEXTBOXES |
			Display.SUPPORTS_FILESELECTORS | Display.SUPPORTS_TABBEDPANES |
			Display.SUPPORTS_MENUS;
	}
	
	/**
	 * Converts a {@link UIItemPosition} to a softkey.
	 * 
	 * @param __pos The {@link UIItemPosition} to get the soft key of.
	 * @return The softkey position or a negative value if not valid.
	 * @since 2020/10/03
	 */
	static int __layoutPosToSoftKey(int __pos)
	{
		switch (__pos)
		{
			case UIItemPosition.LEFT_COMMAND:
				return Display._SOFTKEY_LEFT_COMMAND;
				
			case UIItemPosition.RIGHT_COMMAND:
				return Display._SOFTKEY_RIGHT_COMMAND;
			
			default:
				return -1;
		}
	}
	
	/**
	 * Returns the position where the soft key belongs.
	 * 
	 * @param __softKey The soft key to get the UI position from.
	 * @return The {@link UIItemPosition} of the item, or
	 * {@link UIItemPosition#NOT_ON_FORM} if not valid.
	 * @since 2020/10/03
	 */
	static int __layoutSoftKeyToPos(int __softKey)
	{
		switch (__softKey)
		{
			case Display._SOFTKEY_LEFT_COMMAND:
				return UIItemPosition.LEFT_COMMAND;
				
			case Display._SOFTKEY_RIGHT_COMMAND:
				return UIItemPosition.RIGHT_COMMAND;
			
			default:
				return UIItemPosition.NOT_ON_FORM;
		}
	}
}

