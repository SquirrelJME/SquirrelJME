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

import cc.squirreljme.runtime.cldc.asm.TaskAccess;

public class Thread
	implements Runnable
{
	public static final int MAX_PRIORITY =
		10;
	
	public static final int MIN_PRIORITY =
		1;
	
	public static final int NORM_PRIORITY =
		5;
	
	/** The next virtual thread ID. */
	private static volatile int _NEXT_VIRTUAL_ID =
		1;
	
	/** The runnable to execute. */
	private final Runnable _run;
	
	/** The virtual thread ID. */
	private final long _virtid;
	
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
		this(null, "Thread-" + _NEXT_VIRTUAL_ID);
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
		this(__r, "Thread-" + _NEXT_VIRTUAL_ID);
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
		
		this._run = __r;
		this._name = __n;
		this._virtid = _NEXT_VIRTUAL_ID++;
	}
	
	public final void checkAccess()
	{
		throw new todo.TODO();
	}
	
	protected Object clone()
		throws CloneNotSupportedException
	{
		if (false)
			throw new CloneNotSupportedException();
		throw new todo.TODO();
	}
	
	public long getId()
	{
		throw new todo.TODO();
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
			int realid = TaskAccess.startThread(run, this._name);
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
}

