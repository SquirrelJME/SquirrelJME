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

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

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
	/** The number of characters which can be written from a string. */
	static final int _STRING_LIMIT =
		32767;
	
	/** The okay response type. */
	static final int _RESPONSE_OKAY =
		0;
	
	/** The failure response type. */
	static final int _RESPONSE_FAIL =
		-32768;
	
	/** An exception response, this is given for responseless ones. */
	static final int _RESPONSE_EXCEPTION =
		-32767;
	
	/** Ping request to the remote end, used to check transmission time. */
	static final int _PING =
		-32766;
	
	/** The grow step for the packet. */
	private static final int _GROW_SIZE =
		128;
	
	/** The mask for the grow. */
	private static final int _GROW_MASK =
		_GROW_SIZE - 1;
	
	/** Is this a global packet? */
	protected final boolean isglobalpacket;
	
	/** The owning packet farm. */
	protected final PacketFarm farm;
	
	/** The type of packet this is. */
	protected final int type;
	
	/** Is this a variable size packet? */
	protected final boolean variable;
	
	/** Is this packet in the field? */
	private volatile boolean _infield;
	
	/** Packet data. */
	private volatile byte[] _data;
	
	/** Packet offset. */
	private volatile int _offset;
	
	/** Allocation length. */
	private volatile int _allocation;
	
	/** Packet length. */
	private volatile int _length;
	
	/** Has the packet been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the packet.
	 *
	 * @param __farm The owning farm.
	 * @param __type The packet type.
	 * @param __var Is the packet variable sized?
	 * @param __b The byte array for the output packet.
	 * @param __o Offset into the array.
	 * @param __a The allocation size.
	 * @param __l The length of the packet.
	 * @param __infield If {@code true} then the packet is in the field and
	 * it may grow within it.
	 * @param __global Is this a global packet?
	 * @throws IllegalArgumentException If this is not an infield packet and
	 * the offset is non-zero.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	Packet(PacketFarm __farm, int __type, boolean __var, byte[] __b, int __o,
		int __a, int __l, boolean __infield, boolean __global)
		throws IllegalArgumentException, NullPointerException
	{
		if (__farm == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AT01 Cannot have an outside of field packet with
		// an offset which is not zero.}
		if (!__infield && __o != 0)
			throw new IllegalArgumentException("AT01");
		
		this.isglobalpacket = __global;
		this.farm = __farm;
		this.type = __type;
		this.variable = __var;
		this._data = __b;
		this._offset = __o;
		this._allocation = __a;
		this._length = __l;
		this._infield = __infield;
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
		// Only close once
		if (this._closed)
			return;
		this._closed = true;
		
		// Clear the written data in the packet for security purposes
		byte[] data = this._data;
		int offset = this._offset,
			allocation = this._allocation,
			length = this._length;
		for (int i = offset, e = offset + length; i < e; i++)
			data[i] = 0;
		
		// Tell the farm to free up this packet space
		if (this._infield)
			this.farm.__close(offset, allocation);
		
		// Clear data points to invalidate them and prevent corruption
		this._data = null;
		this._offset = Integer.MIN_VALUE;
		this._allocation = Integer.MIN_VALUE;
		this._length = Integer.MIN_VALUE;
	}
	
	/**
	 * Creates a new reader over the packet data.
	 *
	 * @return A reader over the packet data.
	 * @since 2018/01/01
	 */
	public final PacketReader createReader()
	{
		return new PacketReader(this);
	}
	
	/**
	 * Creates a new writer over the packet data.
	 *
	 * @return A writer over the packet data.
	 * @since 2018/01/01
	 */
	public final PacketWriter createWriter()
	{
		return new PacketWriter(this);
	}
	
	/**
	 * Duplicate the packet.
	 *
	 * @return The packet, but duplicated.
	 * @since 2018/01/01
	 */
	public final Packet duplicate()
	{
		// Does the same thing as this, except it just has the same type
		return this.duplicateAsType(this.type);
	}
	
	/**
	 * Duplicate the packet but as the given type instead.
	 *
	 * @param __t The new type to use.
	 * @return The duplicated packet.
	 * @since 2018/01/01
	 */
	public final Packet duplicateAsType(int __t)
	{
		boolean variable = this.variable;
		PacketFarm farm = this.farm;
		int len = this.length();
		
		// Setup new packet
		Packet rv;
		if (this.isglobalpacket)
			if (variable)
				rv = PacketFarm.createPacket(__t);
			else
				rv = PacketFarm.createPacket(__t, len);
		else
			if (variable)
				rv = farm.createPacket(__t);
			else
				rv = farm.createPacket(__t, len);
		
		rv.writePacketData(0, this);
		
		return rv;
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
		return this._length;
	}
	
	/**
	 * Reads a boolean from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/05
	 */
	public final boolean readBoolean(int __p)
		throws IndexOutOfBoundsException
	{
		return (this.readByte(__p) != 0);
	}
	
	/**
	 * Reads a byte from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/05
	 */
	public final byte readByte(int __p)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__check(__p, 1);
		return data[this._offset + __p];
	}
	
	/**
	 * Reads bytes from the packet into the given array.
	 *
	 * @param __p The position to read from.
	 * @param __b The destination array.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to read.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/06
	 */
	public final void readBytes(int __p, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		byte[] data = this.__check(__p, __l);
		for (int i = this._offset + __p, o = __o, oe = __o + __l; o < oe;)
			__b[o++] = data[i++];
	}
	
	/**
	 * Reads a double from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/05
	 */
	public final double readDouble(int __p)
	{
		return Double.longBitsToDouble(this.readLong(__p));
	}
	
	/**
	 * Reads a float from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/05
	 */
	public final float readFloat(int __p)
	{
		return Float.intBitsToFloat(this.readInteger(__p));
	}
	
	/**
	 * Reads an integer from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/04
	 */
	public final int readInteger(int __p)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__check(__p, 4);
		int baseoffset = this._offset + __p;
		
		return ((((int)data[baseoffset]) & 0xFF) << 24) |
			((((int)data[baseoffset + 1]) & 0xFF) << 16) |
			((((int)data[baseoffset + 2]) & 0xFF) << 8) |
			(((int)data[baseoffset + 3]) & 0xFF);
	}
	
	/**
	 * Reads a long from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/04
	 */
	public final long readLong(int __p)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__check(__p, 8);
		int baseoffset = this._offset + __p;
		
		return ((((int)data[baseoffset]) & 0xFF) << 56) |
			((((int)data[baseoffset + 1]) & 0xFF) << 48) |
			((((int)data[baseoffset + 2]) & 0xFF) << 40) |
			((((int)data[baseoffset + 3]) & 0xFF) << 32) |
			((((int)data[baseoffset + 4]) & 0xFF) << 24) |
			((((int)data[baseoffset + 5]) & 0xFF) << 16) |
			((((int)data[baseoffset + 6]) & 0xFF) << 8) |
			(((int)data[baseoffset + 7]) & 0xFF);
	}
	
	/**
	 * Reads data from this packet and stores it in the specified packet.
	 *
	 * @param __p The position to read from..
	 * @param __v The packet to read data into,
	 * @throws IllegalArgumentException If the packet is of variable size.
	 * @throws IndexOutOfBoundsException If the read exceeds the bounds of
	 * the packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public final void readPacketData(int __p, Packet __v)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AT02 Cannot read into a variable sized packet.}
		if (__v.variable)
			throw new IllegalArgumentException("AT02");
		
		// Forward to the array reading method
		this.readBytes(__p, __v._data, __v._offset, __v._length);
	}
	
	/**
	 * Reads a short from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/04
	 */
	public final short readShort(int __p)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__check(__p, 2);
		int baseoffset = this._offset + __p;
		
		return (short)(((((int)data[baseoffset]) & 0xFF) << 8) |
			(((int)data[baseoffset + 1]) & 0xFF));
	}
	
	/**
	 * Reads a string from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/02
	 */
	public final String readString(int __p)
		throws IndexOutOfBoundsException
	{
		// Determine if this is a long string
		int strlen = this.readUnsignedShort(__p);
		boolean longstr = ((strlen & 0x8000) != 0);
		strlen &= 0x7FFF;
		
		// Make sure we can read the full string
		byte[] data = this.__check(__p, 2 + (longstr ? (strlen * 2) : strlen));
		int baseoffset = this._offset + __p + 2;
		
		// Read long string
		char[] chars = new char[strlen];
		if (longstr)
			for (int i = 0, lochar = baseoffset, hichar = baseoffset + strlen;
				i < strlen; i++)
			{
				char v = (char)(data[lochar++] & 0xFF);
				v |= (char)((data[hichar++] & 0xFF) << 8);
				
				chars[i] = v;
			}
		
		// Read narrow string
		else
			for (int i = 0; i < strlen; i++)
				chars[i] = (char)(data[baseoffset++] & 0xFF);
		
		return new String(chars);
	}
	
	/**
	 * Reads an unsigned byte from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/05
	 */
	public final int readUnsignedByte(int __p)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__check(__p, 1);
		return data[this._offset + __p] & 0xFF;
	}
	
	/**
	 * Reads an unsigned short from the given position.
	 *
	 * @param __p The position to read from.
	 * @return The read value.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/02
	 */
	public final int readUnsignedShort(int __p)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__check(__p, 2);
		int baseoffset = this._offset + __p;
		
		return ((((int)data[baseoffset]) & 0xFF) << 8) |
			(((int)data[baseoffset + 1]) & 0xFF);
	}
	
	/**
	 * Creates a response packet with a variable length.
	 *
	 * @return The packet to use for the response.
	 * @since 2018/01/01
	 */
	public final Packet respond()
	{
		if (this.isglobalpacket)
			return PacketFarm.createPacket(0);
		return this.farm.create(0);
	}
	
	/**
	 * Creates a response packet with a fixed length.
	 *
	 * @param __l The length of the response.
	 * @return The packet to use for the response.
	 * @throws IllegalArgumentException If the length is negative.
	 * @since 2018/01/01
	 */
	public final Packet respond(int __l)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AT03 Cannot respond with a negative length
		// packet.}
		if (__l < 0)
			throw new IllegalArgumentException("AT03");
		
		if (this.isglobalpacket)
			return PacketFarm.createPacket(0, __l);
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
	 * Writes the specified boolean to the given position.
	 *
	 * @param __p The position to write at.
	 * @param __v The value to write.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @since 2018/01/05
	 */
	public final void writeBoolean(int __p, boolean __v)
		throws IndexOutOfBoundsException
	{
		this.writeByte(__p, (__v ? 1 : 0));
	}
	
	/**
	 * Writes the specified byte to the given position.
	 *
	 * @param __p The position to write at.
	 * @param __v The value to write, only the lowest 8-bits are considered.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @since 2018/01/02
	 */
	public final void writeByte(int __p, int __v)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__ensure(__p, 1);
		int baseoffset = this._offset + __p;
		data[baseoffset] = (byte)__v;
	}
	
	/**
	 * Writes bytes from the array to the packet.
	 *
	 * @param __p The position to write to.
	 * @param __b The source array.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to write.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/06
	 */
	public final void writeBytes(int __p, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IndexOutOfBoundsException,
			NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		byte[] data = this.__ensure(__p, __l);
		for (int i = this._offset + __p, o = __o, oe = __o + __l; o < oe;)
			data[i++] = __b[o++];
	}
	
	/**
	 * Writes the specified double to the given position.
	 *
	 * @param __p The position to write at.
	 * @param __v The value to write.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @since 2018/01/02
	 */
	public final void writeDouble(int __p, double __v)
	{
		this.writeLong(__p, Double.doubleToRawLongBits(__v));
	}
	
	/**
	 * Writes the specified f;pat to the given position.
	 *
	 * @param __p The position to write at.
	 * @param __v The value to write.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @since 2018/01/02
	 */
	public final void writeFloat(int __p, float __v)
	{
		this.writeInteger(__p, Float.floatToRawIntBits(__v));
	}
	
	/**
	 * Writes the specified integer to the given position.
	 *
	 * @param __p The position to write at.
	 * @param __v The value to write.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @since 2018/01/02
	 */
	public final void writeInteger(int __p, int __v)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__ensure(__p, 4);
		int baseoffset = this._offset + __p;
		data[baseoffset] = (byte)(__v >>> 24);
		data[baseoffset + 1] = (byte)(__v >>> 16);
		data[baseoffset + 2] = (byte)(__v >>> 8);
		data[baseoffset + 3] = (byte)(__v);
	}
	
	/**
	 * Writes the specified long to the given position.
	 *
	 * @param __p The position to write at.
	 * @param __v The value to write.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @since 2018/01/02
	 */
	public final void writeLong(int __p, long __v)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__ensure(__p, 8);
		int baseoffset = this._offset + __p;
		data[baseoffset] = (byte)(__v >>> 56);
		data[baseoffset + 1] = (byte)(__v >>> 48);
		data[baseoffset + 2] = (byte)(__v >>> 40);
		data[baseoffset + 3] = (byte)(__v >>> 32);
		data[baseoffset + 4] = (byte)(__v >>> 24);
		data[baseoffset + 5] = (byte)(__v >>> 16);
		data[baseoffset + 6] = (byte)(__v >>> 8);
		data[baseoffset + 7] = (byte)(__v);
	}
	
	/**
	 * Writes the data from the specified packet to this packet.
	 *
	 * @param __p The position to write at.
	 * @param __w The packet to write data from.
	 * @throws IndexOutOfBoundsException If the write exceeds the bounds of
	 * this packet.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public final void writePacketData(int __p, Packet __v)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Just forward to writing bytes
		this.writeBytes(__p, __v._data, __v._offset, __v._length);
	}
	
	/**
	 * Writes the specified short to the given position.
	 *
	 * @param __p The position to write at.
	 * @param __v The value to write, only the lowest 16-bits are considered.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @since 2018/01/02
	 */
	public final void writeShort(int __p, int __v)
		throws IndexOutOfBoundsException
	{
		byte[] data = this.__ensure(__p, 2);
		int baseoffset = this._offset + __p;
		data[baseoffset] = (byte)(__v >>> 8);
		data[baseoffset + 1] = (byte)(__v);
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
		
		this.__writeString(__p, __v);
	}
	
	/**
	 * Checks to make sure that the given number of bytes can be read from
	 * the packet.
	 *
	 * @param __p The position to read from.
	 * @param __l The number of bytes to read.
	 * @return The data array.
	 * @throws IndexOutOfBoundsException If the read exceeds the packet bounds.
	 * @since 2018/01/02
	 */
	private final byte[] __check(int __p, int __l)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error AT04 Cannot read from a negative position or
		// a negative length.}
		if (__p < 0 || __l < 0)
			throw new IndexOutOfBoundsException("AT04");
		
		// {@squirreljme.error AT05 Read exceeds the packet bounds.
		// (The position; The read length; The length of the packet;
		// The end position)}
		int endpos = __p + __l,
			plen = this._length;
		if (endpos > plen)
			throw new IndexOutOfBoundsException(
				String.format("AT05 %d %d %d %d", __p, __l, plen, endpos));
		
		return this._data;
	}
	
	/**
	 * Ensures that the number of bytes written to the specified location will
	 * fit in the packet.
	 *
	 * Variable length packets will be grown accordingly.
	 *
	 * @param __p The base position tow write to.
	 * @param __l The length to write.
	 * @return The data array to write into.
	 * @throws IllegalArgumentException If the length is negative.
	 * @throws IndexOutOfBoundsException If the write would exceed the packet
	 * bounds.
	 * @since 2018/01/02
	 */
	private final byte[] __ensure(int __p, int __l)
		throws IllegalArgumentException, IndexOutOfBoundsException
	{
		// {@squirreljme.error AT06 Cannot write at a negative position.}
		if (__p < 0)
			throw new IndexOutOfBoundsException("AT06");
		
		// {@squirreljme.error AT07 Cannot write negative length.}
		if (__l < 0)
			throw new IllegalArgumentException("AT07");
		
		int length = this._length,
			endpos = __p + __l,
			offset = this._offset,
			offsetbase = offset + __p,
			offsetlength = offset + length,
			offsetendpos = offset + endpos;
		
		// Write is within bounds
		byte[] data = this._data;
		if (endpos <= length)
			return data;
			
		// Variable length packet
		if (this.variable)
		{
			// Still fits within the allocation
			int allocation = this._allocation;
			if (endpos <= allocation)
			{
				// Zero old data
				while (offsetlength < offsetendpos)
					data[offsetlength++] = 0;
				
				this._length = endpos;
				return data;
			}
			
			// If the packet is in the field, just create a new packet then
			if (this._infield)
			{
				int newalloc = ((endpos + _GROW_SIZE) & (~_GROW_MASK));
				byte[] newdata = new byte[newalloc];
				
				// Copy old data over
				for (int p = offset, o = 0; p < offsetlength; p++, o++)
					newdata[o] = data[p];
				
				// Store new information
				this._data = newdata;
				this._offset = 0;
				this._allocation = newalloc;
				this._length = endpos;
				this._infield = false;
				
				// Remember to free the packet so it can be used elsewhere
				this.farm.__close(offset, allocation);
				
				return newdata;
			}
			
			// Otherwise just setup a new byte array
			else
			{
				// Make a new copy of the data
				int newalloc = ((endpos + _GROW_SIZE) & (~_GROW_MASK));
				byte[] newdata = Arrays.copyOf(data, newalloc);
				
				// Set new properties
				this._data = newdata;
				this._offset = 0;
				this._allocation = newalloc;
				this._length = endpos;
				
				// Zero old data
				while (offsetlength < offsetendpos)
					newdata[offsetlength++] = 0;
				
				return newdata;
			}
		}
		
		// Fixed length packet
		else
		{
			// {@squirreljme.error AT08 Write exceeds the packet bounds.}
			if (endpos > length)
				throw new IndexOutOfBoundsException("AT08");
			return data;
		}
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
		
		// {@squirreljme.error AT09 Direct input read packet length mismatch.}
		if (this.length() != __len)
			throw new IllegalArgumentException("AT09");
		
		__in.readFully(this._data, this._offset, this._length);
	}
	
	/**
	 * Writes the specified string to the output, returning the number of
	 * written bytes.
	 *
	 * Strings are limited to 32,767 characters.
	 *
	 * If a string contains a character outside of the bounds of 8-bit
	 * then the string will consume double the space and be encoded in
	 * char form, otherwise it will use byte form.
	 *
	 * @param __p The position to write at.
	 * @param __v The value to write.
	 * @return The number of bytes written.
	 * @throws IndexOutOfBoundsException If the write exceeds the packet
	 * bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	final int __writeString(int __p, String __v)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AT0a The string length is too long to write to
		// the packet.}
		int strlen = __v.length();
		if (strlen > Packet._STRING_LIMIT)
			throw new IndexOutOfBoundsException("AT0a");
		
		// Determine if this is a long string
		boolean longstr = false;
		for (int i = 0; i < strlen; i++)
			if (__v.charAt(i) > 0xFF)
			{
				longstr = true;
				break;
			}
		
		// Determine the actual length of the data to write
		int writelen = 2 + (longstr ? (strlen * 2) : strlen);
		
		// Ensure we can write this data
		byte[] data = this.__ensure(__p, writelen);
		int baseoffset = this._offset + __p;
		
		// Record the string length
		data[baseoffset] = (byte)((strlen >>> 8) | (longstr ? 0x80 : 0x00));
		data[baseoffset + 1] = (byte)(strlen);
		
		// Write long string
		int charoffset = baseoffset + 2;
		if (longstr)
			for (int i = 0, lowchar = charoffset, hichar = charoffset + strlen;
				i < strlen; i++)
			{
				char c = __v.charAt(i);
				
				data[hichar++] = (byte)(c >>> 8);
				data[lowchar++] = (byte)c;
			}
		
		// Write narrow string
		else
			for (int i = 0; i < strlen; i++)
				data[charoffset++] = (byte)__v.charAt(i);
		
		return writelen;
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
		
		int length = this._length;
		__out.writeInt(length);
		__out.write(this._data, this._offset, length);
	}
}

