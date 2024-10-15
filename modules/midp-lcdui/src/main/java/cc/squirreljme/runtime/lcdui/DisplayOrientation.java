// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Display;

/**
 * This is used to determine the orientation of the display.
 *
 * @since 2017/10/27
 */
@SquirrelJMEVendorApi
public enum DisplayOrientation
{
	/** Landscape. */
	@SquirrelJMEVendorApi
	LANDSCAPE,
	
	/** Landscape, rotated 180 degrees. */
	@SquirrelJMEVendorApi
	LANDSCAPE_180,
	
	/** Portrait. */
	@SquirrelJMEVendorApi
	PORTRAIT,
	
	/** Portrait, rotated 180 degrees. */
	@SquirrelJMEVendorApi
	PORTRAIT_180,
	
	/** End. */
	;
	
	/**
	 * Returns the value used in the LCDUI code.
	 *
	 * @return The LCDUI value.
	 * @since 2017/10/27
	 */
	@SquirrelJMEVendorApi
	public final int lcduiValue()
	{
		switch (this)
		{
			case LANDSCAPE:		return Display.ORIENTATION_LANDSCAPE;
			case LANDSCAPE_180:	return Display.ORIENTATION_LANDSCAPE_180;
			case PORTRAIT:		return Display.ORIENTATION_PORTRAIT;
			case PORTRAIT_180:	return Display.ORIENTATION_PORTRAIT_180;
			default:
				throw Debugging.oops();
		}
	}
}

