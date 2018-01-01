// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.packets;

/**
 * This is a farm which produces {@link Packet}s using a shared byte array
 * which enables memory to be used more efficiently when there will be multiple
 * packets being created and destroyed.
 *
 * This class is thread safe.
 *
 * @since 2018/01/01
 */
public final class PacketFarm
{
	/**
	 * Creates a new variable length packet.
	 *
	 * @param __t The type of the packet, if the type is negative then no
	 * response will be used.
	 * @return The newly created packet of a variable length.
	 * @since 2018/01/01
	 */
	public final Packet create(int __t)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Creates a new fixed length packet with the specified length.
	 *
	 * @param __t The type of the packet, if the type is negative then no
	 * response will be used.
	 * @param __l The length of the packet to create.
	 * @return The newly created packet of the given length.
	 * @throws IllegalArgumentException If the length is negative.
	 * @since 2018/01/01
	 */
	public final Packet create(int __t, int __l)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AT01 Cannot have a packet with a negative
		// length.}
		if (__l < 0)
			throw new IllegalArgumentException("AT01");
		
		throw new todo.TODO();
	}
}

