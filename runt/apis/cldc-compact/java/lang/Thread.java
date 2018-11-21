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

public class Thread
	implements Runnable
{
	/** Use fake name for string? */
	private static final String _USE_FAKE_NAME =
		new String();
	
	public static final int MAX_PRIORITY =
		10;
	
	public static final int MIN_PRIORITY =
		1;
	
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
	private volatile int _realid;
	
	/** The name of this thread. */
	private volatile String _name;
	
	/** Has this thread been started? */
	private volatile boolean _started;
	
	/** Is this thread alive? */
	private volatile boolean _isalive;
	
	/** The priority of the thread. */
	private volatile int _priority =
		NORM_PRIORITY;
	
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
		this._started = (__rk == _START_MAIN || __rk == _START_MIDLET);
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
	
	public final int getPriority()
	{
		throw new todo.TODO();
	}
	
	public void interrupt()
	{
		throw new todo.TODO();
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
	
	public boolean isInterrupted()
	{
		throw new todo.TODO();
	}
	
	public final void join(long __a)
		throws InterruptedException
	{
		synchronized (this)
		{
			if (false)
				throw new
					InterruptedException();
			throw new todo.TODO();
		}
	}
	
	public final void join(long __a, int __b)
		throws InterruptedException
	{
		synchronized (this)
		{
			if (false)
				throw new
					InterruptedException();
			throw new todo.TODO();
		}
	}
	
	public final void join()
		throws InterruptedException
	{
		if (false)
			throw new InterruptedException();
		throw new todo.TODO();
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
	
	public final void setPriority(int __a)
	{
		throw new todo.TODO();
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
		
		// Lock
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
		
		throw new todo.TODO();
	}
	
	public static boolean interrupted()
	{
		throw new todo.TODO();
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
			throw new InterruptedException("ZZ1d");
	}
	
	public static void yield()
	{
		throw new todo.TODO();
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
		// Debug
		todo.DEBUG.note("Start thread: %s", this._name);
		
		// Get the kind and determin if this is a main entry point
		int startkind = this._startkind;
		boolean ismain = (startkind == _START_MAIN ||
			startkind == _START_MIDLET);
		Integer virtid = this._virtid,
			realid = TaskAccess.currentThread();
		
		// The main method and/or its arguments
		StaticMethod runmethod = this._runmethod;
		Object runargument = this._runargument;
		
		// The exit code is something that is only handled by the main thread
		// It will exit with the given code 
		int exitcode = 0;
		
		// Execution setup
		try
		{
			// Lock
			synchronized (Thread.class)
			{
				// Increase the active thread count
				Thread._ACTIVE_THREADS++;
				
				// Add threads to the thread list
				Map<Integer, Thread> byvirtid = Thread._BY_VIRTID,
					byrealid = Thread._BY_REALID;
				byvirtid.put(virtid, this);
				byrealid.put(realid, this);
			}
				
			// Set the thread as alive
			this._isalive = true;
			
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
						ObjectAccess.newInstanceByName((String)runargument));
					break;
					
					// Start main(String) method
				case _START_MAIN:
					ObjectAccess.invokeStatic(runmethod, runargument);
					break;
					
					// Unknown
				default:
					throw new RuntimeException("OOPS");
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
				byvirtid.remove(virtid);
				byrealid.remove(realid);
			}
			
			// Signal all threads which are waiting on a join
			synchronized (this)
			{
				this.notifyAll();
			}
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
				
				if (true)
					throw new todo.TODO();
			}
			
			// Exit the VM with our normal exit code, since no other
			// thread called exit at all for this point
			SystemAccess.exit(exitcode);
		}
	}
}

