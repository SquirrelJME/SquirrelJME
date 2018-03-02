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
 * This interface is used by the incoming side to handle incoming requests
 * which were sent by the remote end.
 *
 * @since 2018/01/01
 */
public interface PacketStreamHandler
{
	/**
	 * This is called when the remote side has ended the connection.
	 *
	 * @since 2018/01/01
	 */
	public abstract void end();
	
	/**
	 * Handles a request sent from the remote end.
	 *
	 * @param __p The packet received from the remote end.
	 * @return The packet to respond with, this may be {@code null} if there
	 * is no response. The return value is ignored if the type is not one
	 * which generates a response.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public abstract Packet handle(Packet __p)
		throws NullPointerException;
}

