// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Flags representing the type of input method that may be available, this can
 * be used to allow for text and dial-pad input on less capable devices.
 *
 * @since 2026/01/07
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface UIInputMethod
{
	/** A dial-pad such as one on a phone. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DIAL_PAD =
		0x1;

	/** A number pad such as one on a computer keyboard. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUMBER_PAD =
		0x2;

	/** A basic keyboard; glyphs only; no functions. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BASIC_KEYBOARD =
		0x4;

	/** A full keyboard with function keys; number pad is another bit. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FULL_KEYBOARD =
		0x8;

	/** A rocker or hat capable of moving left or right. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ROCKER_LEFT_RIGHT =
		0x10;

	/** A rocker or hat capable of moving up or down. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ROCKER_UP_DOWN =
		0x20;

	/** A pointer that is always on the device; such as a mouse. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ATTACHED_POINTER =
		0x40;

	/** A pointer that can appear at will; such as a stylus/finger. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short DETACHED_POINTER =
		0x80;

	/** Has button A. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short BUTTON_A =
		0x100;

	/** Has button B. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short BUTTON_B =
		0x200;

	/** Has button C. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short BUTTON_C =
		0x400;

	/** Has button D. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short BUTTON_D =
		0x800;
}
