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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
	/** The okay response type. */
	static final int _RESPONSE_OKAY =
		0;
	
	/** The failure response type. */
	static final int _RESPONSE_FAIL =
		Integer.MIN_VALUE;
	
	/** The owning packet farm. */
	protected final PacketFarm farm;
	
	/** The type of packet this is. */
	protected final int type;
	
	/**
	 * Initializes the packet.
	 *
	 * @param __farm The owning farm.
	 * @param __type The packet type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	Packet(PacketFarm __farm, int __type)
		throws NullPointerException
	{
		if (__farm == null)
			throw new NullPointerException("NARG");
		
		this.farm = __farm;
		this.type = __type;
		
		throw new todo.TODO();
	}
	
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
	 * Duplicate the packet.
	 *
	 * @return The packet, but duplicated.
	 * @since 2018/01/01
	 */
	public final Packet duplicate()
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
		return this.type > 0;
	}
	
	/**
	 * Returns the current length of the packet.
	 *
	 * @return The current packet length.
	 * @since 2018/01/01
	 */
	public final int length()
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
		return this.farm.create(0);
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
		
		return this.farm.create(0, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/01
	 */
	@Override
	public final String toString()
	{
		return String.format("{Packet: type=%d, length=%d}",
			this.type, this.length());
	}
	
	/**
	 * Returns the packet type.
	 *
	 * @return The packet type.
	 * @since 2018/01/01
	 */
	public final int type()
	{
		return this.type;
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
	
	/**
	 * Reads the packet data from the input stream.
	 *
	 * @param __in The stream to read from.
	 * @param __len The length to read.
	 * @throws IllegalArgumentException If the read length does not match
	 * the packet length.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	final void __readFromInput(DataInputStream __in, int __len)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AT04 Direct input read packet length mismatch.}
		if (this.length() != __len)
			throw new IllegalArgumentException("AT04");
		
		throw new todo.TODO();
	}
	
	/**
	 * Writes the packet data to the given output stream.
	 *
	 * @param __out The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	final void __writeToOutput(DataOutputStream __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

