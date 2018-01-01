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

import java.io.Closeable;

/**
 * This class represents a single packet which may be sent through the
 * interface, it contains a type and data associated with it.
 *
 * The {@link #close()} method is thread safe when used with a
 * {@link PacketFarm}.
 *
 * @since 2018/01/01
 */
public final class Packet
	implements Closeable
{
	/**
	 * Closes this packet and frees the byte array it uses so that it can be
	 * re-used by the packet farm.
	 *
	 * @since 2018/01/01
	 */
	@Override
	public final void close()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Creates a new reader over the packet data.
	 *
	 * @return A reader over the packet data.
	 * @since 2018/01/01
	 */
	public final PacketReader createReader()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Creates a new writer over the packet data.
	 *
	 * @return A writer over the packet data.
	 * @since 2018/01/01
	 */
	public final PacketWriter createWriter()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns {@code true} if the packet generates a response.
	 *
	 * @return {@code true} if the packet generates a response.
	 * @since 2018/01/01
	 */
	public final boolean hasResponse()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Creates a response packet with a variable length.
	 *
	 * @return The packet to use for the response.
	 * @since 2018/01/01
	 */
	public final Packet respond()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Creates a response packet with a fixed length.
	 *
	 * @param __l
	 * @return The packet to use for the response.
	 * @throws IllegalArgumentException If the length is negative.
	 * @since 2018/01/01
	 */
	public final Packet respond(int __l)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AT02 Cannot respond with a negative length
		// packet.}
		if (__l < 0)
			throw new IllegalArgumentException("AT02");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/01
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the packet type.
	 *
	 * @return The packet type.
	 * @since 2018/01/01
	 */
	public final int type()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Writes a string at the specified position.
	 *
	 * @param __p The position to write at.
	 * @param __v The value to write.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public final void writeString(int __p, String __v)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

