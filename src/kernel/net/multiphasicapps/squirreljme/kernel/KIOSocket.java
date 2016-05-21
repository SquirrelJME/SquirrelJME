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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
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
	/** Generic socket lock. */
	protected final Object lock =
		new Object();
	
	/** The owning process. */
	protected final KernelProcess process;
	
	/** The service identifier. */
	protected final int id;
	
	/** The other socket this is bound to. */
	protected final KIOSocket bound;
	
	/** The acceptance queue for the server socket. */
	protected final Deque<KIOSocket> acceptq;
	
	/** The monitor object to be notified when an event occurs. */
	private volatile Object _monitor;
	
	/** The socket to send data to. */
	private volatile KIOSocket _sendto;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
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
		
		// Otherwise if client, add to accept queue on server side if a server
		else
		{
			// Does not use one
			this.acceptq = null;
			
			// If a server, make is so the socket can be accepted
			if (__rs.isServer())
			{
				// Get the accept queue of the remote socket
				Deque<KIOSocket> remq = __rs.acceptq;
				synchronized (remq)
				{
					// Offer it at the end
					remq.offerLast(this);
				
					// Notify the other end that a socket was accepted
					remq.notify();
				}
				
				// If a monitor is attached then alert anything waiting on
				// the monitor that a socket is waiting to be accepted.
				Object mon = this._monitor;
				if (mon != null)
					synchronized (mon)
					{
						mon.notifyAll();
					}
			}
			
			// Otherwise this is an accepted socket. Send data to the remote
			// socket from this end, and the remote socket sends data to this
			// socket.
			else
				synchronized (__rs.lock)
				{
					// Set send targets
					this._sendto = __rs;
					__rs._sendto = this;
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
		{
			// Is there a socket immedietly available?
			KIOSocket as = acceptq.pollFirst();
			
			// No socket, wait for one
			if (as == null)
			{
				// Wait for that given time
				acceptq.wait(__l);
		
				// If nothing accepted, stop
				if (null == (as = acceptq.pollFirst()))
					return null;
			}
			
			// Setup socket
			KernelProcess process = this.process;
			KIOSocket rv = new KIOSocket(process,
				process.__nextAnonymousSocketID(this), as);
			
			// Register it
			process.__registerSocket(rv);
			
			// Return it
			return rv;
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
	public int id()
	{
		return this.id;
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
	
	/**
	 * Returns the process which owns this given socket.
	 *
	 * @return The owning process for the socket.
	 * @since 2016/05/21
	 */
	public KernelProcess process()
	{
		return this.process;
	}
	
	/**
	 * Receives a datagram from the remote and returns it, if one is not
	 * available then the operation may block for a given amount of time.
	 *
	 * @param __wait The number of milliseconds to wait for a datagram to be
	 * received, {@code 0L} means to wait forever while {@code 1L} means to
	 * immedietly return if there is no available datagram.
	 * @return The datagram that was received or {@code null} if none was
	 * available.
	 * @throws InterruptedException If the thread was interrupted while waiting
	 * for a datagram.
	 * @throws KIOConnectionClosedException If the connection to the remote end
	 * has been closed.
	 * @throws KIOException If a datagram could not be recieved.
	 * @since 2016/05/21
	 */
	public KIODatagram receive(long __wait)
		throws InterruptedException, KIOConnectionClosedException, KIOException
	{
		// {@squirreljme.error AY0m Negative receive wait delay.}
		if (__wait < 0L)
			throw new IllegalArgumentException("AY0m");
		
		throw new Error("TODO");
	}
	
	/**
	 * Creates a new datagram to be sent to the remote end.
	 *
	 * @param __l The length of the datagram.
	 * @throws IllegalArgumentException If the length is negative.
	 * @throws KIOConnectionClosedException This is thrown when the connection
	 * to the remote end has been closed.
	 * @throws KIOException If the datagram could not be created.
	 * @since 2016/05/21
	 */
	public KIODatagram send(int __l)
		throws IllegalArgumentException, KIOConnectionClosedException,
			KIOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the monitor that will be notified when a socket is waiting to be
	 * accepted or if data is waiting to be read from this socket.
	 *
	 * @param __o The monitor which is given notifications for packets being
	 * received and sockets waiting to be accepted.
	 * @throws IllegalStateException If there is already an associated
	 * monitor and the given object is not that monitor.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public void setMonitor(Object __o)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error AY0k The socket already has an associated
			// monitor.}
			Object was = this._monitor;
			if (was != null && __o != was)
				throw new IllegalStateException("AY0k");
			
			// Set
			this._monitor = __o;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/21
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = _string;
		String rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = "Socket#" + this.id));
		
		// Return it
		return rv;
	}
}

