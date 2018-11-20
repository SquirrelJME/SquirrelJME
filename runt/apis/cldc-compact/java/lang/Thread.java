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

import cc.squirreljme.runtime.cldc.asm.SystemAccess;
import cc.squirreljme.runtime.cldc.asm.TaskAccess;
import cc.squirreljme.runtime.cldc.lang.UncaughtExceptionHandler;

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
	
	/** The next virtual thread ID. */
	private static volatile int _NEXT_VIRTUAL_ID =
		0;
	
	/** The runnable to execute. */
	private final Runnable _run;
	
	/** The virtual thread ID. */
	private final int _virtid;
	
	/** The name of this thread. */
	private volatile String _name;
	
	/** Has this thread been started? */
	private volatile boolean _started;
	
	/** The real thread ID. */
	private volatile int _realid;
	
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
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Obtain the next virtual ID to use
		int virtid;
		synchronized (Thread.class)
		{
			this._virtid = (virtid = _NEXT_VIRTUAL_ID++);
		}
		
		this._run = __r;
		this._name = (__n == _USE_FAKE_NAME ? "Thread-" + virtid : __n);
	}
	
	public final void checkAccess()
	{
		throw new todo.TODO();
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
	
	public final boolean isAlive()
	{
		throw new todo.TODO();
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
	
	public final void setName(String __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
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
			
			// Which thread do we run?
			Runnable run = this._run;
			if (run == null)
				run = this;
			
			// Start the thread
			int realid = TaskAccess.startThread(
				new __SubRunner__(run, false), this._name);
			this._realid = realid;
			
			// {@squirreljme.error ZZ2s Could not start the thread.}
			if (realid < 0)
				throw new RuntimeException("ZZ2s");
		}
	}
	
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	public static int activeCount()
	{
		throw new todo.TODO();
	}
	
	public static Thread currentThread()
	{
		throw new todo.TODO();
	}
	
	public static boolean holdsLock(Object __a)
	{
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
	 * Entry point and logic used for the main thread to start using this
	 * thread.
	 *
	 * @throws IllegalThreadStateException If the thread has already been
	 * started.
	 * @since 2018/11/17
	 */
	final void __mainThreadStart()
		throws IllegalThreadStateException
	{
		synchronized (this)
		{
			// {@squirreljme.error ZZ2t A thread may only be started once.}
			if (this._started)
				throw new IllegalThreadStateException("ZZ2t");
			this._started = true;
			
			// Which thread do we run?
			Runnable run = this._run;
			if (run == null)
				run = this;
			
			// Just run this thread in the sub-runner
			new __SubRunner__(run, true).run();
		}
	}
	
	/**
	 * This class runs the runnable and then performs cleanup on it
	 * accordingly.
	 *
	 * @since 2018/11/20
	 */
	private final class __SubRunner__
		implements Runnable
	{
		/** Is this the main thread? */
		final boolean _main;
		
		/** The class to run. */
		final Runnable _run;
		
		/**
		 * Initializes the sub-runner.
		 *
		 * @param __r The runable to run.
		 * @param __main Is this the main thread?
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/20
		 */
		__SubRunner__(Runnable __r, boolean __main)
			throws NullPointerException
		{
			if (__r == null)
				throw new NullPointerException("NARG");
			
			this._main = __main;
			this._run = __r;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/20
		 */
		@Override
		public final void run()
		{
			// Main thread is important for some handling
			boolean main = this._main;
			int exitcode = 0;
			
			// Could fail
			try
			{
				// Increase the active thread count
				if (true)
					throw new todo.TODO();
				
				// Set the thread as alive
				if (true)
					throw new todo.TODO();
				
				// Add the thread to the thread list
				if (true)
					throw new todo.TODO();
				
				// Run the runner
				this._run.run();
			}
			
			// Uncaught exception
			catch (Throwable t)
			{
				// Set the exit code for the process to some error number, if
				// the VM does not exit in this thread but exits in another
				// it would at least be set for the main thread
				// But this is only needed for the main thread
				if (main)
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
				if (true)
					throw new todo.TODO();
			}
			
			// If this is the main thread, wait for every other thread to
			// stop execution. This saves the VM execution code itself from
			// worrying about which threads are running or not.
			if (main)
			{
				// Wait for threads to go away
				if (true)
					throw new todo.TODO();
				
				// Exit the VM with our normal exit code, since no other
				// thread called exit at all for this point
				SystemAccess.exit(exitcode);
			}
		}
	}
}

