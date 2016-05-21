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

import java.io.Closeable;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;

/**
 * This is a socket which establishes an endpoint for sending and receiving
 * datagrams. Each socket is associated with a service number.
 * Sockets may optionally be connected to remote endpoints as a default
 * destination.
 *
 * A process may only have a single socket for a given service at any one time.
 *
 * Internally negative service values are anonymous services which can only
 * send datagrams and receive responses from the other side.
 *
 * @since 2016/05/20
 */
public final class KIOSocket
	implements Closeable
{
	/** The owning process. */
	protected final KernelProcess process;
	
	/** The service identifier. */
	protected final int id;
	
	/** The other socket this is bound to. */
	protected final KIOSocket bound;
	
	/** The acceptance queue for the server socket. */
	protected final Deque<KIOSocket> acceptq;
	
	/** The accepted socket that is referenced on the server side. */
	private volatile KIOSocket _svbacksock;
	
	/**
	 * Initializes a socket.
	 *
	 * @param __kp The process which owns this socket.
	 * @param __id The service identifier.
	 * @param __rs The remote socket to bind to, if {@code null} then this is
	 * a server socket, otherwise a socket to the other endpoint is performed.
	 * @throws IllegalArgumentException If the service identifier is zero.
	 * @throws NullPointerException On null arguments, except for {@code __rs}.
	 * @since 2016/05/21
	 */
	KIOSocket(KernelProcess __kp, int __id, KIOSocket __rs)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__kp == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY0f Illegal service identifier. (The service
		// identifier.}
		if (__id == 0 || (__rs == null && __id <= 0) ||
			(__rs != null && __id > 0))
			throw new IllegalArgumentException(String.format("AY0f %d", __id));
		
		// Set
		this.process = __kp;
		this.id = __id;
		this.bound = __rs;
		
		// If server, setup acceptance queue
		if (__rs == null)
			this.acceptq = new LinkedList<>();
		
		// Otherwise if client, add to accept queue on server side
		else
		{
			// Does not use one
			this.acceptq = null;
			
			// Get the accept queue of the remote socket
			Deque<KIOSocket> remq = __rs.acceptq;
			synchronized (remq)
			{
				// Offer it at the end
				remq.offerLast(this);
				
				// Notify the other end that a socket was accepted
				remq.notify();
				
				System.err.printf("DEBUG -- Socket connect %s (%s -> %s).%n",
					remq, this, __rs);
			}
		}
	}
	
	/**
	 * Accepts a given socket from a client, blocking for the given amount of
	 * milliseconds until one is found or the thread is interrupted or the
	 * timeout is reached.
	 *
	 * @param __l The number of milliseconds to wait for a client connection
	 * to accept for. A timeout of {@code 0L} means to wait forever.
	 * @return A socket connected to a given client or {@code null} if there
	 * is socket (the timeout was reached). A timeout of {@code 1L} means to
	 * not wait for a socket to enter the queue.
	 * @throws KIOException If a client connection could not be accepted
	 * properly.
	 * @throws IllegalStateException If this is not a server socket.
	 * @throws InterruptedException If during the wait for an accepted client
	 * socket, the time expired.
	 * @since 2016/05/21
	 */
	public KIOSocket accept(long __l)
		throws KIOException, IllegalStateException, InterruptedException
	{
		// {@squirreljme.error AY0j Only server sockets may use accept.}
		if (!isServer())
			throw new IllegalStateException("AY0j");
		
		// Lock on the local queue
		Deque<KIOSocket> acceptq = this.acceptq;
		synchronized (acceptq)
		{System.err.printf("DEBUG -- Socket will accept %s %s%n", this,
			acceptq);
			// Is there a socket immedietly available?
			KIOSocket as = acceptq.pollFirst();
			System.err.printf("DEBUG -- Quick acccept %s%n", as);
			
			// No socket, wait for one
			if (as == null)
			{
				long start = System.nanoTime() / 1_000_000_000L;
				for (;;)
				{
					// Get remaining time to wait for
					long rem = (__l == 0L ? 10L : __l -
						((System.nanoTime() / 1_000_000_000L) - start));
					System.err.printf("DEBUG -- Socket wait %d %s (%s)%n", rem,
						acceptq, this);
				
					// No more time left? Poll immedietly
					if (rem <= 0)
					{
						// If nothing was read, stop
						if (null == (as = acceptq.pollFirst()))
							return null;
					}
				
					// Otherwise wait for the given amount of time to pass
					else
					{
						// Wait
						acceptq.wait(rem);
					
						// If got an accept request, run with it
						if (null != (as = acceptq.pollFirst()))
							break;
					}
				}
			}
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public void close()
		throws KIOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the socket's service identifier number.
	 *
	 * @return The server identifier number of the socket.
	 * @since 2016/05/21
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * Returns the process which owns this given socket.
	 *
	 * @return The owning process for the socket.
	 * @since 2016/05/21
	 */
	public KernelProcess getProcess()
	{
		return this.process;
	}
	
	/**
	 * Returns {@code true} if this is an anonymous socket.
	 *
	 * @return {@code true} if an anonymous socket.
	 * @since 2016/05/20
	 */
	public boolean isAnonymous()
	{
		return this.id < 0;
	}
	
	/**
	 * Is a server (listening) socket?
	 *
	 * @return {@code true} if this is a server socket.
	 * @since 2016/05/21
	 */
	public boolean isServer()
	{
		return null == this.bound;
	}
}

