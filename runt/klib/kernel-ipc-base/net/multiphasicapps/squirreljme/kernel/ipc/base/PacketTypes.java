// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.ipc.base;

/**
 * This represents the packet types which are sent between the clients.
 *
 * Packet types which are negative do not have a response value returned and
 * are essentially just events.
 *
 * @since 2018/01/01
 */
public interface PacketTypes
{
	/** This is a special type used to indicate an okay response. */
	public static final int SPECIAL_RESPONSE_OKAY =
		0;
	
	/** This is a special type used to indicate a failed response. */
	public static final int SPECIAL_RESPONSE_FAIL =
		Integer.MIN_VALUE;
	
	/** The hello packet which indicates the remote side is alive. */
	public static final int HELLO =
		-1;
	
	/** The client has been started (constructors are okay). */
	public static final int INITIALIZED =
		-2;
	
	/** Map service. */
	public static final int MAP_SERVICE =
		1;
}

