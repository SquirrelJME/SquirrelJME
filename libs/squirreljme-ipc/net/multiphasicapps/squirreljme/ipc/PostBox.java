// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ipc;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * This is a mailbox which is part of a post office (think PO Box), a mailbox
 * keeps a queue of letters which may be read off as a queue.
 *
 * @since 2016/10/13
 */
public final class PostBox
	implements Closeable
{
	/** The owning post office. */
	protected final PostOffice postoffice;
	
	/** The incoming queue. */
	private final Deque<__Datagram__> _queue =
		new ArrayDeque<>();
	
	/** Was this closed? */
	private volatile boolean _closed;
	
	/** Was the end reached? */
	private volatile boolean _eof;
	
	/**
	 * Initializes the mailbox.
	 *
	 * @param __po The owning post office.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	PostBox(PostOffice __po)
		throws NullPointerException
	{
		// Check
		if (__po == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.postoffice = __po;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void close()
	{
		// Close only once
		if (this._closed)
			return;
		this._closed = true;
		
		// Lock on the queue
		Deque<__Datagram__> q = this._queue;
		synchronized (q)
		{
			// Notify all of closed
			q.notifyAll();
		}
		
		// Closing this end also closes the other end
		this.postoffice.__otherBox(this).close();
	}
	
	/**
	 * Returns {@code true} if there are messages in the queue.
	 *
	 * @return {@code true} if there are datagrams waiting.
	 * @since 2016/10/13
	 */
	public boolean hasRemaining()
	{
		// Lock on the queue
		Deque<__Datagram__> q = this._queue;
		synchronized (q)
		{
			return !q.isEmpty();
		}
	}
	
	/**
	 * Receives a single datagram from the queue.
	 *
	 * @param __chan An array with a length of least zero, used as output to
	 * specify the channel the data was sent on.
	 * @param __b The output array where data is to be written.
	 * @param __o The starting offset to the output.
	 * @param __l The maximum number of bytes to read.
	 * @param __w If {@code true} then the operation will block until a
	 * datagram is read or the thread is interrupted.
	 * @return The number of bytes read, or a negative value if the end of
	 * the stream was reached. A value of zero means that a datagram with no
	 * actual data was sent by the remote side.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the length of the array; or the channel array has
	 * a zero length.
	 * @throws ArrayStoreException If there is not enough data in the output
	 * array to store the datagram data, {@code __chan} will contain the
	 * required storage length.
	 * @throws InterruptedException If an interrupt occured waiting for data.
	 * @throws NoSuchElementException If not waiting and there are no datagrams
	 * available.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	public int receive(int[] __chan, byte[] __b, int __o, int __l, boolean __w)
		throws ArrayIndexOutOfBoundsException, ArrayStoreException,
			IllegalArgumentException, InterruptedException,
			NoSuchElementException, NullPointerException
	{
		// Check
		if (__chan == null || __b == null)
			throw new NullPointerException("NARG");
		int end = __o + __l;
		if (__chan.length < 1 || __o < 0 || __l < 0 || end > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// Lock on the queue
		Deque<__Datagram__> iq = this._queue;
		synchronized (iq)
		{
			// Loop for awhile
			for (;;)
			{
				// If the end was reached, stop
				if (this._eof)
					return -1;	
				
				// See if there is data waiting
				__Datagram__ d = iq.peekFirst();
				if (d == null)
				{
					// Closed, no more datagrams will be received
					if (this._closed)
					{
						this._eof = true;
						return -1;
					}
					
					// Not closed, wait for more
					else if (__w)
					{
						// Wait until something was stored in the queue
						iq.wait();
						
						// Try again
						continue;
					}
					
					// {@squirreljme.error BW01 No datagram is available.}
					else
						throw new NoSuchElementException("BW01");
				}
				
				// Get details
				byte[] data = d._data;
				
				// {@squirreljme.error BW02 Cannot read the datagram
				// because the input buffer is too small.}
				int len = data.length;
				if (len > __l)
				{
					__chan[0] = len;
					throw new ArrayStoreException("BW02");
				}
				
				// Copy data
				for (int i = __o, b = 0; b < len; i++, b++)
					__b[i] = data[b];
				
				// Set channel
				__chan[0] = d._channel;
				
				// Return length
				return len;
			}
		}
	}
	
	/**
	 * Sends the data from this postbox to the other side's queue.
	 *
	 * @param __chan The channel to send on.
	 * @param __b The data to send.
	 * @param __o The offset to the start of the data.
	 * @param __l The number of bytes to send.
	 * @throws ArrayIndexOutOfBoundsException If the array is not within
	 * bounds.
	 * @throws IOException If the remote end is closed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	public void send(int __chan, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Create datagram
		__Datagram__ d = new __Datagram__(__chan, __b, __o, __l);
		
		// Get the other box and its queue
		PostBox other = this.postoffice.__otherBox(this);
		Deque<__Datagram__> oq = other._queue;
		synchronized (oq)
		{
			// {@squirreljme.error BW03 The remote post box has closed.}
			if (other._closed)
				throw new IOException("BW03");
			
			// Add to the end of the queue
			oq.offerLast(d);
			
			// Notify a thread that a message arrived
			oq.notify();
		}
	}
}

