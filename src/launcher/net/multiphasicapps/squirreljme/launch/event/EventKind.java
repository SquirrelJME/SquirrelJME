// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch.event;

/**
 * This represents the kind of event that was used.
 *
 * @since 2016/05/15
 */
public enum EventKind
{
	/**
	 * A system event which just happens.
	 *
	 * These are not keys because they occur once and they in general do not
	 * have pressed and released states.
	 */
	SYSTEM,
	
	/**
	 * A key was pressed.
	 *
	 * The key chars specified here are idealized and are unique for a given
	 * key (this means {@code shift+a} results in {@code A} and {@code a}
	 * results in {@code A}. Note that this only applies to letter keys and
	 * not symbols.
	 */
	KEY_PRESSED,
	
	/**
	 * A key was released.
	 *
	 * The same rules for key pressing are used.
	 */
	KEY_RELEASED,
	
	/**
	 * A key was typed.
	 *
	 * Typed keys match the character that was typed on the keyboard with
	 * shift modifiers and such taken into consideration for letters.
	 */
	KEY_TYPED,
	
	/** The mouse was moved. */
	MOUSE_POSITION_MOVED,
	
	/** Joystick axis changed. */
	JOYSTICK_AXIS_CHANGED,
	
	/** End. */
	;
}

