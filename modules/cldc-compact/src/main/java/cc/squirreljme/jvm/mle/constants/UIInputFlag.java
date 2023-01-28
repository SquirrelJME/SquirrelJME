// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Bit field for input types that are supported.
 *
 * @since 2020/10/03
 */
@Exported
public interface UIInputFlag
{
	/** Has gamepad input. */
	@Exported
	byte GAMEPAD =
		1;
	
	/** Has keyboard input. */
	@Exported
	byte KEYBOARD =
		2;
	
	/** Has pointer input. */
	@Exported
	byte POINTER =
		4;
	
	/** Has pointer motion. */
	@Exported
	byte POINTER_MOTION =
		8;
	
	/** All input types. */
	@Exported
	byte ALL_MASK =
		UIInputFlag.GAMEPAD | UIInputFlag.KEYBOARD |
		UIInputFlag.POINTER | UIInputFlag.POINTER_MOTION;
}
