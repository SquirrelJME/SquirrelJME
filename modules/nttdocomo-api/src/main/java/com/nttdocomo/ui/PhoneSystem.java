// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.BacklightControl;

public class PhoneSystem
{
	public static final int ATTR_BACKLIGHT_OFF =
		0;

	public static final int ATTR_BACKLIGHT_ON =
		1;

	public static final int DEV_BACKLIGHT =
		0;

	public static final int MAX_VENDOR_ATTR =
		127;

	public static final int MIN_VENDOR_ATTR =
		64;
	
	@SuppressWarnings("FinalStaticMethod")
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
		
		// Unsupported??
		throw Debugging.todo("Attribute %d = %d", __attr, __value);
	}
}
