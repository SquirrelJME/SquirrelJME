// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Deque;

/**
 * Represents a packet for JDWP.
 * 
 * This class is mutable and as such must be thread safe.
 *
 * @since 2021/03/10
 */
public final class JDWPPacket
	implements Closeable
{
	/** Flag used for replies. */
	public static final short FLAG_REPLY =
		0x80;
	
	/** Grow size. */
	private static final byte _GROW_SIZE =
		32;
	
	/** The queue where packets will go when done. */
	private final Reference<Deque<JDWPPacket>> _queue;
	
	/** The ID of this packet. */
	volatile int _id;
	
	/** The flags for this packet. */
	volatile int _flags;
	
	/** The command set (if not a reply). */
	volatile int _commandSet =
		-1;
	
	/* The command (if not a reply). */
	volatile int _command =
		-1;
	
	/** The raw error code. */
	volatile int _rawErrorCode;
	
	/** The error code (if a reply). */
	volatile JDWPErrorType _errorCode;
	
	/** The packet data. */
	private volatile byte[] _data;
	
	/** Identifier sizes. */
	private volatile JDWPIdSizes _idSizes;
	
	/** The length of the data. */
	private volatile int _length;
	
	/** The read position. */
	private volatile int _readPos;
	
	/** Is this packet open? */
	private volatile boolean _open;
	
	/**
	 * Initializes the packet with the queue it will go back into whenever
	 * it is done being used.
	 * 
	 * @param __queue The queue for packets.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/10
	 */
	JDWPPacket(Deque<JDWPPacket> __queue)
		throws NullPointerException
	{
		if (__queue == null)
			throw new NullPointerException("NARG");
		
		this._queue = new WeakReference<>(__queue);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/10
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	@Override
	public void close()
		throws JDWPException
	{
		synchronized (this)
		{
			// Ignore if closed already (prevent double queue add)
			if (!this._open)
				return;
			
			// Set to closed
			this._open = false;
			
			// Return to the queue
			Deque<JDWPPacket> queue = this._queue.get();
			if (queue != null)
				synchronized (queue)
				{
					queue.add(this);
				}
		}
	}
	
	/**
	 * Returns the command Id.
	 * 
	 * @return The command Id.
	 * @since 2021/03/13
	 */
	public int command()
	{
		synchronized (this)
		{
			// Ensure it is valid
			this.__checkOpen();
			this.__checkType(false);
			
			return this._command;
		}
	}
	
	/**
	 * Returns the command set for the packet.
	 * 
	 * @return The command set.
	 * @since 2021/03/12
	 */
	public JDWPCommandSet commandSet()
	{
		synchronized (this)
		{
			// Ensure it is valid
			this.__checkOpen();
			this.__checkType(false);
			
			// Map the ID
			return JDWPCommandSet.of(this._commandSet);
		}
	}
	
	/**
	 * Returns the raw command set id.
	 *
	 * @return The command set id.
	 * @since 2024/01/19
	 */
	public int commandSetId()
	{
		synchronized (this)
		{
			// Ensure it is valid
			this.__checkOpen();
			this.__checkType(false);
			
			// Return the raw command set
			return this._commandSet;
		}
	}
	
	/**
	 * Returns a copy of the given packet.
	 * 
	 * @param __packet The packet to copy from.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/30
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public JDWPPacket copyOf(JDWPPacket __packet)
		throws NullPointerException
	{
		if (__packet == null)
			throw new NullPointerException("NARG");
		
		// Read from other packet to prevent potential deadlock
		int id;
		int flags;
		int commandSet;
		int command;
		JDWPErrorType errorCode;
		byte[] data;
		int length;
		int readPos;
		synchronized (__packet)
		{
			id = __packet._id;
			flags = __packet._flags;
			commandSet = __packet._commandSet;
			command = __packet._command;
			errorCode = __packet._errorCode;
			data = __packet._data;
			length = __packet._length;
			readPos = __packet._readPos;
		}
		
		// Set packet details
		synchronized (this)
		{
			this._id = id;
			this._flags = flags;
			this._commandSet = commandSet;
			this._command = command;
			this._errorCode = errorCode;
			this._length = length;
			this._readPos = readPos;
			
			// Data might not even be valid here
			if (data != null)
			{
				// Can we get away with only copying part of the array?
				byte[] ourData = this._data;
				if (ourData != null && ourData.length >= data.length)
					System.arraycopy(data, 0,
						ourData, 0, data.length);
				
				// Larger size, which will require array re-allocation
				else
					this._data = data.clone();
			}
		}
		
		return this;
	}
	
	/**
	 * Returns the error for this packet.
	 *
	 * @return The packet's error, if there is one.
	 * @since 2024/01/22
	 */
	public JDWPErrorType error()
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			return this._errorCode;
		}
	}
	
	/**
	 * Does this packet have any error?
	 *
	 * @return If this has an error.
	 * @since 2024/01/21
	 */
	public boolean hasError()
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			return this._errorCode != JDWPErrorType.NO_ERROR;
		}
	}
	
	/**
	 * Does this packet have the given error? This should be called when there
	 * are other possible error states, but we only want to match against a
	 * specific case.
	 *
	 * @param __error The error to check against.
	 * @return If this has an error, and it is set to {@code __error}.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public boolean hasError(JDWPErrorType __error)
		throws NullPointerException
	{
		if (__error == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			return this._errorCode == __error;
		}
	}
	
	/**
	 * Returns the packet ID.
	 * 
	 * @return The packet it.
	 * @since 2021/03/12
	 */
	public int id()
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			return this._id;
		}
	}
	
	/**
	 * Returns the current ID sizes in use.
	 *
	 * @return The used ID sizes.
	 * @since 2024/01/22
	 */
	public JDWPIdSizes idSizes()
	{
		synchronized (this)
		{
			this.__checkOpen();
			
			return this._idSizes;
		}
	}
	
	/**
	 * Is this a reply packet?
	 * 
	 * @return If this is a reply.
	 * @since 2021/03/11
	 */
	public boolean isReply()
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			return (this._flags & JDWPPacket.FLAG_REPLY) != 0;
		}
	}
	
	/**
	 * Returns the length of this packet.
	 *
	 * @return The packet length.
	 * @since 2024/01/19
	 */
	public int length()
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			return this._length;
		}
	}
	
	/**
	 * Reads a single boolean from the packet
	 * 
	 * @return The single read value.
	 * @throws JDWPException If the end of the packet was reached.
	 * @since 2021/04/17
	 */
	public boolean readBoolean()
		throws JDWPException
	{
		return this.readByte() != 0;
	}
	
	/**
	 * Reads a single byte from the packet
	 * 
	 * @return The single read value.
	 * @throws JDWPException If the end of the packet was reached.
	 * @since 2021/03/13
	 */
	public final byte readByte()
		throws JDWPException
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			/* {@squirreljme.error AG0d End of packet reached. 
			(The packet size)} */
			int readPos = this._readPos;
			if (readPos >= this._length)
				throw new JDWPException("AG0d " + readPos);
			
			// Read in and increment the position
			byte rv = this._data[readPos];
			this._readPos = readPos + 1;
			return rv;
		}
	}
	
	/**
	 * Reads an identifier from the packet.
	 * 
	 * @return The single read value.
	 * @throws JDWPException If the end of the packet was reached.
	 * @deprecated Do not use.
	 * @since 2021/03/13
	 */
	@Deprecated
	public final int readId()
		throws JDWPException
	{
		return this.readInt();
	}
	
	/**
	 * Reads an ID of the given kind.
	 *
	 * @param __kind The kind of ID to read.
	 * @return The read ID.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public final JDWPId readId(JDWPIdKind __kind)
		throws NullPointerException
	{
		if (__kind == null)
			throw new NullPointerException("NARG");
		
		return JDWPId.of(__kind, this.readId());
	}
	
	/**
	 * Reads an integer from the packet
	 * 
	 * @return The single read value.
	 * @throws JDWPException If the end of the packet was reached.
	 * @since 2021/03/13
	 */
	public final int readInt()
		throws JDWPException
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			// Read in each byte
			return ((this.readByte() & 0xFF) << 24) |
				((this.readByte() & 0xFF) << 16) |
				((this.readByte() & 0xFF) << 8) |
				(this.readByte() & 0xFF);
		}
	}
	
	/**
	 * Reads aa long from the packet
	 * 
	 * @return The single read value.
	 * @throws JDWPException If the end of the packet was reached.
	 * @since 2021/03/17
	 */
	public long readLong()
		throws JDWPException
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			// Read in each byte
			return ((this.readByte() & 0xFFL) << 56) |
				((this.readByte() & 0xFFL) << 48) |
				((this.readByte() & 0xFFL) << 40) |
				((this.readByte() & 0xFFL) << 32) |
				((this.readByte() & 0xFFL) << 24) |
				((this.readByte() & 0xFF) << 16) |
				((this.readByte() & 0xFF) << 8) |
				(this.readByte() & 0xFF);
		}
	}
	
	/**
	 * Reads the specified string.
	 * 
	 * @return The read string.
	 * @throws JDWPException If it could not be read.
	 * @since 2021/03/13
	 */
	public final String readString()
		throws JDWPException
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			// Read length
			int len = this.readInt();
			
			// Read in UTF data
			byte[] utf = new byte[len];
			for (int i = 0; i < len; i++)
				utf[i] = this.readByte();
			
			// Build final string
			try
			{
				return new String(utf, "utf-8");
			}
			catch (UnsupportedEncodingException __e)
			{
				/* {@squirreljme.error AG0f UTF-8 not supported?} */
				throw new JDWPException("AG0f", __e);
			}
		}
	}
	
	/**
	 * Reads a variable width value from the packet
	 * 
	 * @param __width The width of the value.
	 * @return The single read value.
	 * @throws IllegalArgumentException If the width is zero or negative.
	 * @throws JDWPException If the end of the packet was reached.
	 * @since 2021/03/13
	 */
	public final long readVariable(int __width)
		throws IllegalArgumentException, JDWPException
	{
		if (__width <= 0)
			throw new IllegalArgumentException("NEGV");
		
		long result = 0;
		
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			// Read in each byte
			for (int i = 0; i < __width; i++)
			{
				// Shift up and read in
				result <<= 8;
				result |= (this.readByte() & 0xFF);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the byte array of the packet.
	 *
	 * @return The packet data as a byte array.
	 * @since 2024/01/22
	 */
	public byte[] toByteArray()
	{
		synchronized (this)
		{
			// Ensure this is open
			this.__checkOpen();
			
			// If there is no data, then return a blank array
			byte[] data = this._data;
			if (data == null)
				return new byte[0];
			
			// Otherwise make a copy of it
			int len = this._length;
			byte[] result = new byte[len];
			System.arraycopy(data, 0,
				result, 0, len);
			
			// Use the result
			return result;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final String toString()
	{
		synchronized (this)
		{
			if (!this._open)
				return "JDWPPacket:Closed";
			
			// Find the command set
			JDWPCommandSet commandSet = JDWPCommandSet.of(this._commandSet);
			JDWPCommand command = (commandSet == null ? null :
				commandSet.command(this._command));
			
			// Put in the actual packet data
			int length = this._length;
			byte[] data = this._data;
			StringBuilder sb = new StringBuilder(length * 2);
			for (int i = 0; i < length; i++)
			{
				byte b = data[i];
				
				sb.append(Character
					.forDigit(((b & 0xF0) >>> 4) & 0xF, 16));
				sb.append(Character.forDigit(b & 0xF, 16));
			}
			
			int flags = this._flags;
			return String.format("JDWPPacket[id=%08x,flags=%02x,len=%d]:%s:%s",
				this._id, flags, length,
				((flags & JDWPPacket.FLAG_REPLY) != 0 ?
					(this._errorCode == JDWPErrorType.NO_ERROR ? "" :
						String.format("[error=%s(%d)]",
							this._errorCode, this._rawErrorCode)) :
					String.format("[cmdSet=%s;cmd=%s]",
						(commandSet == null ||
							commandSet == JDWPCommandSet.UNKNOWN ?
							this._commandSet : commandSet),
						(command == null ? this._command : command))),
				sb);
		}
	}
	
	/**
	 * Writes to the given packet.
	 * 
	 * @param __b The buffer.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws JDWPException If this could not be written.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/21
	 */
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, JDWPException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		synchronized (this)
		{
			// Must be an open packet
			this.__checkOpen();
			
			for (int i = 0; i < __l; i++)
				this.writeByte(__b[__o + i]);
		}
	}
	
	/**
	 * Writes the boolean to the output.
	 * 
	 * @param __b The boolean to write.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/03/13
	 */
	public void writeBoolean(boolean __b)
		throws JDWPException
	{
		this.writeByte((__b ? 1 : 0));
	}
	
	/**
	 * Writes the given byte.
	 * 
	 * @param __v The value to write.
	 * @since 2021/03/12
	 */
	public void writeByte(int __v)
	{
		synchronized (this)
		{
			// Must be an open packet
			this.__checkOpen();
			
			// Where this will be going
			int length = this._length;
			byte[] data = this._data;
			
			// Too small?
			if (data == null || length + 1 > data.length)
				this._data = (data = (data == null ?
					new byte[JDWPPacket._GROW_SIZE] : Arrays.copyOf(
					data, data.length + JDWPPacket._GROW_SIZE)));
			
			// Write the byte
			data[length] = (byte)__v;
			this._length = length + 1;
		}
	}
	
	/**
	 * Writes an ID to the output.
	 * 
	 * @param __v The value to write.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/04/10
	 */
	public void writeId(int __v)
		throws JDWPException
	{
		this.writeInt(__v);
	}
	
	/**
	 * Writes an integer to the output.
	 * 
	 * @param __v The value to write.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/03/12
	 */
	public void writeInt(int __v)
		throws JDWPException
	{
		synchronized (this)
		{
			// Must be an open packet
			this.__checkOpen();
			
			// Write the data
			this.writeByte(__v >> 24);
			this.writeByte(__v >> 16);
			this.writeByte(__v >> 8);
			this.writeByte(__v);
		}
	}
	
	/**
	 * Writes a long to the output.
	 * 
	 * @param __v The value to write.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/03/13
	 */
	public void writeLong(long __v)
		throws JDWPException
	{
		synchronized (this)
		{
			// Must be an open packet
			this.__checkOpen();
			
			// Write the data
			this.writeByte((byte)(__v >> 56));
			this.writeByte((byte)(__v >> 48));
			this.writeByte((byte)(__v >> 40));
			this.writeByte((byte)(__v >> 32));
			this.writeByte((byte)(__v >> 24));
			this.writeByte((byte)(__v >> 16));
			this.writeByte((byte)(__v >> 8));
			this.writeByte((byte)(__v));
		}
	}
	
	/**
	 * Writes a short to the output.
	 * 
	 * @param __v The value to write.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/03/19
	 */
	public void writeShort(int __v)
		throws JDWPException
	{
		synchronized (this)
		{
			// Must be an open packet
			this.__checkOpen();
			
			// Write the data
			this.writeByte(__v >> 8);
			this.writeByte(__v);
		}
	}
	
	/**
	 * Writes the string to the output.
	 * 
	 * @param __string The string to write.
	 * @throws JDWPException If it could not be written.
	 * @since 2021/03/13
	 */
	public void writeString(String __string)
		throws JDWPException
	{
		synchronized (this)
		{
			byte[] bytes;
			try
			{
				bytes = __string.getBytes("utf-8");
			}
			
			/* {@squirreljme.error AG0e UTF-8 is not supported?} */
			catch (UnsupportedEncodingException __e)
			{
				throw new JDWPException("AG0e", __e);
			}
			
			// Write length
			this.writeInt(bytes.length);
			
			// Write all the bytes
			for (byte b : bytes)
				this.writeByte(b);
		}
	}
	
	/**
	 * Writes to the output.
	 * 
	 * @param __out The output.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/12
	 */
	public void writeTo(DataOutputStream __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Must be an open packet
			this.__checkOpen();
			
			// Write shared header (includes header size)
			__out.writeInt(this._length + JDWPCommLink._HEADER_SIZE);
			__out.writeInt(this._id);
			__out.writeByte(this._flags);
			
			// Reply packet
			if (this.isReply())
				__out.writeShort(this._errorCode.id);
			
			// Command packet
			else
			{
				__out.writeByte(this._commandSet);
				__out.writeByte(this._command);
			}
			
			// Write output data
			if (this._length > 0)
				__out.write(this._data, 0, this._length);
		}
	}
	
	/**
	 * Writes a void type to the output.
	 * 
	 * @throws JDWPException If it failed to write.
	 * @since 2021/03/19
	 */
	public void writeVoid()
		throws JDWPException
	{
		synchronized (this)
		{
			// Must be an open packet
			this.__checkOpen();
			
			this.writeByte('V');
		}
	}
	
	/**
	 * Checks if the packet is open.
	 * 
	 * @throws IllegalStateException If it is not open.
	 * @since 2021/03/12
	 */
	void __checkOpen()
		throws IllegalStateException
	{
		/* {@squirreljme.error AG0b Packet not open.} */
		if (!this._open)
			throw new IllegalStateException("AG0b");
	}
	
	/**
	 * Checks if this is a reply packet or not.
	 * 
	 * @param __isReply If this should be reply.
	 * @throws IllegalStateException If it is not a reply packet.
	 * @since 2021/03/12
	 */
	void __checkType(boolean __isReply)
		throws IllegalStateException
	{
		/* {@squirreljme.error AG0c Packet type mismatched. (Requested reply?)} */
		if (__isReply != this.isReply())
			throw new IllegalStateException("AG0c " + __isReply);
	}
	
	/**
	 * Loads the packet data within.
	 * 
	 * @param __header The header.
	 * @param __data The packet data.
	 * @param __dataLen The data length.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/10
	 */
	void __load(byte[] __header, byte[] __data, int __dataLen)
		throws NullPointerException
	{
		synchronized (this)
		{
			/* {@squirreljme.error AG0a Packet is already open.} */
			if (this._open)
				throw new IllegalStateException("AG0a");
			
			// Grow (or allocate) to fit the data
			byte[] data = this._data;
			if (data == null || data.length < __dataLen)
				this._data = (data = new byte[__dataLen]);
			
			// Copy it in quickly
			System.arraycopy(__data, 0,
				data, 0, __dataLen);
			
			// Common header bits
			this._length = __dataLen;
			this._id = ((__header[4] & 0xFF) << 24) |
				((__header[5] & 0xFF) << 16) |
				((__header[6] & 0xFF) << 8) |
				(__header[7] & 0xFF);
			int flags;
			this._flags = ((flags = __header[8]) & 0xFF);
			
			// Reply type
			if ((flags & JDWPPacket.FLAG_REPLY) != 0)
			{
				// These are not used
				this._commandSet = -1;
				this._command = -1;
				
				// Read just the error code
				int rawErrorCode = ((__header[9] & 0xFF) << 8) |
					(__header[10] & 0xFF);
				this._rawErrorCode = rawErrorCode;
				this._errorCode = JDWPErrorType.of(rawErrorCode);
			}
			
			// Non-reply
			else
			{
				// These are not used
				this._rawErrorCode = 0;
				this._errorCode = null;
				
				// Read the command used
				this._commandSet = __header[9] & 0xFF;
				this._command = __header[10] & 0xFF;
			}
			
			// Becomes open now
			this._open = true;
		}
	}
	
	/**
	 * Resets and opens the packet.
	 *
	 * @param __open Should this be opened?
	 * @param __idSizes ID sizes.
	 * @since 2021/03/12
	 */
	final void __resetAndOpen(boolean __open, JDWPIdSizes __idSizes)
	{
		synchronized (this)
		{
			/* {@squirreljme.error AG05 Cannot reset an open packet.} */
			if (this._open)
				throw new JDWPException("AG05");
			
			this._id = 0;
			this._flags = 0;
			this._commandSet = -1;
			this._command = -1;
			this._errorCode = null;
			this._rawErrorCode = -1;
			this._length = 0;
			this._readPos = 0;
			this._idSizes = __idSizes;
			
			// Mark as open?
			this._open = __open;
		}
	}
}
