// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.BacklightControl;
import cc.squirreljme.runtime.lcdui.mle.Vibration;
import cc.squirreljme.runtime.midlet.DoJaRuntime;
import cc.squirreljme.runtime.nttdocomo.ui.VendorPhoneSystem;
import javax.microedition.lcdui.Displayable;

@Api
public class PhoneSystem
{
	/** Maximum time for a steady vibration. */
	private static final int _MAX_VIBRATION_TIME = 2_000;
	
	/** Backlight is turned off. */
	@Api
	public static final int ATTR_BACKLIGHT_OFF = 0;
	
	/** Backlight is turned on. */
	@Api
	public static final int ATTR_BACKLIGHT_ON = 1;
	
	/** Vibrator is turned off. */
	@Api
	public static final int ATTR_VIBRATOR_OFF = 0;
	
	/** Vibrator is turned on. */
	@Api
	public static final int ATTR_VIBRATOR_ON = 1;
	
	/** Backlight device control. */
	@Api
	public static final int DEV_BACKLIGHT = 0;
	
	/** Vibrator device control. */
	@Api
	public static final int DEV_VIBRATOR = 1;
	
	@Api
	public static final int MAX_VENDOR_ATTR = 127;
	
	@Api
	public static final int MIN_VENDOR_ATTR = 64;
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final void setAttribute(int __attr, int __value)
	{
		// Backlight control
		if (__attr == PhoneSystem.DEV_BACKLIGHT)
		{
			// Change level
			if (__value == PhoneSystem.ATTR_BACKLIGHT_ON)
				BacklightControl.setLevel(BacklightControl.MAX_LEVEL);
			else if (__value == PhoneSystem.ATTR_BACKLIGHT_OFF)
				BacklightControl.setLevel(BacklightControl.MIN_LEVEL);
			
			// Some software such as Final Fantasy 1 for DoJa sets the
			// backlight constantly every frame to force the screen to refresh,
			// despite there being repaint()
			javax.microedition.lcdui.Display display = Display.__midpDisplay();
			if (display != null)
			{
				Displayable current = display.getCurrent();
				if (current instanceof javax.microedition.lcdui.Canvas)
					((javax.microedition.lcdui.Canvas)current).repaint();
			}
			
			return;
		}
		
		// Vibration
		else if (__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_F503I_SO503I ||
			__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_P503I ||
			__attr == PhoneSystem.DEV_VIBRATOR)
		{
			// Vibration is not available before DoJa 2.0, if the standard
			// device vibration is selected then just ignore it
			if (__attr == PhoneSystem.DEV_VIBRATOR &&
				DoJaRuntime.versionBefore(2, 0))
				return;
			
			// Different phones have different means of turning on the shake
			boolean isOn;
			if (__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_P503I ||
				__attr == PhoneSystem.DEV_VIBRATOR)
				isOn = (__value == PhoneSystem.ATTR_VIBRATOR_ON);
			else
				isOn = (__value == PhoneSystem.ATTR_VIBRATOR_ON ||
					__value == 64);
			
			// Perform the vibration
			Vibration.vibrate((isOn ? PhoneSystem._MAX_VIBRATION_TIME : 0));
			
			return;
		}
		
		// Unsupported??
		throw Debugging.todo("Attribute %d = %d", __attr, __value);
	}
}
