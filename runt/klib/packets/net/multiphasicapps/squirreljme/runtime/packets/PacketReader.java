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
 * This class is used to read from packets in a stream like manner.
 *
 * @since 2018/01/01
 */
public final class PacketReader
{
	/** The packet. */
	protected final Packet packet;
	
	/** The position. */
	private volatile int _position;
	
	/**
	 * Initializes a packet reader.
	 *
	 * @param __p The packet to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	public PacketReader(Packet __p)
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
	 * Reads a string from the given packet.
	 *
	 * @return The read string.
	 * @since 2018/01/01
	 */
	public final String readString()
	{
		Packet packet = this.packet;
		int position = this._position;
		
		// It can be determined how many bytes to skip based on the string
		// length.
		int strlen = packet.readUnsignedShort(position);
		
		// Is this a long string?
		boolean longstr = ((strlen & 0x8000) != 0);
		strlen &= 0x7FFF;
		
		// Read in string data
		String rv = packet.readString(position);
		
		// Skip the string data bytes
		this._position = position + 2 + (longstr ? (strlen * 2) : strlen);
		return rv;
	}
}

