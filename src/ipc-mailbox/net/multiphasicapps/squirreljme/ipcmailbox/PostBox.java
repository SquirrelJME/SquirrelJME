// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ipcmailbox;

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
{
	/** The owning post office. */
	protected final PostOffice postoffice;
	
	/** The incoming queue. */
	private final Deque<__Datagram__> _queue =
		new ArrayDeque<>();
	
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
		if (__chan.length < 1 || __o < 0 || __l < 0 ||
			(__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// Lock on the queue
		Deque<__Datagram__> iq = this._queue;
		synchronized (iq)
		{
			throw new Error("TODO");
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
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	public void send(int __chan, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		// Create datagram
		__Datagram__ d = new __Datagram__(__chan, __b, __o, __l);
		
		// Get the other box and its queue
		PostBox other = this.postoffice.__otherBox(this);
		Deque<__Datagram__> oq = other._queue;
		synchronized (oq)
		{
			// Add to the end of the queue
			oq.offerLast(d);
			
			// Notify a thread that a message arrived
			oq.notify();
		}
	}
}

