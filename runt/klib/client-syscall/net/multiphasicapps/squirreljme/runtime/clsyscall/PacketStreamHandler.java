// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.clsyscall;

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
	 * @param __t The type of request made.
	 * @param __b The data contained in the response.
	 * @param __o The offset into the array.
	 * @param __l The length of the data.
	 * @return The response data, may be {@code null} if there is no data
	 * to be sent and it may be blank.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public abstract byte[] handle(int __t, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException;
}

