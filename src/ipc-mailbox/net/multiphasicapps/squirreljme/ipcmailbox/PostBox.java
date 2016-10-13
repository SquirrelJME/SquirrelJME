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

