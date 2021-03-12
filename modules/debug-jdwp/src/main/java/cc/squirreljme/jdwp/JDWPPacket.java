// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
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
	
	/** The queue where packets will go when done. */
	private final Deque<JDWPPacket> _queue;
	
	/** The ID of this packet. */
	private volatile int _id;
	
	/** The flags for this packet. */
	private volatile int _flags;
	
	/** The command set (if not a reply). */
	private volatile int _commandSet =
		-1;
	
	/* The command (if not a reply). */
	private volatile int _command =
		-1;
	
	/** The error code (if a reply). */
	private volatile int _errorCode;
	
	/** The packet data. */
	private volatile byte[] _data;
	
	/** The length of the data. */
	private volatile int _length;
	
	/** The read position. */
	private volatile int _readPos;
	
	/**
	 * Initializes the packet with the queue it will go back into whenever
	 * it is done being used.
	 * 
	 * @param __queue The queue for packets.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/03/10
	 */
	public JDWPPacket(Deque<JDWPPacket> __queue)
		throws NullPointerException
	{
		if (__queue == null)
			throw new NullPointerException("NARG");
		
		this._queue = __queue;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/10
	 */
	@Override
	public void close()
		throws JDWPException
	{
		throw Debugging.todo();
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
			return (this._flags & JDWPPacket.FLAG_REPLY) != 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final String toString()
	{
		int flags = this._flags;
		return String.format("JDWDPacket[id=%08x,flags=%02x,len=%d]:%s",
			this._id, flags, this._length,
			((flags & JDWPPacket.FLAG_REPLY) != 0 ?
				String.format("[error=%d]", this._errorCode) :
				String.format("[cmdSet=%d;cmd=%d]",
					this._commandSet, this._command)));
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
				this._errorCode = ((__header[9] & 0xFF) << 8) |
					(__header[10] & 0xFF);
			}
			
			// Non-reply
			else
			{
				// These are not used
				this._errorCode = -1;
				
				// Read the command used
				this._commandSet = __header[9] & 0xFF;
				this._command = __header[10] & 0xFF;
			}
		}
	}
}
