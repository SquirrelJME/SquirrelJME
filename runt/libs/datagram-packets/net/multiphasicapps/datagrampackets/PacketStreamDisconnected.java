// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.datagrampackets;

/**
 * This is thrown when the packet stream has been disconnected.
 *
 * @since 2018/01/01
 */
public class PacketStreamDisconnected
	extends RuntimeException
{
	/**
	 * Initializes exception with the given message.
	 *
	 * @param __m The message.
	 * @since 2018/01/01
	 */
	public PacketStreamDisconnected(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/01/01
	 */
	public PacketStreamDisconnected(String __m, Throwable __c)
	{
		super(__m, __c);
	}
}

