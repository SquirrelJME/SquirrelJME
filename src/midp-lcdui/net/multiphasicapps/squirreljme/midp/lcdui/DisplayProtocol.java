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
	 *   .int            LCDUI Display capabilities
	 *   .int            "Extended" SquirrelJME specific capabilities.
	 * }
	 */
	public static final byte COMMAND_REQUEST_NUMDISPLAYS =
		1;
	
	/** Are there pointer events? */
	public static final int EXTENDED_CAPABILITY_POINTER_EVENTS =
		0x0000_0001;
	
	/** Are there pointer motion events? */
	public static final int EXTENDED_CAPABILITY_POINTER_MOTION_EVENTS =
		0x0000_0002;
	
	/** Is color supported? */
	public static final int EXTENDED_CAPABILITY_COLOR =
		0x0000_0004;
}

