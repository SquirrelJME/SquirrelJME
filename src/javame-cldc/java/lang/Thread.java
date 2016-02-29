// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
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
		throw new Error("TODO");
	}
	
	public Thread(Runnable __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public Thread(String __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public Thread(Runnable __a, String __b)
	{
		super();
		throw new Error("TODO");
	}
	
	public final void checkAccess()
	{
		throw new Error("TODO");
	}
	
	protected Object clone()
		throws CloneNotSupportedException
	{
		if (false)
			throw new CloneNotSupportedException();
		throw new Error("TODO");
	}
	
	public long getId()
	{
		throw new Error("TODO");
	}
	
	public final String getName()
	{
		throw new Error("TODO");
	}
	
	public final int getPriority()
	{
		throw new Error("TODO");
	}
	
	public void interrupt()
	{
		throw new Error("TODO");
	}
	
	public final boolean isAlive()
	{
		throw new Error("TODO");
	}
	
	public boolean isInterrupted()
	{
		throw new Error("TODO");
	}
	
	public final void join(long __a)
		throws InterruptedException
	{
		synchronized (this)
		{
			if (false)
				throw new
					InterruptedException();
			throw new Error("TODO");
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
			throw new Error("TODO");
		}
	}
	
	public final void join()
		throws InterruptedException
	{
		if (false)
			throw new InterruptedException();
		throw new Error("TODO");
	}
	
	public void run()
	{
		throw new Error("TODO");
	}
	
	public final void setName(String __a)
	{
		synchronized (this)
		{
			throw new Error("TODO");
		}
	}
	
	public final void setPriority(int __a)
	{
		throw new Error("TODO");
	}
	
	public void start()
	{
		synchronized (this)
		{
			throw new Error("TODO");
		}
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static int activeCount()
	{
		throw new Error("TODO");
	}
	
	public static Thread currentThread()
	{
		throw new Error("TODO");
	}
	
	public static boolean holdsLock(Object __a)
	{
		throw new Error("TODO");
	}
	
	public static boolean interrupted()
	{
		throw new Error("TODO");
	}
	
	public static void sleep(long __a)
		throws InterruptedException
	{
		if (false)
			throw new InterruptedException();
		throw new Error("TODO");
	}
	
	public static void sleep(long __a, int __b)
		throws InterruptedException
	{
		if (false)
			throw new InterruptedException();
		throw new Error("TODO");
	}
	
	public static void yield()
	{
		throw new Error("TODO");
	}
}

