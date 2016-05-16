// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.event;

/**
 * This represents the kind of event that was used.
 *
 * Note that human input devices allow for four ports, this is used for
 * video game consoles and such where multiple players and controllers may
 * be attached to a given system. This is generally for games which may be ran
 * on SquirrelJME.
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
	 *
	 * {@code
	 * 0bssss__qqqq_qqqq__rrrr_rrrr__SSSS_SSSS
	 *
	 * q = Event code parameter 1, if applicable (0-255).
	 * r = Event code parameter 2, if applicable (0-255).
	 * s = Event code parameter 3, if applicable (0-31).
	 * S = A {@link SystemEventCode} representing the event that occured.
	 * }
	 */
	SYSTEM,
	
	/**
	 * A key was pressed.
	 *
	 * The key chars specified here are idealized and are unique for a given
	 * key (this means {@code shift+a} results in {@code A} and {@code a}
	 * results in {@code A}. Note that this only applies to letter keys and
	 * not symbols.
	 *
	 * {@code
	 * 0bPP??__????_????__cccc_cccc__cccc_cccc
	 *
	 * P = The controller port, from 1-4.
	 * c = The {@link KeyChars} or {@code char} which was pressed.
	 * }
	 */
	KEY_PRESSED,
	
	/**
	 * A key was released.
	 *
	 * The same rules for key pressing are used.
	 *
	 * The same bit layout for key pressing is used.
	 */
	KEY_RELEASED,
	
	/**
	 * A key was typed.
	 *
	 * Typed keys match the character that was typed on the keyboard with
	 * shift modifiers and such taken into consideration for letters.
	 *
	 * The same bit layout for key pressing is used.
	 */
	KEY_TYPED,
	
	/**
	 * The pen was moved.
	 *
	 * {@code
	 * 0bPPnn__xxxx_xxxx__xxxx_yyyy__yyyy_yyyy
	 * 
	 * P = The controller port, from 1-4.
	 * n = The pen number that changed position.
	 * x = The unsigned X coordinate of the pen.
	 * y = The unsigned Y coordinate of the pen.
	 * }
	 */
	PEN_POSITION_CHANGED,
	
	/**
	 * Joystick axis changed.
	 *
	 * {@code
	 * 0bPPaa__xxxx_xxxx__xxxx_yyyy__yyyy_yyyy
	 *
	 * P = The controller port, from 1-4.
	 * a = The analog stick that changed.
	 * x = The signed joystick X value.
	 * y = The signed joystick Y value.
	 * }
	 */
	JOYSTICK_AXIS_CHANGED,
	
	/** End. */
	;
}

