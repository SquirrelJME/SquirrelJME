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
 * Tests interrupted, monitors, and threads as well.
 *
 * @since 2018/11/20
 */
public class TestVMInterrupt
	extends TestRunnable
{
	/** The object to lock on. */
	protected final Object lock =
		new Object();
	
	/** The order of things, is a check for locks. */
	protected volatile int order;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/20
	 */
	@Override
	public void test()
	{
		// Note
		this.secondary("a-before-lock", order++);
		
		// Lock on this object
		synchronized (this.lock)
		{
			// Note
			this.secondary("a-after-lock", order++);
			
			// Setup thread to run
			Thread runner = new Thread(new __Runner__(
				Thread.currentThread()), "VMInterruptChild");
			runner.start();
			
			// Note
			this.secondary("a-thread-created", order++);
			
			// Wait for a notification
			try
			{
				this.lock.wait(1000);
				
				// Note
				this.secondary("a-was-not-interrupted", order++);
			}
			catch (InterruptedException e)
			{
				// Note
				this.secondary("a-was-interrupted", order++);
			}
		}
		
		// Note
		this.secondary("a-done", order++);
	}
	
	/**
	 * Performs the second part of the test run.
	 *
	 * @since 2018/11/20
	 */
	final class __Runner__
		implements Runnable
	{
		/** The thread to signal. */
		protected final Thread signal;
		
		/**
		 * Initializes the runner.
		 *
		 * @param __t The thread to signal.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/20
		 */
		public __Runner__(Thread __t)
			throws NullPointerException
		{
			if (__t == null)
				throw new NullPointerException("NARG");
			
			this.signal = __t;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/20
		 */
		@Override
		public void run()
		{
			// Lock
			synchronized (TestVMInterrupt.this.lock)
			{
				// Note
				TestVMInterrupt.this.secondary("b-in-lock",
					TestVMInterrupt.this.order++);
				
				// Interrupt A
				this.signal.interrupt();
				
				// Note
				TestVMInterrupt.this.secondary("b-interrupt-a",
					TestVMInterrupt.this.order++);
				
				// Sleep for a bit
				try
				{
					// Note
					TestVMInterrupt.this.secondary("b-dosleep",
						TestVMInterrupt.this.order++);
					
					// Sleep for a bit longer than the main thread
					Thread.sleep(3000);
					
					// Note
					TestVMInterrupt.this.secondary("b-sleepdone",
						TestVMInterrupt.this.order++);
				}
				catch (InterruptedException e)
				{
					TestVMInterrupt.this.secondary("b-interrupted",
						TestVMInterrupt.this.order++);
				}
				
				// About to unlock
				TestVMInterrupt.this.secondary("b-about-to-unlock",
					TestVMInterrupt.this.order++);
			}
		}
	}
}

