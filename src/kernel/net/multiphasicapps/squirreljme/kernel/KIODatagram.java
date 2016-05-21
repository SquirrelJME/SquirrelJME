// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This represents a single unit of communication that is sent and received
 * over a {@link KIOSocket}.
 *
 * Once a datagram is sent, it cannot be modified or read from by the sender.
 *
 * Datagrams may also send arrays to act as a kind of shared memory along with
 * their normal packet data if needed.
 *
 * @since 2016/05/20
 */
public final class KIODatagram
{
	/** The source socket. */
	protected final KIOSocket src;
	
	/** The destination socket. */
	protected final KIOSocket dest;
	
	/**
	 * Initializes the datagram.
	 *
	 * @param __from The source socket.
	 * @param __to The destination socket.
	 * @param __l The length of the datagram.
	 * @throws IllegalArgumentException If the length is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	KIODatagram(KIOSocket __from, KIOSocket __to, int __l)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY0l The datagram length is negative.}
		if (__l < 0)
			throw new IllegalArgumentException("AY0l");
		
		// Set
		this.src = __from;
		this.dest = __to;
		
		throw new Error("TODO");
	}
	
	/**
	 * Indicates that this datagram is ready to be read by the receiving end.
	 *
	 * @return {@code this}.
	 * @since 2016/05/21
	 */
	final KIODatagram __ready()
	{
		if (true)
			throw new Error("TODO");
		
		return this;
	}
}

