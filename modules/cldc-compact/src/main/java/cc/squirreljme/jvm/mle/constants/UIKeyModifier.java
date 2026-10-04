// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Modifier keys for when key and mouse events occur.
 *
 * @since 2020/08/02
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface UIKeyModifier
{
	/** Alt key modifier. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MODIFIER_ALT =
		0x10000;
	
	/** Shift key modifier. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MODIFIER_SHIFT =
		0x20000;
	
	/** Control (Ctrl) key modifier. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MODIFIER_CTRL =
		0x40000;
	
	/** Left Command Button. (SquirrelJME). */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MODIFIER_LEFT_COMMAND =
		0x80000;
	
	/** Right Command button. (SquirrelJME). */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MODIFIER_RIGHT_COMMAND =
		0x100000;
	
	/** Command key modifier. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MODIFIER_COMMAND =
		0x400000;
		
	/** The Function key. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MODIFIER_FUNCTION =
		0x800000;
	
	/** Left and right commands, used for middle command. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MODIFIER_LEFT_RIGHT_COMMANDS =
		UIKeyModifier.MODIFIER_LEFT_COMMAND |
		UIKeyModifier.MODIFIER_RIGHT_COMMAND;
		
	/** Mask specifically for the limits of J2ME. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int J2ME_MASK =
		UIKeyModifier.MODIFIER_ALT |
		UIKeyModifier.MODIFIER_FUNCTION | UIKeyModifier.MODIFIER_COMMAND |
		UIKeyModifier.MODIFIER_CTRL | UIKeyModifier.MODIFIER_SHIFT;
	
	/** Mask for all the modifier keys. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MASK =
		UIKeyModifier.J2ME_MASK |
		UIKeyModifier.MODIFIER_LEFT_RIGHT_COMMANDS;
	
}
