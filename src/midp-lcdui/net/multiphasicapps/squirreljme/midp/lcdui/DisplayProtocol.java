// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui;

/**
 * This contains display protocol constants.
 *
 * @since 2016/10/13
 */
public interface DisplayProtocol
{
	/**
	 * Request the number of displays that are available for usage.
	 *
	 * {@code
	 * In:
	 *   --Nothing--
	 * Out:
	 *   ubyte       (n) The number of displays available.
	 *   struct[n]
	 *   .ubyte          The display descriptor IDs.
	 *   .struct[?]
	 *   ..ubyte         DisplayProperty enum ordinal, 0 = stop.
	 *   ..int           The value of the property.
	 * }
	 */
	public static final byte COMMAND_REQUEST_NUMDISPLAYS =
		1;
	
	/**
	 * Binds to the given display.
	 *
	 * {@code
	 * In:
	 *   --Nothing--
	 * Out:
	 *   ubyte           The display to bind to.
	 * }
	 */
	public static final byte COMMAND_BIND_DISPLAY =
		2;
}

