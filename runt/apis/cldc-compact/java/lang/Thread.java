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

import cc.squirreljme.runtime.cldc.asm.ObjectAccess;
import cc.squirreljme.runtime.cldc.asm.StaticMethod;
import cc.squirreljme.runtime.cldc.asm.SystemAccess;
import cc.squirreljme.runtime.cldc.asm.TaskAccess;
import cc.squirreljme.runtime.cldc.lang.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * A thread represents literally a single stream of execution that can
 * execute concurrently (or not).
 *
 * SquirrelJME may be running with multiple threads executing at once or it
 * may also be executing cooperatively (only a single thread at a time). If
 * SquirrelJME is running cooperatively then only locking,
 * {@link Thread.sleep(long, int)}, or {@link Thread.yield()} will allow
 * another thread to run.
 *
 * @since 2018/12/07
 */
public class Thread
	implements Runnable
{
	/** Use fake name for string? */
	private static final String _USE_FAKE_NAME =
		new String();
	
	/** Maximum supported priority. */
	public static final int MAX_PRIORITY =
		10;
	
	/** Minimum supported priority. */
	public static final int MIN_PRIORITY =
		1;
	
	/** Default priority. */
	public static final int NORM_PRIORITY =
		5;
	
	/** Start kind: Self Runnable */
	private static final int _START_SELF_RUNNABLE =
		1;
	
	/** Start kind: Specified Runnable. */
	private static final int _START_GIVEN_RUNNABLE =
		2;
	
	/** Start kind: MIDlet (construct then run startApp()). */
	private static final int _START_MIDLET =
		3;
	
	/** Start kind: main() method (is String[] argument). */
	private static final int _START_MAIN =
		4;
	
	/** Threads by virtual ID. */
	private static final Map<Integer, Thread> _BY_VIRTID =
		new HashMap<>();
	
	/** Threads by real ID. */
	private static final Map<Integer, Thread> _BY_REALID =
		new HashMap<>();
	
	/** The next virtual thread ID. */
	private static volatile int _NEXT_VIRTUAL_ID =
		0;
	
	/** The active number of threads. */
	private static volatile int _ACTIVE_THREADS;
	
	/** Which kind of start are we doing? */
	private final int _startkind;
	
	/** The method to execute. */
	private final StaticMethod _runmethod;
	
	/** The argument to the method. */
	private final Object _runargument;
	
	/** The virtual thread ID. */
	private final int _virtid;
	
	/** The real thread ID. */
	private volatile int _realid =
		-1;
	
	/** The name of this thread. */
	private volatile String _name;
	
	/** Has this thread been started? */
	private volatile boolean _started;
	
	/** Is this thread alive? */
	private volatile boolean _isalive;
	
	/** The priority of the thread. */
	private volatile int _priority =
		NORM_PRIORITY;
	
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
		this(null, _USE_FAKE_NAME);
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
		this(__r, _USE_FAKE_NAME);
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
		this(null, __n);
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
		this(__n, (__r == null ? _START_SELF_RUNNABLE : _START_GIVEN_RUNNABLE),
			null, __r);
	}
	
	/**
	 * Initializes a thread which is registered in this constructor and
	 * additionally has the given name and real ID.
	 *
	 * The thread is started in the started state and technically is not
	 * removed ever. This is generally used by the native display thread
	 * since there has to be a thread running as the VM sees it otherwise
	 * things will break much. A thread started this way never terminates
	 * unless that termination is explicit.
	 *
	 * @param __n The thread name.
	 * @param __rid The real ID.
	 * @since 2018/12/03
	 */
	private Thread(int __rid, String __n)
	{
		this._startkind = -1;
		this._runmethod = null;
		this._runargument = null;
		this._realid = __rid;
		this._name = __n;
		this._started = true;
		this._isalive = true;
		
		// Obtain the next virtual ID to use
		int virtid;
		synchronized (Thread.class)
		{
			this._virtid = (virtid = _NEXT_VIRTUAL_ID++);
		}
		
		// Now register this thread in the main objects
		this.__registerThread();
	}
	
	/**
	 * Initializes the thread to execute the given static method.
	 *
	 * @param __n The name of the thread.
	 * @param __rk How is this method to be run?
	 * @param __mm The static method to call.
	 * @param __ma The argument to use.
	 * @throws NullPointerException If no name was specified.
	 * @since 2018/11/20
	 */
	private Thread(String __n, int __rk, StaticMethod __mm, Object __ma)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Obtain the next virtual ID to use
		int virtid;
		synchronized (Thread.class)
		{
			this._virtid = (virtid = _NEXT_VIRTUAL_ID++);
		}
		
		// Set
		this._name = (__n == _USE_FAKE_NAME ? "Thread-" + virtid : __n);
		this._startkind = __rk;
		this._runmethod = __mm;
		this._runargument = __ma;
		
		// The main thread is implicitly started
		boolean implicitstart = (__rk == _START_MAIN || __rk == _START_MIDLET);
		this._started = implicitstart;
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
		return this._virtid;
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
		int realid = this._realid;
		if (realid >= 0)
			TaskAccess.signalInterrupt(realid);
	}
	
	/**
	 * Is this thread currently alive?
	 *
	 * @return If this thread is alive.
	 * @since 2018/11/20
	 */
	public final boolean isAlive()
	{
		return this._isalive;
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
			System.nanoTime() + (__ms * 1_000_000L) + __ns);
		
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
				if (this._started && !this._isalive)
					return;
				
				// Otherwise wait on our own monitor
				long diff = end - now;
				this.wait(diff / 1_000_000L, (int)(diff % 1_000_000L));
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
		// {@squirreljme.error ZZ3g Invalid priority.}
		if (__p < MIN_PRIORITY || __p > MAX_PRIORITY)
			throw new IllegalArgumentException("ZZ3g");
		
		// Check access
		this.checkAccess();
		
		// Store for later
		this._priority = __p;
		
		// Only set the priority if the thread is active
		int realid = this._realid;
		if (realid >= 0)
			TaskAccess.setThreadPriority(realid, __p);
	}
	
	/**
	 * Starts the specified thread.
	 *
	 * @throws IllegalThreadStateException 
	 * @since 2018/11/17
	 */
	public void start()
		throws IllegalThreadStateException
	{
		synchronized (this)
		{
			// {@squirreljme.error ZZ2r A thread may only be started once.}
			if (this._started)
				throw new IllegalThreadStateException("ZZ2r");
			this._started = true;
			
			// Start the thread
			int realid = TaskAccess.startThread(this, this._name);
			this._realid = realid;
			
			// {@squirreljme.error ZZ2s Could not start the thread.}
			if (realid < 0)
				throw new RuntimeException("ZZ2s");
			
			// Set the initial priority of the thread
			TaskAccess.setThreadPriority(realid, this._priority);
			this._isalive = true;
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
		return Thread._ACTIVE_THREADS;
	}
	
	/**
	 * Returns the current thread.
	 *
	 * @return The current thread.
	 * @since 2018/11/20
	 */
	public static Thread currentThread()
	{
		int rid = TaskAccess.currentThread();
		
		// If the map is not initialized yet, ignore
		Map<Integer, Thread> byrealid = _BY_REALID;
		if (byrealid == null)
			return null;
		
		// Lock, it should be in the map
		synchronized (Thread.class)
		{
			return byrealid.get(rid);
		}
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
		
		return ObjectAccess.holdsLock(TaskAccess.currentThread(), __o);
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
		Thread self = Thread.currentThread();
		
		// Check interrupt?
		boolean rv = self._interrupted;
		self._interrupted = false;
		return rv;
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
	public static void sleep(long __ms, int __ns)
		throws IllegalArgumentException, InterruptedException
	{
		// {@squirreljme.error ZZ1c Invalid sleep arguments.}
		if (__ms < 0 || __ns < 0 || __ns > 999999)
			throw new IllegalArgumentException("ZZ1c");
		
		// {@squirreljme.error ZZ1d Sleep was interrupted.}
		if (TaskAccess.sleep(__ms, __ns))
		{
			Thread.currentThread()._interrupted = false;
			throw new InterruptedException("ZZ1d");
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
		TaskAccess.sleep(0, 0);
	}
	
	/**
	 * Registers this thread so that way it is in the thread list and can be
	 * obtained and such.
	 *
	 * @since 2018/12/03
	 */
	final void __registerThread()
	{
		// Lock
		synchronized (Thread.class)
		{
			// Increase the active thread count
			Thread._ACTIVE_THREADS++;
			
			// Add threads to the thread list
			Map<Integer, Thread> byvirtid = Thread._BY_VIRTID,
				byrealid = Thread._BY_REALID;
			byvirtid.put(this._virtid, this);
			byrealid.put(this._realid, this);
		}
	}
	
	/**
	 * Ends the current thread and cleans up its registration.
	 *
	 * @since 2018/12/03
	 */
	final void __revokeThread()
	{
		// Thread no longer alive
		this._isalive = false;
		
		// Lock
		synchronized (Thread.class)
		{
			// Decrease the active count
			Thread._ACTIVE_THREADS--;
			
			// Remove from the thread list
			Map<Integer, Thread> byvirtid = Thread._BY_VIRTID,
				byrealid = Thread._BY_REALID;
			byvirtid.remove(this._virtid);
			byrealid.remove(this._realid);
		}
		
		// Signal all threads which are waiting on a join for this thread
		// only
		synchronized (this)
		{
			this.notifyAll();
		}
		
		// Signal anything waiting on the class itself, to indicate that
		// a thread has finished
		int startkind = this._startkind;
		if (startkind != _START_MAIN && startkind != _START_MIDLET)
			synchronized (Thread.class)
			{
				Thread.class.notifyAll();
			}
	}
	
	/**
	 * This is the starting point for all threads, including the main thread
	 * and such.
	 *
	 * @throws IllegalThreadStateException If the thread has already been
	 * started.
	 * @since 2018/11/20
	 */
	final void __start()
		throws IllegalThreadStateException
	{
		// Get the kind and determine if this is a main entry point
		int startkind = this._startkind;
		boolean ismain = (startkind == _START_MAIN ||
			startkind == _START_MIDLET);
		
		// We need to lock because the real ID might just not get assigned
		// yet here.
		int realid;
		int virtid = this._virtid;
		if (!ismain)
			synchronized (this)
			{
				// {@squirreljme.error ZZ2w Real ID has not been set yet while
				// in the lock, this should not occur unless the virtual
				// machine is very broken.}
				if ((realid = this._realid) < 0)
					throw new Error("ZZ2w");
			}
		
		// Main thread, so set our real ID to the current thread the VM says
		// we are since it will still be negatively initialized 
		else
		{
			realid = TaskAccess.currentThread();
			this._realid = realid;
		}
		
		// The exit code is something that is only handled by the main thread
		// It will exit with the given code 
		int exitcode = 0;
		
		// Execution setup
		try
		{
			// Register this thread
			this.__registerThread();
				
			// Set the thread as alive
			this._isalive = true;
			
			// The main method and/or its arguments
			StaticMethod runmethod = this._runmethod;
			Object runargument = this._runargument;
			
			// How do we run this thread?
			switch (this._startkind)
			{
					// Start Runnable in this instance (extended from)
				case _START_SELF_RUNNABLE:
					this.run();
					break;
					
					// Start the given runnable
				case _START_GIVEN_RUNNABLE:
					((Runnable)runargument).run();
					break;
					
					// Start MIDlet, construct then startApp()
				case _START_MIDLET:
					ObjectAccess.invokeStatic(runmethod,
						ObjectAccess.classByName((String)runargument).
						__newInstance());
					break;
					
					// Start main(String[]) method
				case _START_MAIN:
					ObjectAccess.invokeStatic(runmethod, runargument);
					break;
					
					// Unknown
				default:
					throw new todo.OOPS();
			}
		}
		
		// Uncaught exception
		catch (Throwable t)
		{
			// Set the exit code for the process to some error number, if
			// the VM does not exit in this thread but exits in another
			// it would at least be set for the main thread
			// But this is only needed for the main thread
			if (ismain)
				exitcode = 127;
			
			// Handle uncaught exception
			UncaughtExceptionHandler.handle(t);
		}
		
		// Cleanup after the thread:
		//  * Signal joins (for those that are waiting)
		//  * Remove the thread from the thread list
		//  * Decrease the active count
		//  * Set thread as not alive
		finally
		{
			// Revoke this thread
			this.__revokeThread();
		}
		
		// If this is the main thread, wait for every other thread to
		// stop execution. This saves the VM execution code itself from
		// worrying about which threads are running or not.
		if (ismain)
		{
			// Wait for threads to go away
			for (;;)
			{
				// No threads are active, so that works
				if (Thread._ACTIVE_THREADS == 0)
					break;
				
				// Wait a bit until trying again, unless we get notified
				synchronized (Thread.class)
				{
					try
					{
						// Three seconds is short enough to not be forever
						// but long enough to where we can get a notify to
						// quit
						Thread.class.wait(3_000);
					}
					catch (InterruptedException e)
					{
					}
				}
			}
			
			// Exit the VM with our normal exit code, since no other
			// thread called exit at all for this point
			SystemAccess.exit(exitcode);
		}
	}
}

