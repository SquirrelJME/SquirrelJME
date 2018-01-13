// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.ipc.base;

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
		-32768;
	
	/** This is used to indicate that an exception was thrown. */
	public static final int SPECIAL_REPONSE_EXCEPTION =
		-32767;
	
	/** The hello packet which indicates the remote side is alive. */
	public static final int HELLO =
		-1;
	
	/** The client has been started (constructors are okay). */
	public static final int INITIALIZED =
		-2;
	
	/** Service with no result. */
	public static final int SERVICE_NO_RESULT =
		-3;
	
	/** Map service. */
	public static final int MAP_SERVICE =
		1;
	
	/** Return the number of services available. */
	public static final int SERVICE_COUNT =
		2;
	
	/** Service with a result. */
	public static final int SERVICE_WITH_RESULT =
		3;
	
	/** Operating System type. */
	public static final int OS_TYPE =
		4;
}

