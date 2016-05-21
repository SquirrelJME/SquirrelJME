// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui.ipc;

/**
 * All packets sent over the display manager all start with a 16-bit prefix
 * which identifies the command to be executed over the IPC interface. Any
 * data that is used by the command must be specified following a short
 * description of it.
 *
 * Commands are 16-bits wide and values 1 through 65535 are valid.
 *
 * @since 2016/05/21
 */
public interface DMCommandID
{
	/**
	 * This is used to indicate that the given command has failed due to
	 * illegal input or otherwise.
	 *
	 * IPO -- A {@link String} which specifies the description of the error
	 * or excpetion which was thrown.
	 */
	public static final int ERROR =
		1;
}

