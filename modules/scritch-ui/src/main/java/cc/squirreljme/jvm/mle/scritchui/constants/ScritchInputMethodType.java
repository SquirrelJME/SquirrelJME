// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Input method flag types supported for ScritchUI.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public interface ScritchInputMethodType
{
	/** Full Keyboard. */
	byte FULL_KEYBOARD =
		1;
	
	/** Mouse. */
	byte MOUSE =
		2;
	
	/** Track-point/Joystick. */
	byte TRACK_POINT =
		4;
	
	/** Touchscreen/Tablet. */
	byte TOUCHSCREEN =
		8;
	
	/** Phone dial pad. */
	byte DIAL_PAD =
		16;
	
	/** Number pad only. */
	byte NUMPAD_ONLY =
		32;
	
	/** Game controller. */
	byte GAME_CONTROLLER =
		64;
	
	/** Rocker, left/right and select. */
	short ROCKER =
		128;
}
