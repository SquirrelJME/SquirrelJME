// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.constants;

import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputDialPadListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputGamepadListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputImeListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputKeyboardListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputMouseListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputNumPadListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputRockerListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputTouchScreenListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputTrackPointListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Input method flag types supported for ScritchUI.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public interface ScritchInputMethodType
{
	/** Full Keyboard, {@link ScritchInputKeyboardListener}. */
	@SquirrelJMEVendorApi
	byte FULL_KEYBOARD =
		1;
	
	/** Mouse, {@link ScritchInputMouseListener}. */
	@SquirrelJMEVendorApi
	byte MOUSE =
		2;
	
	/** Track-point/Joystick, {@link ScritchInputTrackPointListener}. */
	@SquirrelJMEVendorApi
	byte TRACK_POINT =
		4;
	
	/** Touchscreen/Tablet. {@link ScritchInputTouchScreenListener}. */
	@SquirrelJMEVendorApi
	byte TOUCHSCREEN =
		8;
	
	/** Phone dial pad, {@link ScritchInputDialPadListener}. */
	@SquirrelJMEVendorApi
	byte DIAL_PAD =
		16;
	
	/** Number pad only, {@link ScritchInputNumPadListener}. */
	@SquirrelJMEVendorApi
	byte NUMPAD_ONLY =
		32;
	
	/** Game controller, {@link ScritchInputGamepadListener}. */
	@SquirrelJMEVendorApi
	byte GAME_CONTROLLER =
		64;
	
	/** Rocker, left/right and select, {@link ScritchInputRockerListener}. */
	@SquirrelJMEVendorApi
	short ROCKER =
		128;
	
	/** System enabled IME, {@link ScritchInputImeListener}. */
	@SquirrelJMEVendorApi
	short IME =
		256;
}
