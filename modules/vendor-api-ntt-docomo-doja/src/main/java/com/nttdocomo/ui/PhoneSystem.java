// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.BacklightControl;
import cc.squirreljme.runtime.lcdui.mle.Vibration;
import cc.squirreljme.runtime.nttdocomo.ui.VendorPhoneSystem;

@Api
public class PhoneSystem
{
	/** Maximum time for a steady vibration. */
	private static final int _MAX_VIBRATION_TIME = 7_000;
	
	@Api
	public static final int ATTR_BACKLIGHT_OFF = 0;
	
	@Api
	public static final int ATTR_BACKLIGHT_ON = 1;
	
	@Api
	public static final int DEV_BACKLIGHT = 0;
	
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
			if (__value == PhoneSystem.ATTR_BACKLIGHT_ON)
				BacklightControl.setLevel(BacklightControl.MAX_LEVEL);
			else if (__value == PhoneSystem.ATTR_BACKLIGHT_OFF)
				BacklightControl.setLevel(BacklightControl.MIN_LEVEL);
			
			return;
		}
		
		// Vibration
		else if (__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_F503I_SO503I ||
			__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_P503I)
		{
			// Different phones have different means of turning on the shake
			boolean isOn;
			if (__attr == VendorPhoneSystem.VIBRATE_ATTRIBUTE_P503I)
				isOn = (__value == 1);
			else
				isOn = (__value == 1 || __value == 64);
			
			// Perform the vibration
			Vibration.vibrate((isOn ? PhoneSystem._MAX_VIBRATION_TIME : 0));
			
			return;
		}
		
		// Unsupported??
		throw Debugging.todo("Attribute %d = %d", __attr, __value);
	}
}
