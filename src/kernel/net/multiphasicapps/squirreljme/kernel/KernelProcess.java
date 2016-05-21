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

import java.util.ArrayList;
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
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** Is this the kernel process? */
	protected final boolean iskernel;
	
	/** The permission manager. */
	protected final KernelAccessManager access;
	
	/** Threads the process own. */
	private final List<Thread> _threads =
		new LinkedList<>();
	
	/** The process IPC sockets. */
	private final List<KIOSocket> _sockets =
		new LinkedList<>();
	
	/**
	 * Initializes the kernel process.
	 *
	 * @param __k The owning kernel.
	 * @param __ik Is this the kernel process.
	 * @throws NullPointerException
	 * @since 2016/05/16
	 */
	KernelProcess(Kernel __k, boolean __ik)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		this.iskernel = __ik;
		
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
	 * Adds a thread to the current process.
	 *
	 * @param __t The thread to add.
	 * @throws IllegalStateException If another process contains this thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/16
	 */
	public final void addThread(Thread __t)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Check permission
		if (!iskernel)
			this.access.createThread();
		
		// {@squirreljme.error AY07 The thread to be added is owned by
		// another process.}
		KernelProcess op = this.kernel.processByThread(__t);
		if (op != null && op != this)
			throw new IllegalStateException("AY07");
		
		// Lock
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// Add it
			threads.add(__t);
		}
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
			return !threads.isEmpty();
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
	 * @param __sv The service number.
	 * @throws IllegalArgumentException If the service number is negative.
	 * @throws KIOException If the socket could not be created.
	 * @throws SecurityException If it is not permitted to create a new socket.
	 * @since 2016/05/20
	 */
	public final KIOSocket createSocket(int __id)
		throws IllegalArgumentException, KIOException, SecurityException
	{
		// {@squirreljme.error AY03 The service number for a socket cannot be
		// negative.}
		if (__id < 0)
			throw new IllegalArgumentException("AY03");
		
		// Check permission
		this.access.createSocket();
		
		throw new Error("TODO");
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
		
		// Check permission
		this.access.createThread();
		
		// Create thread
		Thread rv = new Thread(__r);
		
		// Lock
		List<Thread> threads = this._threads;
		synchronized (threads)
		{
			// Add to thread list
			threads.add(rv);
			
			// Start it
			rv.start();
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Creates a socket connection to the given process and service ID.
	 *
	 * @param __kp The process which hosts the service to connect to.
	 * @param __id The service identifier to connect to.
	 * @throws IllegalArgumentException If the service ID is zero or negative.
	 * @throws KIOException If the socket could not be opened.
	 * @throws SecurityException If the current process cannot create a socket
	 * for the given process.
	 * @since 2016/05/21
	 */
	public final KIOSocket connectSocket(KernelProcess __kp, int __id)
		throws IllegalArgumentException, KIOException, SecurityException
	{	
		// {@squirreljme.error AY09 Cannot connect to the given service ID
		// of another process because it is not valid. (The service ID)}
		if (__id <= 0)
			throw new IllegalArgumentException(String.format("AY09 %d", __id));
	
		throw new Error("TODO");
	}
	
	/**
	 * Returns the inter-process objects for the current process.
	 *
	 * @return The inter-process objects for the current process.
	 * @since 2016/05/20
	 */
	public final Object[] getObjects()
	{
		return getObjects(this.kernel.currentProcess());
	}
	
	/**
	 * Returns the inter-process objects for the given process.
	 *
	 * @param __kp The process to get the objects for.
	 * @return The inter-process objects for the given process.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the current process is not the kernel and
	 * not the process being invoked on, where the given process is not the
	 * current process (a process wants another process's objects).
	 * @since 2016/05/20
	 */
	public final Object[] getObjects(KernelProcess __kp)
		throws NullPointerException, SecurityException
	{
		// Check
		if (__kp == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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
}

