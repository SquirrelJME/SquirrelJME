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
	private volatile int _commandSet;
	
	/* The command (if not a reply). */
	private volatile int _command;
	
	/** The error code (if a reply). */
	private volatile int _errorCode;
	
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
		throws IOException
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
		throw Debugging.todo();
	}
}
