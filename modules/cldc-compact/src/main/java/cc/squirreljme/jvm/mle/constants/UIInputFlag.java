// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Bit field for input types that are supported.
 *
 * @since 2020/10/03
 */
public interface UIInputFlag
{
	/** Has gamepad input. */
	byte GAMEPAD =
		1;
	
	/** Has keyboard input. */
	byte KEYBOARD =
		2;
	
	/** Has pointer input. */
	byte POINTER =
		4;
	
	/** Has pointer motion. */
	byte POINTER_MOTION =
		8;
	
	/** All input types. */
	byte ALL_MASK =
		UIInputFlag.GAMEPAD | UIInputFlag.KEYBOARD |
		UIInputFlag.POINTER | UIInputFlag.POINTER_MOTION;
}
