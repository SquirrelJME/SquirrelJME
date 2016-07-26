// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This is a packet identifier which identifies the type of the next
 * non-deterministic data in the replay file.
 *
 * @since 2016/07/25
 */
public enum EmulatorPacketType
{
	/** Not a valid packet type. */
	INVALID_PACKET_TYPE,
	
	/** Create new system. */
	CREATE_SYSTEM,
	
	/** Add a component to the system. */
	ADD_COMPONENT,
	
	/** End. */
	;
}

