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
	 * Sub-thread which runs.
	 *
	 * @since 2018/11/21
	 */
	final class __Sub__
		implements Runnable
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/11/21
		 */
		@Override
		public void run()
		{
			TestSynchronizedMethod.this.testSomething();
		}
	}
}

