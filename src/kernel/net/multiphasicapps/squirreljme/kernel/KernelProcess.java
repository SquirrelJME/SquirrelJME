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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This represents a process within the kernel. {@link Thread}s are associated
 * with processes and are generally used for access.
 *
 * SquirrelJME can run without a memory manager and one is not needed at all
 * (due to lack of reflection) and as such all processes if checks were not
 * made could access other processes freely. However to prevent this, a
 * permission manager is associated with each process and checks are placed in
 * the specific locations to allow or deny specific behaviors of the code.
 *
 * @since 2016/05/16
 */
public final class KernelProcess
{
	/**
	 * This is an array used by connectSocket to pass to the multi-connect
	 * version without allocating.
	 */
	static final KernelProcess[] _NO_KERNEL_PROCESSES =
		new KernelProcess[0];
	
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** Is this the kernel process? */
	protected final boolean iskernel;
	
	/** The permission manager. */
	protected final KernelAccessManager access;
	
	/** The ID of this process. */
	protected final int id;
	
	/** The kernel tracer. */
	final __KernelTraceHolder__ _tracer;
	
	/** Threads the process own. */
	private final List<Thread> _threads =
		new LinkedList<>();
	
	/** The process IPC sockets. */
	private final List<KIOSocket> _sockets =
		new LinkedList<>();
	
	/** Is the current process dead? */
	private volatile boolean _dead;
	
	/** The next anonymous socket to use. */
	private volatile int _nextanon =
		-1;
	
	/** String representation of this process. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the kernel process.
	 *
	 * @param __k The owning kernel.
	 * @param __ik Is this the kernel process.
	 * @throws NullPointerException
	 * @since 2016/05/16
	 */
	KernelProcess(Kernel __k, boolean __ik, int __id)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		this._tracer = __k._tracer;
		this.iskernel = __ik;
		this.id = __id;
		
		// Setup access
		this.access = new KernelAccessManager(this);
	}
	
	/**
	 * Returns the permission manager.
	 *
	 * @return The permission manager.
	 * @since 2016/05/16
	 */
	public final KernelAccessManager accessManager()
	{
		return this.access;
	}
	
	/**
	 * Returns {@code true} if any threads in the current process are
	 * alive.
	 *
	 * @return {@code true} if any threads are currently alive.
	 * @since 2016/05/16
	 */
	public final boolean areAnyThreadsAlive()
	{
		// Lock
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// If dead, this will never return true
			if (this._dead)
				return false;
			
			// Go through all threads
			Iterator<Thread> it = threads.iterator();
			while (it.hasNext())
			{
				Thread t = it.next();
				
				// If not alive, remove it
				if (!t.isAlive())
					it.remove();
			}
			
			// Only if there are threads, is the process considered alive.
			boolean isnotdead = !threads.isEmpty();
			
			// Mark as dead if dead
			if (!isnotdead)
				this._dead = true;
			
			// Return not dead state
			return isnotdead;
		}
	}
	
	/**
	 * Creates a socket connection to the given process and service ID.
	 *
	 * @param __id The service identifier to connect to.
	 * @param __kp The process which hosts the service to connect to.
	 * @throws IllegalArgumentException If the service ID is zero or negative.
	 * @throws KIOException If the socket could not be opened.
	 * @throws SecurityException If the current process cannot create a socket
	 * for the given process.
	 * @since 2016/05/21
	 */
	public final KIOSocket connectSocket(int __id, KernelProcess __kp)
		throws IllegalArgumentException, KIOException, SecurityException
	{	
		// Check
		if (__kp == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY09 Cannot connect to the given service ID
		// of another process because it is not valid. (The service ID)}
		if (__id <= 0)
			throw new IllegalArgumentException(String.format("AY09 %d", __id));
		
		// Lock
		List<KIOSocket> sockets = this._sockets;
		synchronized (sockets)
		{
			// Is dead
			__checkDead();
			
			// Make sure connecting is permitted
			this.access.connectSocket(__kp, __id);
			
			// Get socket to connect/bind to
			KIOSocket rsocks = __kp.__getServiceSocket(__id);
			
			// Determine the next service ID to use
			int next = __nextAnonymousSocketID(null);
			
			// Create client socket
			KIOSocket rv = new KIOSocket(this, next, rsocks);
			
			// Add to socket list
			sockets.add(rv);
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns {@code true} if the process contains the given thread.
	 *
	 * @param __t The thread to see if this processes manages it.
	 * @return {@code true} if this proccess owns the given thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/16
	 */
	public final boolean containsThread(Thread __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// Go through all threads and potentially cleanup while searching
			// for the given thread
			Iterator<Thread> it = threads.iterator();
			while (it.hasNext())
			{
				Thread t = it.next();
				
				// Remove dead threads
				if (!t.isAlive())
					it.remove();
				
				// Is this a thread?
				else if (t == __t)
					return true;
			}
		}
		
		// Does not contain it
		return false;
	}
	
	
	/**
	 * Creates a new anonymous socket which is used to connect and send data
	 * to sockets which are listening.
	 *
	 * @throws KIOException If the socket could not be created.
	 * @throws SecurityException If it is not permitted to create a new socket.
	 * @since 2016/05/20
	 */
	public final KIOSocket createSocket()
		throws KIOException, NullPointerException, SecurityException
	{
		return createSocket(0);
	}
	
	/**
	 * Creates a new socket for this process
	 *
	 * @param __sv The service number, must be a positive non-zero integer.
	 * @throws IllegalArgumentException If the service number is negative or
	 * zero.
	 * @throws KIOException If the socket could not be created because the
	 * service identifier is already in use.
	 * @throws SecurityException If it is not permitted to create a new socket.
	 * @since 2016/05/20
	 */
	public final KIOSocket createSocket(int __id)
		throws IllegalArgumentException, KIOException, SecurityException
	{
		// {@squirreljme.error AY03 The service number for a socket cannot be
		// zero or negative.}
		if (__id <= 0)
			throw new IllegalArgumentException("AY03");
		
		// Lock
		List<KIOSocket> sockets = this._sockets;
		synchronized (sockets)
		{
			// Is dead
			__checkDead();
			
			// Check permission
			this.access.createSocket();
			
			// {@squirreljme.error AY0c The current process is already hosting
			// a service with the given identifier. (The service identifier)}
			Iterator<KIOSocket> it = sockets.iterator();
			while (it.hasNext())
				if (__id == it.next().id())
					throw new KIOException(String.format("AY0c %d", __id));
			
			// Could fail
			try
			{
				// Create the socket
				KIOSocket rv = new KIOSocket(this, __id, null);
			
				// Add it to the socket list
				sockets.add(rv);
			
				// Return it
				return rv;
			}
			
			// {@squirreljme.error AY0e Could not create a socket because not
			// enough memory was available.}
			catch (OutOfMemoryError e)
			{
				throw new KIOException("AY0e", e);
			}
		}
	}
	
	/**
	 * Creates a new thread with the specified code to run which is then
	 * assigned to the current process.
	 *
	 * @param __r The code to run when the thread is started.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If thread creation is denied.
	 * @since 2016/05/16
	 */
	public final Thread createThread(Runnable __r)
		throws NullPointerException, SecurityException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// Is dead
			__checkDead();
			
			// Check permission
			this.access.createThread();
		
			// Create thread
			Thread rv = new Thread(__r);
		
			// Add to thread list
			threads.add(rv);
			
			// Start it
			rv.start();
		
			// Return it
			return rv;
		}
	}
	
	/**
	 * Checks if the current process hosts the given service.
	 *
	 * @param __id The service identifier to check.
	 * @return {@code true} if the current process hosts a server for the
	 * given service.
	 * @throws IllegalArgumentException If the service identifier is zero or
	 * negative.
	 * @since 2016/05/21
	 */
	public final boolean hostsService(int __id)
		throws IllegalArgumentException
	{
		return null != __getServiceSocket(__id);
	}
	
	/**
	 * Returns the ID number of this process.
	 *
	 * @return The process ID number.
	 * @since 2016/05/21
	 */
	public final int id()
	{
		return this.id;
	}
	
	/**
	 * Returns {@code true} if this is the kernel process.
	 *
	 * @return {@code true} if this is the kernel process, otherwise it is
	 * a user-space process.
	 * @since 2016/05/16
	 */
	public final boolean isKernelProcess()
	{
		return this.iskernel;
	}
	
	/**
	 * Returns the kernel that this runs under.
	 *
	 * @return The kernel this runs under.
	 * @since 2016/05/16
	 */
	public final Kernel kernel()
	{
		return this.kernel;
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
			_string = new WeakReference<>((rv = "Process#" + this.id));
		
		// Return it
		return rv;
	}
	
	/**
	 * Adds a thread to the current process.
	 *
	 * This method may be prone to dead-locks and as such should only be used
	 * at the very start of the kernel to assign threads to a given process.
	 *
	 * @param __t The thread to add.
	 * @throws IllegalStateException If another process contains this thread.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the current process cannot add a thread to
	 * this process.
	 * @since 2016/05/16
	 */
	final void __addThread(Thread __t)
		throws IllegalStateException, NullPointerException, SecurityException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY07 The thread to be added is owned by
		// another process.}
		KernelProcess op = this.kernel.processByThread(__t);
		if (op != null && op != this)
			throw new IllegalStateException("AY07");
		
		// Lock
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// Check permission
			if (!iskernel)
				this.access.createThread();
			
			// Add it
			threads.add(__t);
		}
	}
	
	/**
	 * Checks if the current process is dead.
	 *
	 * @throws IllegalStateException If it is dead.
	 * @since 2016/05/21
	 */
	final void __checkDead()
		throws IllegalStateException
	{
		// Lock on threads
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// {@squirreljme.error AY0b The requested operation is not valid
			// because the given process is dead.}
			if (this._dead)
				throw new IllegalStateException("AY0b");
		}
	}
	
	/**
	 * Returns the socket which is associated with the given service.
	 *
	 * @param __id The service identifier to check.
	 * @return The socket which acts as the server for the given process or
	 * {@code null} if no socket is associated with the given service.
	 * @throws IllegalArgumentException If the service identifier is zero or
	 * negative.
	 * @since 2016/05/21
	 */
	final KIOSocket __getServiceSocket(int __id)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AY0g The service identifier is zero or
		// negative. (The service identifier)}
		if (__id <= 0)
			throw new IllegalArgumentException(String.format("AY0g %d", __id));
		
		// Lock
		List<KIOSocket> sockets = this._sockets;
		synchronized (sockets)
		{
			// Go through all sockets
			Iterator<KIOSocket> it = sockets.iterator();
			while (it.hasNext())
			{
				KIOSocket sock = it.next();
				
				// Is this service?
				if (sock.id() == __id)
					return sock;
			}
		}
		
		// Not a service being hosted
		return null;
	}
	
	/**
	 * Determines the identifer to use for the next anonymous socket.
	 *
	 * @param __clb This is non-{@code null} if an ID is being requested on
	 * the server end on behalf of a server socket.
	 * @return The socket identifer to use for the given socket.
	 * @throws KIOException If no identifiers remain.
	 * @since 2016/05/21
	 */
	final int __nextAnonymousSocketID(KIOSocket __clb)
		throws KIOException
	{
		// Lock
		List<KIOSocket> sockets = this._sockets;
		synchronized (sockets)
		{
			// Obtain the next anonymous service ID to use for the given socket
			// If there have been more than 2 billion sockets created, then
			// a free socket identifier must be found.
			int next = _nextanon - 1;
			if (next >= 0)
			{
				// Need to determine a socket number that is not used at all.
				int nes = sockets.size();
				int[] used = new int[nes];
				Iterator<KIOSocket> it = sockets.iterator();
				int at = 0;
				while (it.hasNext())
					used[at++] = it.next().id();
			
				// Sort the given array so values are in order
				Arrays.sort(used);
			
				// Go through the array to find a free ID number
				int usenum = 0;
				int alloc = Integer.MIN_VALUE;
				for (int i = 0; i < nes; i++)
				{
					// Current identifier
					int now = used[i];
				
					// The remaining sockets are identified services (servers)
					// and as such the search stops, since if this point is
					// reached then that means that there are 2 billion
					// open sockets.
					if (now >= 0)
						break;
				
					// If the allocation point is below the current socket
					// then the given identifier is available
					if (alloc < now)
					{
						usenum = alloc;
						break;
					}
				
					// Otherwise increase the allocation to the current id
					alloc = now;
				}
			
				// {@squirreljme.error AY0i No more anonymous sockets are
				// available for the current process.}
				if (usenum == 0)
					throw new KIOException("AY0i");
			
				// Set
				next = usenum;
			}
		
			// Otherwise use that value instead next time
			else
				_nextanon = next;
		
			// Return it
			return next;
		}
	}
	
	/**
	 * Registers a socket and places it into the socket list for the current
	 * process, this is used for accepted sockets (which are anonymous and
	 * are created by the server socket code).
	 *
	 * @param __sock The socket to register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	final void __registerSocket(KIOSocket __sock)
		throws NullPointerException
	{
		// Check
		if (__sock == null)
			throw new NullPointerException("NARG");
		
		// Lock
		List<KIOSocket> sockets = this._sockets;
		synchronized (sockets)
		{
			// Add it
			sockets.add(__sock);
		}
	}
}

