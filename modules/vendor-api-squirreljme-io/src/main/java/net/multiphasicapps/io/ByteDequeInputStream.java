// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

/**
 * An input stream which reads from the front of a byte deque, locking is
 * done on the queue itself.
 *
 * @see ByteDequeOutputStream
 * @since 2024/01/19
 */
public class ByteDequeInputStream
	extends InputStream
{
	/** The byte deque to access. */
	protected final ByteDeque queue;
	
	/**
	 * Initializes the byte deque stream.
	 *
	 * @param __queue The queue to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	public ByteDequeInputStream(ByteDeque __queue)
		throws NullPointerException
	{
		if (__queue == null)
			throw new NullPointerException("NARG");
		
		this.queue = __queue;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public int available()
		throws IOException
	{
		ByteDeque queue = this.queue;
		synchronized (queue)
		{
			return queue.available();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void close()
		throws IOException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public int read()
		throws IOException
	{
		ByteDeque queue = this.queue;
		synchronized (queue)
		{
			for (;;)
			{
				// Are there any bytes available?
				int available = queue.available();
				
				// If there are no bytes available, wait for them to come
				if (available <= 0)
				{
					// Wait for data
					ByteDequeInputStream.__wait(queue);
					
					// Try again otherwise
					continue;
				}
				
				// Remove the first byte from the queue
				return (queue.removeFirst() & 0xFF);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public int read(byte[] __b)
		throws IOException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return this.read(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IOException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		int bufLen = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bufLen ||
			(__o + __l) < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		ByteDeque queue = this.queue;
		synchronized (queue)
		{
			for (;;)
			{
				// Are there any bytes available?
				int available = queue.available();
				
				// If there are no bytes available, wait for them to come
				if (available <= 0)
				{
					// Wait for a bit to receive a signal that there is data
					// in the pipe
					ByteDequeInputStream.__wait(queue);
					
					// Try again otherwise
					continue;
				}
				
				// Determine how much we can read at once
				int limit = (available < __l ? available : __l);
				
				// Read in the bytes
				return queue.removeFirst(__b, __o, limit);
			}
		}
	}
	
	/**
	 * Waits for data within the queue.
	 *
	 * @param __queue The queue to wait on.
	 * @throws InterruptedIOException If interrupted.
	 * @since 2024/01/19
	 */
	@SuppressWarnings("WaitNotifyWhileNotSynced")
	private static void __wait(ByteDeque __queue)
		throws InterruptedIOException
	{
		// Wait for a bit to receive a signal that there is data
		// in the pipe
		try
		{
			__queue.wait(100);
		}
		catch (InterruptedException __e)
		{
			InterruptedIOException toss =
				new InterruptedIOException("INTR");
			toss.bytesTransferred = 0;
			throw toss;
		}
	}
}
