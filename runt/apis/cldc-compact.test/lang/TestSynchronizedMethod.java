// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;

/**
 * This tests synchronized methods.
 *
 * @since 2018/12/04
 */
public class TestSynchronizedMethod
	extends TestRunnable
{
	/** Counter. */
	volatile int _count;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/21
	 */
	@Override
	public void test()
	{
		// Note
		this.secondary("before-lock", this._count++);
		
		// Lock the monitor for this
		synchronized (this)
		{
			// Note
			this.secondary("in-lock", this._count++);
			
			// Create new thread
			Thread t = new Thread(new __Sub__(), "SynchronizedMethodTest");
			t.start();
			
			// Note
			this.secondary("thread-created", this._count++);
			
			// Wait on monitor
			try
			{
				// Wait forever
				this.wait();
				
				// Note
				this.secondary("expected-resume", this._count++);
			}
			
			// Interrupted?
			catch (InterruptedException e)
			{
				// Note
				this.secondary("interrupted", this._count++);
			}
		}
		
		// Note
		this.secondary("after-lock", this._count++);
		
		// Perform another test
		synchronized (TestSynchronizedMethod.class)
		{
			// Note
			this.secondary("in-lock2", this._count++);
			
			// Create new thread
			Thread t = new Thread(new __Cl__(), "SynchronizedMethodTest2");
			t.start();
			
			// Note
			this.secondary("thread-created2", this._count++);
			
			// Wait on monitor
			try
			{
				// Wait forever
				TestSynchronizedMethod.class.wait();
				
				// Note
				this.secondary("expected-resume2", this._count++);
			}
			
			// Interrupted?
			catch (InterruptedException e)
			{
				// Note
				this.secondary("interrupted2", this._count++);
			}
		}
		
		// Note
		this.secondary("after-lock2", this._count++);
	}
	
	/**
	 * Tests synchronized method.
	 *
	 * @since 2018/12/04
	 */
	public synchronized void testSomething()
	{
		// Note
		this.secondary("sub-in-lock", this._count++);
		
		// Notify other thread
		this.notify();
		
		// Note
		this.secondary("sub-after-notify", this._count++);
		
		// Sleep for a bit, for sanity
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}
		
		// Sub continued
		this.secondary("sub-after-sleep", this._count++);
	}
	
	/**
	 * Same as above, but static.
	 *
	 * @param __m The method to report to.
	 * @since 2018/12/04
	 */
	public static synchronized void testStatic(TestSynchronizedMethod __m)
	{
		// Note
		__m.secondary("sub-in-lock2", __m._count++);
		
		// Notify other thread
		TestSynchronizedMethod.class.notify();
		
		// Note
		__m.secondary("sub-after-notify2", __m._count++);
		
		// Sleep for a bit, for sanity
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}
		
		// Sub continued
		__m.secondary("sub-after-sleep2", __m._count++);
	}
	
	/**
	 * Sub-thread which runs on the class!
	 *
	 * @since 2018/12/04
	 */
	final class __Cl__
		implements Runnable
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/12/04
		 */
		@Override
		public void run()
		{
			TestSynchronizedMethod.testStatic(TestSynchronizedMethod.this);
		}
	}
	
	/**
	 * Sub-thread which runs.
	 *
	 * @since 2018/12/04
	 */
	final class __Sub__
		implements Runnable
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/12/04
		 */
		@Override
		public void run()
		{
			TestSynchronizedMethod.this.testSomething();
		}
	}
}

