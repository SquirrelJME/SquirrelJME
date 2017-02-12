// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

/**
 * This is used for connectors which are capable of receiving key events.
 *
 * @since 2017/02/12
 */
public interface KeyEventCapableConnector
{
	/**
	 * This is called when a key event has been generated.
	 *
	 * @param __code The keycode.
	 * @param __mods The modifiers to the key, these will match those
	 * of {@link javax.microedition.lcdui.KeyListener}.
	 * @see javax.microedition.lcdui.KeyListener
	 * @since 2017/02/12
	 */
	public abstract void keyEvent(int __code, int __mods);
}

