// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.brackets.VMThreadBracket;

/**
 * A thread represents literally a single stream of execution that can
 * execute concurrently (or not).
 *
 * SquirrelJME may be running with multiple threads executing at once or it
 * may also be executing cooperatively (only a single thread at a time). If
 * SquirrelJME is running cooperatively then only locking,
 * {@link Thread#sleep(long, int)}, or {@link Thread#yield()} will allow
 * another thread to run.
 *
 * @since 2018/12/07
 */
public class Thread
	implements Runnable
{
	/** Maximum supported priority. */
	public static final int MAX_PRIORITY =
		10;
	
	/** Minimum supported priority. */
	public static final int MIN_PRIORITY =
		1;
	
	/** Default priority. */
	public static final int NORM_PRIORITY =
		5;
	
	/** Second in nano seconds. */
	private static final long _NS_SECOND =
		1_000_000L;
	
	/** The runnable that this thread uses for its main code, if applicable. */
	@SuppressWarnings("unused")
	private final Runnable _runnable;
	
	/** The virtual machine thread this uses. */
	private final VMThreadBracket _vmThread;
	
	/** The name of this thread. */
	private volatile String _name;
	
	/** Has this thread been started? */
	@SuppressWarnings("unused")
	private volatile boolean _started;
	
	/** Is this thread alive? */
	@SuppressWarnings("unused")
	private volatile boolean _isAlive;
	
	/** The priority of the thread. */
	private volatile int _priority =
		Thread.NORM_PRIORITY;
	
	/** Is this thread interrupted? */
	volatile boolean _interrupted;
	
	/**
	 * Initializes the thread which invokes this object's {@link #run()} and
	 * uses a default thread name.
	 *
	 * @since 2018/11/17
	 */
	public Thread()
	{
		VMThreadBracket vmThread = ThreadShelf.createVMThread(this);
		this._vmThread = vmThread;
		
		this._runnable = null;
		this._name = Thread.__defaultName(null, vmThread);
	}
	
	/**
	 * Initializes the thread which invokes this object's {@link #run()} and
	 * uses a default thread name.
	 *
	 * @param __r The runnable to execute.
	 * @since 2018/11/17
	 */
	public Thread(Runnable __r)
	{
		VMThreadBracket vmThread = ThreadShelf.createVMThread(this);
		this._vmThread = vmThread;
		
		this._runnable = __r;
		this._name = Thread.__defaultName(null, vmThread);
	}
	
	/**
	 * Initializes the thread which invokes this object's {@link #run()} and
	 * uses the specified thread name.
	 *
	 * @param __n The thread's name.
	 * @throws NullPointerException If the thread name is null.
	 * @since 2018/11/17
	 */
	public Thread(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		VMThreadBracket vmThread = ThreadShelf.createVMThread(this);
		this._vmThread = vmThread;
		
		this._runnable = null;
		this._name = Thread.__defaultName(__n, vmThread);
	}
	
	/**
	 * Initializes the thread which invokes the given {@link Runnable} and uses
	 * the given name.
	 *
	 * @param __r The runnable to execute.
	 * @param __n The thread's name.
	 * @throws NullPointerException If the thread name is null.
	 * @since 2018/11/17
	 */
	public Thread(Runnable __r, String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		VMThreadBracket vmThread = ThreadShelf.createVMThread(this);
		this._vmThread = vmThread;
		
		this._runnable = __r;
		this._name = Thread.__defaultName(__n, vmThread);
	}
	
	/**
	 * Checks that the thread has access to perform some operations.
	 *
	 * @throws SecurityException If access is denied.
	 * @since 2018/11/21
	 */
	public final void checkAccess()
		throws SecurityException
	{
		SecurityManager sm = System.getSecurityManager();
		if (sm != null)
			sm.checkAccess(this);
	}
	
	/**
	 * Returns the ID of this thread.
	 *
	 * @return The thread ID.
	 * @since 2018/11/20
	 */
	public long getId()
	{
		return ThreadShelf.vmThreadId(this._vmThread);
	}
	
	/**
	 * Returns the name of this thread.
	 *
	 * @return The thread name.
	 * @since 2018/11/17
	 */
	public final String getName()
	{
		return this._name;
	}
	
	/**
	 * Returns the priority of the thread.
	 *
	 * @return The thread priority.
	 * @since 2018/12/07
	 */
	public final int getPriority()
	{
		return this._priority;
	}
	
	/**
	 * Interrupts the thread.
	 *
	 * @since 2018/11/21
	 */
	public void interrupt()
	{
		// Signal software interrupt
		this._interrupted = true;
		
		// Signal hardware interrupt
		ThreadShelf.vmThreadInterrupt(this._vmThread);
	}
	
	/**
	 * Is this thread currently alive?
	 *
	 * @return If this thread is alive.
	 * @since 2018/11/20
	 */
	public final boolean isAlive()
	{
		return this._isAlive;
	}
	
	/**
	 * Is this thread interrupted?
	 *
	 * @return If this thread is interrupted.
	 * @since 2018/11/21
	 */
	public boolean isInterrupted()
	{
		return this._interrupted;
	}
	
	/**
	 * Waits forever for a thread to die or until interrupted.
	 *
	 * @throws InterruptedException If the thread was interrupted while
	 * waiting.
	 * @since 2018/12/07
	 */
	public final void join()
		throws InterruptedException
	{
		this.join(0, 0);
	}
	
	/**
	 * Waits for a thread to die.
	 *
	 * @param __ms The milliseconds to wait for, if this is zero then this
	 * will wait forever.
	 * @throws IllegalArgumentException If the timeout is negative.
	 * @throws InterruptedException If the thread was interrupted while
	 * waiting.
	 * @since 2018/12/07
	 */
	public final void join(long __ms)
		throws IllegalArgumentException, InterruptedException
	{
		this.join(__ms, 0);
	}
	
	/**
	 * Waits for a thread to die.
	 *
	 * If both milliseconds and nanoseconds are zero this will wait forever.
	 *
	 * @param __ms The milliseconds to wait for.
	 * @param __ns The nanoseconds to wait for.
	 * @throws IllegalArgumentException If the timeout is negative.
	 * @throws InterruptedException If the thread was interrupted while
	 * waiting.
	 * @since 2018/12/07
	 */
	public final void join(long __ms, int __ns)
		throws IllegalArgumentException, InterruptedException
	{
		// The end time, since our thread could be notified
		long end = (__ms == 0 && __ns == 0 ? Long.MAX_VALUE :
			System.nanoTime() + (__ms * Thread._NS_SECOND) + __ns);
		
		// Lock on self
		synchronized (this)
		{
			// Loop constantly until the thread is dead
			for (;;)
			{
				// Time ended?
				long now;
				if ((now = System.nanoTime()) >= end)
					return;
				
				// Did the thread die yet?
				if (ThreadShelf.javaThreadIsStarted(this) &&
					!this.isAlive())
					return;
				
				// Otherwise wait on our own monitor
				long diff = end - now;
				this.wait(diff / Thread._NS_SECOND,
					(int)(diff % Thread._NS_SECOND));
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/17
	 */
	@Override
	public void run()
	{
		// Does nothing
	}
	
	/**
	 * Sets the name of the thread.
	 *
	 * @param __n The new name of the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/21
	 */
	public final void setName(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Check access first
		this.checkAccess();
		
		// Set new name
		synchronized (this)
		{
			this._name = __n;
		}
	}
	
	/**
	 * Sets the priority of the thread.
	 *
	 * @param __p The thread priority.
	 * @throws IllegalArgumentException If the priority is not valid.
	 * @throws SecurityException If setting the priority is not permitted.
	 * @since 2018/12/07
	 */
	public final void setPriority(int __p)
		throws IllegalArgumentException, SecurityException
	{
		// {@squirreljme.error ZZ20 Invalid priority.}
		if (__p < Thread.MIN_PRIORITY || __p > Thread.MAX_PRIORITY)
			throw new IllegalArgumentException("ZZ20");
		
		// Check access
		this.checkAccess();
		
		// Store for later
		this._priority = __p;
		
		// Set the thread's hardware priority
		ThreadShelf.vmThreadSetPriority(this._vmThread, __p);
	}
	
	/**
	 * Starts the specified thread.
	 *
	 * @throws IllegalThreadStateException If the thread was already started
	 * or failed to start.
	 * @since 2018/11/17
	 */
	public void start()
		throws IllegalThreadStateException
	{
		synchronized (this)
		{
			// {@squirreljme.error ZZ21 A thread may only be started once.}
			if (ThreadShelf.javaThreadIsStarted(this))
				throw new IllegalThreadStateException("ZZ21");
			
			// {@squirreljme.error ZZ22 Failed to start the thread.}
			if (!ThreadShelf.vmThreadStart(this._vmThread))
				throw new IllegalThreadStateException("ZZ22");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/20
	 */
	@Override
	public String toString()
	{
		// JavaSE is in the format of `Thread[name,priority,group]` but
		// we do not have thread groups here
		return "Thread[" + this._name + "," + this._priority + "]";
	}
	
	/**
	 * Returns the number of threads which are currently alive.
	 *
	 * @return The number of alive threads.
	 * @since 2018/11/20
	 */
	public static int activeCount()
	{
		return ThreadShelf.aliveThreadCount(
			true, true);
	}
	
	/**
	 * Returns the current thread.
	 *
	 * @return The current thread.
	 * @since 2018/11/20
	 */
	public static Thread currentThread()
	{
		return ThreadShelf.currentJavaThread();
	}
	
	/**
	 * Checks if the current thread holds the monitor for the given object.
	 *
	 * @param __o The object to check.
	 * @return If the thread owns the monitor.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/21
	 */
	public static boolean holdsLock(Object __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return ObjectShelf.holdsLock(ThreadShelf.currentJavaThread(), __o);
	}
	
	/**
	 * Checks if the current thread was interrupted, if it was then the
	 * interrupt status will be cleared.
	 *
	 * @return If this thread was interrupted.
	 * @since 2018/11/21
	 */
	public static boolean interrupted()
	{
		return ThreadShelf.javaThreadClearInterrupt(Thread.currentThread());
	}
	
	/**
	 * Causes the thread to sleep for the given amount of milliseconds.
	 *
	 * @param __ms The number of milliseconds to sleep for.
	 * @throws InterruptedException If the thread was interrupted.
	 * @since 2018/11/04
	 */
	public static void sleep(long __ms)
		throws InterruptedException
	{
		Thread.sleep(__ms, 0);
	}
	
	/**
	 * Causes the thread to sleep for the given milliseconds and nanoseconds.
	 *
	 * @param __ms The milliseconds to sleep for.
	 * @param __ns The nanoseconds to sleep for, in the range of 0-999999.
	 * @throws IllegalArgumentException If the milliseconds and/or nanoseconds
	 * are out of range.
	 * @throws InterruptedException If the thread was interrupted.
	 * @since 2018/11/04
	 */
	@SuppressWarnings("MagicNumber")
	public static void sleep(long __ms, int __ns)
		throws IllegalArgumentException, InterruptedException
	{
		// {@squirreljme.error ZZ23 Invalid sleep arguments.}
		if (__ms < 0 || __ns < 0 || __ns > 999999)
			throw new IllegalArgumentException("ZZ23");
		
		// Convert to integer but do not sleep for too long
		int ims = (__ms > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)__ms);
		
		// Perform sleep, if it was interrupted then the return status will
		// be non-zero!
		if (ThreadShelf.sleep(ims, __ns))
		{
			// Signal interrupts
			Thread.currentThread().interrupt();
			
			// {@squirreljme.error ZZ24 Sleep was interrupted.}
			throw new InterruptedException("ZZ24");
		}
	}
	
	/**
	 * Yields the current thread giving up its execution slice, but allowing
	 * it to continue instantly resuming as needed.
	 *
	 * @since 2018/12/05
	 */
	public static void yield()
	{
		// Zero times means to yield
		try
		{
			Thread.sleep(0, 0);
		}
		catch (InterruptedException ignored)
		{
			// Ignore
		}
	}
	
	/**
	 * Determines a default name for the given thread.
	 *
	 * @param __name The supplied name of the thread.
	 * @param __vm The virtual machine thread.
	 * @return The name to use for the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	private static String __defaultName(String __name, VMThreadBracket __vm)
		throws NullPointerException
	{
		if (__vm == null)
			throw new NullPointerException("NARG");
		
		// Use the given name
		if (__name != null)
			return __name;
		
		// Otherwise it is just the attached thread ID
		return "Thread-" + ThreadShelf.vmThreadId(__vm);
	}
}

