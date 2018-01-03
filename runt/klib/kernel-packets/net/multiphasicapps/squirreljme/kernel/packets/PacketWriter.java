// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.packets;

/**
 * This class is used to write to a packet in a stream like manner.
 *
 * @since 2018/01/01
 */
public final class PacketWriter
{
	/** The packet. */
	protected final Packet packet;
	
	/** The position. */
	private volatile int _position;
	
	/**
	 * Initializes a packet writer.
	 *
	 * @param __p The packet to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	public PacketWriter(Packet __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.packet = __p;
	}
	
	/**
	 * Returns the current position of the head.
	 *
	 * @return The current head position.
	 * @since 2018/01/02
	 */
	public final int position()
	{
		return this._position;
	}
	
	/**
	 * Writes the specified string to the packet.
	 *
	 * @param __v The value to write.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public final void writeString(String __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		int position = this._position;
		position += this.packet.__writeString(position, __v);
		this._position = position;
	}
}

