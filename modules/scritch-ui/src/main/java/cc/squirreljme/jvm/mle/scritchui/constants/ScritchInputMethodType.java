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
	/** Unknown event. */
	@SquirrelJMEVendorApi
	int UNKNOWN =
		0;
	
	/** Key event: Pressed. */
	@SquirrelJMEVendorApi
	int KEY_PRESSED =
		1;
	
	/** Key event: Released. */
	@SquirrelJMEVendorApi
	int KEY_RELEASED =
		2;
	
	/** Key event: Repeated. */
	@SquirrelJMEVendorApi
	int KEY_REPEATED =
		4;
	
	/** Mouse event: Button pressed. */
	@SquirrelJMEVendorApi
	int MOUSE_BUTTON_PRESSED =
		8;
	
	/** Mouse event: Button released. */
	@SquirrelJMEVendorApi
	int MOUSE_BUTTON_RELEASED =
		16;
	
	/** Mouse event: Motion. */
	@SquirrelJMEVendorApi
	int MOUSE_MOTION =
		32;
	
	/** Gamepad event: Button pressed. */
	@SquirrelJMEVendorApi
	int GAMEPAD_BUTTON_PRESSED =
		64;
	
	/** Gamepad event: Button released. */
	@SquirrelJMEVendorApi
	int GAMEPAD_BUTTON_RELEASED =
		128;
	
	/** Gamepad event: Motion on axis. */
	@SquirrelJMEVendorApi
	int GAMEPAD_AXIS_MOTION =
		256;
	
	/** Touch event: Finger pressed. */
	@SquirrelJMEVendorApi
	int TOUCH_FINGER_PRESSED =
		512;
	
	/** Touch event: Finger released. */
	@SquirrelJMEVendorApi
	int TOUCH_FINGER_RELEASED =
		1024;
	
	/** Touch event: Drag motion. */
	@SquirrelJMEVendorApi
	int TOUCH_DRAG_MOTION =
		2048;
	
	/** Stylus event: Pressed. */
	@SquirrelJMEVendorApi
	int STYLUS_PEN_PRESSED =
		4096;
	
	/** Stylus event: Released. */
	@SquirrelJMEVendorApi
	int STYLUS_PEN_RELEASED =
		8192;
	
	/** Stylus event: Dragging motion. */
	@SquirrelJMEVendorApi
	int STYLUS_DRAG_MOTION =
		16384;
	
	/** Stylus event: Hovering over display. */
	@SquirrelJMEVendorApi
	int STYLUS_HOVER_MOTION =
		32768;
	
	/** Gyroscope event: Axis motion. */
	@SquirrelJMEVendorApi
	int GYRO_AXIS_MOTION =
		65536;
	
	/** Device action (flip open/close, shaken, not stirred). */
	@SquirrelJMEVendorApi
	int DEVICE_ACTION =
		131072;
	
	/** A keyboard character was typed. */
	@SquirrelJMEVendorApi
	int KEY_CHAR_PRESSED =
		262144;
}
