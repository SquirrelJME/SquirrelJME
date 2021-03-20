// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	
	/** String type. */
	private static final String _STRING = 
		"Ljava/lang/String;";
	
	/** Thread type. */
	private static final String _THREAD =
		"Ljava/lang/Thread;";
	
	/** Class type. */
	private static final String _CLASS =
		"Ljava/lang/Class;";
	
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
	
	/** The error code (if a reply). */
	volatile ErrorType _errorCode;
	
	/** The packet data. */
	private volatile byte[] _data;
	
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
	protected int command()
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
			
			// {@squirreljme.error AG0d End of packet reached. 
			// (The packet size)}
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
	 * @since 2021/03/13
	 */
	public final int readId()
		throws JDWPException
	{
		return this.readInt();
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
				// {@squirreljme.error AG0f UTF-8 not supported?}
				throw new JDWPException("AG0f", __e);
			}
		}
	}
	
	/**
	 * Resets and opens the packet.
	 * 
	 * @param __open Should this be opened?
	 * @since 2021/03/12
	 */
	protected final void resetAndOpen(boolean __open)
	{
		synchronized (this)
		{
			// {@squirreljme.error AG05 Cannot reset an open packet.}
			if (this._open)
				throw new JDWPException("AG05");
			
			this._id = 0;
			this._flags = 0;
			this._commandSet = -1;
			this._command = -1;
			this._errorCode = null;
			this._length = 0;
			this._readPos = 0;
			
			// Mark as open?
			this._open = __open;
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
			
			int flags = this._flags;
			return String.format("JDWPPacket[id=%08x,flags=%02x,len=%d]:%s",
				this._id, flags, this._length,
				((flags & JDWPPacket.FLAG_REPLY) != 0 ?
					String.format("[error=%s]", this._errorCode) :
					String.format("[cmdSet=%s;cmd=%s]",
						(commandSet == null ||
							commandSet == JDWPCommandSet.UNKNOWN ?
							this._commandSet : commandSet),
						(command == null ? this._command : command))));
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
	 * @since 2021/03/12
	 */
	public void writeId(JDWPId __v)
		throws JDWPException
	{
		this.writeInt((__v == null ? 0 : __v.debuggerId()));
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
			
			// {@squirreljme.error AG0e UTF-8 is not supported?}
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
	protected void writeTo(DataOutputStream __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Must be an open packet
			this.__checkOpen();
			
			// Write shared header (includes header size)
			__out.writeInt(this._length + CommLink._HEADER_SIZE);
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
			__out.write(this._data, 0, this._length);
		}
	}
	
	/**
	 * Writes a value to the output.
	 * 
	 * @param __val The value to write.
	 * @param __context Context value which may adjust how the value is
	 * written, this may be {@code null}.
	 * @param __untag Untagged value?
	 * @throws JDWPException If it failed to write.
	 * @since 2021/03/15
	 */
	public void writeValue(Object __val, String __context, boolean __untag)
		throws JDWPException
	{
		// We really meant to write a value here
		if (__val instanceof JDWPValue)
		{
			this.writeValue(((JDWPValue)__val).get(), __context, __untag);
			return;
		}
		
		synchronized (this)
		{
			// Must be an open packet
			this.__checkOpen();
			
			// Character
			if (__val instanceof Character)
			{
				if (!__untag)
					this.writeByte('C');
				this.writeShort((char)__val);
			}
			
			// Integer or boxed lower types
			else if (__val instanceof Integer || __val instanceof Short ||
				__val instanceof Byte)
			{
				Number num = (Number)__val;
				
				// Should be boolean
				if ("Z".equals(__context))
				{
					if (!__untag)
						this.writeByte('Z');
					this.writeByte((num.intValue() != 0 ? 1 : 0));
				}
				
				// Should be short
				else if ("S".equals(__context))
				{
					if (!__untag)
						this.writeByte('S');
					this.writeShort(num.intValue());
				}
				
				// Should be character
				else if ("C".equals(__context))
				{
					if (!__untag)
						this.writeByte('C');
					this.writeShort(num.intValue());
				}
				
				// Plain integer
				else
				{
					if (!__untag)
						this.writeByte('I');
					this.writeInt(num.intValue());
				}
			}
			
			// Long
			else if (__val instanceof Long)
			{
				if (!__untag)
					this.writeByte('J');
				this.writeLong((long)__val);
			}
			
			// Float
			else if (__val instanceof Float)
			{
				if (!__untag)
					this.writeByte('F');
				this.writeInt(Float.floatToRawIntBits((float)__val));
			}
			
			// Double
			else if (__val instanceof Double)
			{
				if (!__untag)
					this.writeByte('D');
				this.writeLong(Double.doubleToRawLongBits((double)__val));
			}
			
			// IDAble
			else if ((__val instanceof JDWPId) || __val == null)
			{
				JDWPId id = (JDWPId)__val;
				
				// Array
				if (id instanceof JDWPArray)
					this.writeByte('[');
				
				// Thread
				else if (id instanceof JDWPThread)
					this.writeByte('t');
				
				// Thread group
				else if (id instanceof JDWPThreadGroup)
					this.writeByte('g');
				
				// Class
				else if (id instanceof JDWPClass)
					this.writeByte('c');
				
				// A kind of reference with a class
				else if (id instanceof JDWPReferenceType)
				{
					JDWPReferenceType ref = (JDWPReferenceType)id;
					JDWPClass classy = ref.debuggerClass();
					
					// String type
					if (classy != null && JDWPPacket._STRING.equals(
						classy.debuggerFieldDescriptor()))
						this.writeByte('s');
					
					// Thread type
					else if (classy != null && JDWPPacket._THREAD.equals(
						classy.debuggerFieldDescriptor()))
						this.writeByte('t');
					
					// Class type
					else if (classy != null && JDWPPacket._CLASS.equals(
						classy.debuggerFieldDescriptor()))
						this.writeByte('c');
					
					// Object otherwise
					else
						this.writeByte('L');
				}
				
				// Treat as object otherwise
				else
					this.writeByte('L');
				
				// Write ID
				this.writeId(id);
			}
			
			// Unknown, treat as void?
			else
				this.writeVoid();
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
		// {@squirreljme.error AG0b Packet not open.}
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
		// {@squirreljme.error AG0c Packet type mismatched. (Requested reply?)}
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
			// {@squirreljme.error AG0a Packet is already open.}
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
			this._flags = (flags = __header[8]);
			
			// Reply type
			if ((flags & JDWPPacket.FLAG_REPLY) != 0)
			{
				// These are not used
				this._commandSet = -1;
				this._command = -1;
				
				// Read just the error code
				this._errorCode = ErrorType.of(((__header[9] & 0xFF) << 8) |
					(__header[10] & 0xFF));
			}
			
			// Non-reply
			else
			{
				// These are not used
				this._errorCode = null;
				
				// Read the command used
				this._commandSet = __header[9] & 0xFF;
				this._command = __header[10] & 0xFF;
			}
			
			// Becomes open now
			this._open = true;
		}
	}
}
