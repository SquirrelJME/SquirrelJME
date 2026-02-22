// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests the various complex {@link Thread#run()} combinations and
 * interactions.
 *
 * @since 2026/02/22
 */
public class TestThreadRun
	extends TestRunnable
{
	/** The order number. */
	volatile int _order;
	
	/**
	 * {@inheritDoc}
	 * @since 2026/02/22
	 */
	@SuppressWarnings({"CallToThreadRun", 
		"InstantiatingAThreadWithDefaultRunMethod"})
	@Override
	public void test()
	{
		// Thread with no Runnable
		Thread baseNone = new Thread();
		baseNone.run();
		
		// Thread with Runnable
		Thread baseExt = new Thread(new __Ext__(this));
		baseExt.run();
		
		// Sub with no Runnable, no run()
		Thread subNone = new __SubNone__(this);
		subNone.run();
		
		// Sub with no Runnable, with run()
		Thread subRun = new __SubRun__(this);
		subRun.run();
		
		// Sub with no Runnable, with run() + super.run()
		Thread subRunSuper = new __SubRunSuper__(this);
		subRunSuper.run();
		
		// Sub with Runnable, no run()
		Thread subExt = new __SubNone__(this, new __Ext__(this));
		subExt.run();
		
		// Sub with Runnable, with run()
		Thread subExtRun = new __SubRun__(this, new __Ext__(this));
		subExtRun.run();
		
		// Sub with Runnable, with run() + super.run()
		Thread subExtRunSuper = new __SubRunSuper__(this,
			new __Ext__(this));
		subExtRunSuper.run();
	}
	
	/**
	 * Emit secondary order.
	 *
	 * @param __from Where did this come from?
	 * @since 2026/02/22
	 */
	void __order(String __from)
	{
		this.secondary("order" + (++this._order), __from);
	}
	
	/**
	 * Base runnable.
	 *
	 * @since 2026/02/22
	 */
	private static final class __Ext__
		implements Runnable
	{
		/** The owning test. */
		public final TestThreadRun test;
		
		/**
		 * Initializes this test class.
		 *
		 * @param __test The owning test.
		 * @since 2026/02/22
		 */
		public __Ext__(TestThreadRun __test)
		{
			this.test = __test;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2026/02/22
		 */
		@Override
		public void run()
		{
			this.test.__order("Runnable");
		}
	}
	
	/**
	 * Thread with no {@link Thread#run()}.
	 *
	 * @since 2026/02/22
	 */
	private static final class __SubNone__
		extends Thread
	{
		/** The owning test. */
		public final TestThreadRun test;
		
		/**
		 * Initializes this test class.
		 *
		 * @param __test The owning test.
		 * @param __run The runnable to use.
		 * @since 2026/02/22
		 */
		public __SubNone__(TestThreadRun __test, Runnable __run)
		{
			super(__run);
			
			this.test = __test;
		}
		
		/**
		 * Initializes this test class.
		 *
		 * @param __test The owning test.
		 * @since 2026/02/22
		 */
		public __SubNone__(TestThreadRun __test)
		{
			this.test = __test;
		}
	}
	
	/**
	 * Thread with {@link Thread#run()}.
	 *
	 * @since 2026/02/22
	 */
	private static final class __SubRun__
		extends Thread
	{
		/** The owning test. */
		public final TestThreadRun test;
		
		/**
		 * Initializes this test class.
		 *
		 * @param __test The owning test.
		 * @param __run The runnable to use.
		 * @since 2026/02/22
		 */
		public __SubRun__(TestThreadRun __test, Runnable __run)
		{
			super(__run);
			
			this.test = __test;
		}
		
		/**
		 * Initializes this test class.
		 *
		 * @param __test The owning test.
		 * @since 2026/02/22
		 */
		public __SubRun__(TestThreadRun __test)
		{
			this.test = __test;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2026/02/22
		 */
		@Override
		public void run()
		{
			this.test.__order("SubRun");
		}
	}
	
	/**
	 * Thread with {@link Thread#run()} that calls {@code super}.
	 *
	 * @since 2026/02/22
	 */
	private static final class __SubRunSuper__
		extends Thread
	{
		/** The owning test. */
		public final TestThreadRun test;
		
		/**
		 * Initializes this test class.
		 *
		 * @param __test The owning test.
		 * @param __run The runnable to use.
		 * @since 2026/02/22
		 */
		public __SubRunSuper__(TestThreadRun __test, Runnable __run)
		{
			super(__run);
			
			this.test = __test;
		}
		
		/**
		 * Initializes this test class.
		 *
		 * @param __test The owning test.
		 * @since 2026/02/22
		 */
		public __SubRunSuper__(TestThreadRun __test)
		{
			this.test = __test;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2026/02/22
		 */
		@Override
		public void run()
		{
			// Call super run first
			super.run();
			
			this.test.__order("SubRunSuper");
		}
	}
}
