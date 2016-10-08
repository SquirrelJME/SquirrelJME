// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.displays.terminal;

/**
 * This is the terminal interface which is implemented given by
 * {@link TerminalProvider}s to allow native terminal access.
 *
 * @since 2016/09/11
 */
public interface Terminal
{
	/**
	 * Returns the current terminal screen that is used by the terminal.
	 *
	 * If the terminal driver supports resizing the terminal then it will
	 * return a new screen if the display is resized.
	 *
	 * @return The terminal screen.
	 * @since 2016/09/11
	 */
	public abstract TerminalScreen screen();
}

