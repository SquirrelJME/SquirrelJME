// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
		for (Thread thread : Thread.getAllStackTraces().keySet())
		{
			// Ignore daemon threads if we do not want them
			if (!__includeDaemon && thread.isDaemon())
				continue;
			
			// Count living threads
			if (thread.isAlive())
				if (thread.isDaemon())
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
}
