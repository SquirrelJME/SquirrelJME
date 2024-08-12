// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.lcdui.Canvas;

/**
 * This represents a single display for an application, it is equivalent to
 * {@link javax.microedition.lcdui.Display}.
 *
 * @see javax.microedition.lcdui.Display
 * @since 2021/11/30
 */
@Api
public class Display
{
	@Api
	public static final int KEY_0 =
		0;
	
	@Api
	public static final int KEY_1 =
		1;
	
	@Api
	public static final int KEY_2 =
		2;
	
	@Api
	public static final int KEY_3 =
		3;
	
	@Api
	public static final int KEY_4 =
		4;
	
	@Api
	public static final int KEY_5 =
		5;
	
	@Api
	public static final int KEY_6 =
		6;
	
	@Api
	public static final int KEY_7 =
		7;
	
	@Api
	public static final int KEY_8 =
		8;
	
	@Api
	public static final int KEY_9 =
		9;
	
	@Api
	public static final int KEY_ASTERISK =
		10;
	
	@Api
	public static final int KEY_CAMERA_SELECT =
		59;
	
	@Api
	public static final int KEY_CAMERA_ZOOM_IN =
		57;
	
	@Api
	public static final int KEY_CAMERA_ZOOM_OUT =
		58;
	
	@Api
	public static final int KEY_CLEAR =
		32;
	
	@Api
	public static final int KEY_DOWN =
		19;
	
	@Api
	public static final int KEY_GPS =
		42;
	
	@Api
	public static final int KEY_IAPP =
		24;
	
	@Api
	public static final int KEY_LEFT =
		16;
	
	@Api
	public static final int KEY_LOWER_LEFT =
		29;
	
	@Api
	public static final int KEY_LOWER_RIGHT =
		28;
	
	@Api
	public static final int KEY_MAIL =
		33;
	
	@Api
	public static final int KEY_MEMO =
		34;
	
	@Api
	public static final int KEY_MY_SELECT =
		53;
	
	@Api
	public static final int KEY_PAGE_DOWN =
		31;
	
	@Api
	public static final int KEY_PAGE_UP =
		30;
	
	@Api
	public static final int KEY_POUND =
		11;
	
	@Api
	public static final int KEY_PRESSED_EVENT =
		0;
	
	@Api
	public static final int KEY_RELEASED_EVENT =
		1;
	
	@Api
	public static final int KEY_RIGHT =
		18;
	
	@Api
	public static final int KEY_ROLL_LEFT =
		48;
	
	@Api
	public static final int KEY_ROLL_RIGHT =
		49;
	
	@Api
	public static final int KEY_SELECT =
		20;
	
	@Api
	public static final int KEY_SOFT1 =
		21;
	
	@Api
	public static final int KEY_SOFT2 =
		22;
	
	@Api
	public static final int KEY_SUB1 =
		50;
	
	@Api
	public static final int KEY_SUB2 =
		51;
	
	@Api
	public static final int KEY_SUB3 =
		52;
	
	@Api
	public static final int KEY_UP =
		17;
	
	@Api
	public static final int KEY_UPPER_LEFT =
		26;
	
	@Api
	public static final int KEY_UPPER_RIGHT =
		27;
	
	@Api
	public static final int KEY_VOICE =
		39;
	
	@Api
	protected static final int MAX_OPTION_KEY = 63;
	
	@Api
	protected static final int MAX_VENDOR_EVENT = 127;
	
	@Api
	protected static final int MAX_VENDOR_KEY = 0x1F;
	
	@Api
	public static final int MEDIA_EVENT = 8;
	
	@Api
	public static final int MIN_VENDOR_EVENT = 64;
	
	@Api
	public static final int MIN_VENDOR_KEY = 0x1A;
	
	@Api
	public static final int RESET_VM_EVENT = 5;
	
	@Api
	public static final int RESUME_VM_EVENT = 4;
	
	@Api
	public static final int TIMER_EXPIRED_EVENT = 7;
	
	@Api
	public static final int UPDATE_VM_EVENT = 6;
	
	/** The backing MIDP Display. */
	private static volatile javax.microedition.lcdui.Display _midpDisplay;
	
	/** The current frame being displayed. */
	private static volatile Frame _currentFrame;
	
	/**
	 * Internal only.
	 *
	 * @since 2021/11/30
	 */
	private Display()
	{
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final Frame getCurrent()
	{
		synchronized (Display.class)
		{
			return Display._currentFrame;
		}
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final int getHeight()
	{
		return Display.__midpDisplay().getHeight();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final int getWidth()
	{
		return Display.__midpDisplay().getWidth();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final boolean isColor()
	{
		return Display.__midpDisplay().isColor();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final int numColors()
	{
		return Display.__midpDisplay().numColors();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final void setCurrent(Frame __frame)
	{
		synchronized (Display.class)
		{
			// Do nothing if this is the same
			Frame currentFrame = Display._currentFrame;
			if (currentFrame == __frame)
				return;
			
			// Try changing the display first
			javax.microedition.lcdui.Display display =
				Display.__midpDisplay();
			display.setCurrent(
				(__frame == null ? null : __frame.__displayable()));
			
			// Is now the current frame
			Display._currentFrame = __frame;
		}
	}
	
	/**
	 * Returns the true display to use, only the first is used.
	 *
	 * @return The true display to use.
	 * @since 2022/02/14
	 */
	static javax.microedition.lcdui.Display __midpDisplay()
	{
		javax.microedition.lcdui.Display rv = Display._midpDisplay;
		if (rv != null)
			return rv;
		
		// {@squirreljme.error AH0n No native displays available.}
		javax.microedition.lcdui.Display[] midpDisplays =
			javax.microedition.lcdui.Display.getDisplays(0);
		if (midpDisplays == null || midpDisplays.length == 0)
			throw new IllegalStateException("AH0n");
		
		// Cache and use this display
		Display._midpDisplay = (rv = midpDisplays[0]);
		return rv;
	}
	
	/**
	 * Maps a MIDP Key to DoJa.
	 *
	 * @param __code The code to map.
	 * @return The mapped key.
	 * @since 2022/02/14
	 */
	static int __mapKey(int __code)
	{
		switch (__code)
		{
			case Canvas.KEY_UP:
			case NonStandardKey.VGAME_UP:
				return Display.KEY_UP;
			case Canvas.KEY_DOWN:
			case NonStandardKey.VGAME_DOWN:
				return Display.KEY_DOWN;
			case Canvas.KEY_LEFT:
			case NonStandardKey.VGAME_LEFT:
				return Display.KEY_LEFT;
			case Canvas.KEY_RIGHT:
			case NonStandardKey.VGAME_RIGHT:
				return Display.KEY_RIGHT;
				
			case Canvas.KEY_NUM0:
			case NonStandardKey.NUMPAD_0:
				return Display.KEY_0;
			case Canvas.KEY_NUM1:
			case NonStandardKey.NUMPAD_1:
				return Display.KEY_1;
			case Canvas.KEY_NUM2:
			case NonStandardKey.NUMPAD_2:
				return Display.KEY_2;
			case Canvas.KEY_NUM3:
			case NonStandardKey.NUMPAD_3:
				return Display.KEY_3;
			case Canvas.KEY_NUM4:
			case NonStandardKey.NUMPAD_4:
				return Display.KEY_4;
			case Canvas.KEY_NUM5:
			case NonStandardKey.NUMPAD_5:
				return Display.KEY_5;
			case Canvas.KEY_NUM6:
			case NonStandardKey.NUMPAD_6:
				return Display.KEY_6;
			case Canvas.KEY_NUM7:
			case NonStandardKey.NUMPAD_7:
				return Display.KEY_7;
			case Canvas.KEY_NUM8:
			case NonStandardKey.NUMPAD_8:
				return Display.KEY_8;
			case Canvas.KEY_NUM9:
			case NonStandardKey.NUMPAD_9:
				return Display.KEY_9;
			case Canvas.KEY_STAR:
			case NonStandardKey.NUMPAD_MULTIPLY:
				return Display.KEY_ASTERISK;
			case Canvas.KEY_POUND:
			case NonStandardKey.NUMPAD_DIVIDE:
				return Display.KEY_POUND;
			
			case Canvas.KEY_SELECT:
			case Canvas.KEY_ENTER:
			case NonStandardKey.NUMPAD_ENTER:
			case NonStandardKey.VGAME_FIRE:
				return Display.KEY_SELECT;
			
			// SquirrelJME specific keys
			case NonStandardKey.F1:
			case NonStandardKey.VGAME_COMMAND_LEFT:
				return Display.KEY_SOFT1;
			case NonStandardKey.F2:
			case NonStandardKey.VGAME_COMMAND_RIGHT:
				return Display.KEY_SOFT2;
		}
		
		// Unknown
		return Display.MAX_VENDOR_KEY;
	}
}
