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
	 * Writes the specified boolean to the packet.
	 *
	 * @param __v The value to write.
	 * @since 2018/01/05
	 */
	public final void writeBoolean(boolean __v)
	{
		this.writeByte((__v ? 1 : 0));
	}
	
	/**
	 * Writes the specified byte to the packet.
	 *
	 * @param __v The value to write.
	 * @since 2018/01/05
	 */
	public final void writeByte(int __v)
	{
		int position = this._position;
		this.packet.writeByte(position, __v);
		this._position = position + 1;
	}
	
	/**
	 * Writes the specified double to the packet.
	 *
	 * @param __v The value to write.
	 * @since 2018/01/05
	 */
	public final void writeDouble(double __v)
	{
		this.writeLong(Double.doubleToRawLongBits(__v));
	}
	
	/**
	 * Writes the specified float to the packet.
	 *
	 * @param __v The value to write.
	 * @since 2018/01/05
	 */
	public final void writeFloat(float __v)
	{
		this.writeInteger(Float.floatToRawIntBits(__v));
	}
	
	/**
	 * Writes the specified int to the packet.
	 *
	 * @param __v The value to write.
	 * @since 2018/01/05
	 */
	public final void writeInteger(int __v)
	{
		int position = this._position;
		this.packet.writeInteger(position, __v);
		this._position = position + 4;
	}
	
	/**
	 * Writes the specified long to the packet.
	 *
	 * @param __v The value to write.
	 * @since 2018/01/05
	 */
	public final void writeLong(long __v)
	{
		int position = this._position;
		this.packet.writeLong(position, __v);
		this._position = position + 8;
	}
	
	/**
	 * Writes the specified short to the packet.
	 *
	 * @param __v The value to write.
	 * @since 2018/01/05
	 */
	public final void writeShort(int __v)
	{
		int position = this._position;
		this.packet.writeShort(position, __v);
		this._position = position + 2;
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

