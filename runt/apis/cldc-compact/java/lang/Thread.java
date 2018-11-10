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
	
	public Thread()
	{
		super();
		throw new todo.TODO();
	}
	
	public Thread(Runnable __a)
	{
		super();
		throw new todo.TODO();
	}
	
	public Thread(String __a)
	{
		super();
		throw new todo.TODO();
	}
	
	public Thread(Runnable __a, String __b)
	{
		super();
		throw new todo.TODO();
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
	
	public final String getName()
	{
		throw new todo.TODO();
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
	
	public void run()
	{
		throw new todo.TODO();
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
	
	public void start()
	{
		synchronized (this)
		{
			throw new todo.TODO();
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

