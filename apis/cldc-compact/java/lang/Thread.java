// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

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
	
	public static void sleep(long __a)
		throws InterruptedException
	{
		sleep(__a, 0);
	}
	
	public static void sleep(long __a, int __b)
		throws InterruptedException
	{
		if (false)
			throw new InterruptedException();
		throw new todo.TODO();
	}
	
	public static void yield()
	{
		throw new todo.TODO();
	}
}

