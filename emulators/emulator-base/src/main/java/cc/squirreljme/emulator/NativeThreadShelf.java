// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Native thread shelf.
 *
 * @since 2020/09/12
 */
public final class NativeThreadShelf
{
	/** The thread count tracker. */
	private static final __ThreadCountTracker__ _TRACKER =
		new __ThreadCountTracker__();
	
	/** The time to wait between tracker updates. */
	private static final int _TRACKER_WAIT = 
		250;
	
	/** The last thread count. */
	static volatile int _currentCount =
		-1;
	
	static
	{
		// Start the tracker thread to time this accordingly
		NativeThreadShelf._TRACKER.start();
	}
	
	/**
	 * Not used.
	 * 
	 * @since 2020/09/12
	 */
	private NativeThreadShelf()
	{
	}
	
	/**
	 * Returns the number of currently living threads.
	 * 
	 * @param __includeMain Include the main thread?
	 * @param __includeDaemon Include daemon threads?
	 * @return The number of alive threads.
	 * @since 2020/09/12
	 */
	public static int aliveThreadCount(boolean __includeMain,
		boolean __includeDaemon)
	{
		int daemon = 0;
		int nonDaemon = 0;
		
		// Count each thread, the only way to check how many threads exist
		// and what is there is to get all stack traces... this is a bit slow
		// but this is only needed for Hosted so it should not be too bad on
		// performance.
		__ThreadCountTracker__ tracker = NativeThreadShelf._TRACKER;
		for (Thread thread : Thread.getAllStackTraces().keySet())
		{
			// Ignore the tracker thread since it is needed for Java SE
			// to work how SquirrelJME expects
			if (thread == tracker)
				continue;
			
			// Ignore daemon threads if we do not want them
			boolean isDaemon = thread.isDaemon();
			if (!__includeDaemon && isDaemon)
				continue;
			
			// Count living threads
			if (thread.isAlive())
				if (isDaemon)
					daemon++;
				else
					nonDaemon++;
		}
		
		// One of these threads here might be the main thread or it might not
		// be but it is hard to tell regardless
		return daemon + Math.max(0, nonDaemon - (__includeMain ? 0 : 1));
	}
	
	/**
	 * As {@link ThreadShelf#javaThreadSetDaemon(Thread)}. 
	 * 
	 * @param __thread The thread to use.
	 * @throws MLECallError If the thread is null or is already alive.
	 * @since 2020/09/12
	 */
	public static void javaThreadSetDaemon(Thread __thread)
		throws MLECallError
	{
		if (__thread == null)
			throw new MLECallError("Null thread.");
		
		try
		{
			__thread.setDaemon(true);
		}
		catch (IllegalThreadStateException ignored)
		{
			throw new MLECallError("Thread is alive.");
		}
	}
	
	
	/**
	 * As {@link ThreadShelf#waitForUpdate(int)}. 
	 *
	 * @param __ms The amount of time to wait for.
	 * @return If the thread was interrupted while waiting.
	 * @throws MLECallError If {@code __ms} is negative.
	 * @since 2020/11/27
	 */
	public static boolean waitForUpdate(int __ms)
		throws MLECallError
	{
		if (__ms < 0)
			throw new MLECallError("Negative waitForUpdate() time"); 
		
		// When do we stop waiting?
		long stopTime = System.nanoTime() + (__ms * 1_000_000L);
		
		// Lock here because we need to get the current count
		synchronized (NativeThreadShelf.class)
		{
			for (int lastCount = NativeThreadShelf._currentCount;;)
			{
				// Did the count change?
				if (lastCount != NativeThreadShelf._currentCount)
					return true;
			
				// Ran out of time?
				long diffTime = stopTime - System.nanoTime();
				if (diffTime <= 0)
					break;
				
				// Wait until we get a signal or this runs again
				try
				{
					NativeThreadShelf.class.wait(
						diffTime / 1_000_000L);
				}
				catch (InterruptedException ignored)
				{
				}
			}
		}
		
		// No changes
		return false;
	}
	
	/**
	 * This is used to keep track of the current thread count so that the
	 * wait for update method works properly.
	 * 
	 * @since 2021/11/27
	 */
	private static final class __ThreadCountTracker__
		extends Thread
	{
		/**
		 * Initializes the tracker.
		 * 
		 * @since 2021/11/27
		 */
		__ThreadCountTracker__()
		{
			super("SquirrelJME-ThreadCountTracker");
			
			super.setDaemon(true);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/11/27
		 */
		@Override
		public void run()
		{
			synchronized (NativeThreadShelf.class)
			{
				for (int lastCount = NativeThreadShelf._currentCount;;)
				{
					// Get the new count
					int currentCount = Thread.activeCount();
					NativeThreadShelf._currentCount = currentCount;
					
					// We saw this change so do signal that the other side
					// should awaken
					if (lastCount != currentCount)
					{
						NativeThreadShelf.class.notifyAll();
						lastCount = currentCount;
					}
					
					// Wait for the next run
					try
					{
						NativeThreadShelf.class.wait(
							NativeThreadShelf._TRACKER_WAIT);
					}
					catch (InterruptedException ignored)
					{
					}
				}
			}
		}
	}
}
