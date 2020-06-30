// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.brackets.VMThreadBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.lang.UncaughtExceptionHandler;

/**
 * This is the starting point for every thread regardless of if it is a main
 * thread or a non-main thread.
 *
 * @since 2020/05/31
 */
@SuppressWarnings("unused")
final class __Start__
{
	/** One second in milliseconds. */
	private static final int _MS_SECOND =
		1_000;
	
	/** Exit code for un-handled main exceptions. */
	private static final int _UNHANDLED_EXIT_CODE =
		61;
	
	/**
	 * This is the entry point for any thread which is not a main thread.
	 *
	 * @since 2020/05/31
	 */
	static void __base()
	{
		// Get both of our thread infos
		Thread javaThread = ThreadShelf.currentJavaThread();
		VMThreadBracket vmThread = ThreadShelf.toVMThread(javaThread);
		
		// We will need to catch any exceptions that the thread throws and
		// have some fallback logic for handling it
		try
		{
			// Mark the thread as alive
			ThreadShelf.javaThreadFlagStarted(javaThread);
			ThreadShelf.javaThreadSetAlive(javaThread, true);
			
			// Execute the thread, if we are the main thread we use an
			// alternative run
			if (ThreadShelf.vmThreadIsMain(vmThread))
				ThreadShelf.runProcessMain();
			
			// Use normal run logic
			else
			{
				// Use the thread's runnable or otherwise run the thread itself
				// if none was used
				Runnable run = ThreadShelf.javaThreadRunnable(javaThread);
				if (run == null)
					run = javaThread;
				
				// Execute it
				run.run();
			}
			
			// Mark the thread as dead
			ThreadShelf.javaThreadSetAlive(javaThread, false);
		}
		
		// Missed an exception, so handle it there
		catch (Throwable t)
		{
			// Mark the thread as dead
			ThreadShelf.javaThreadSetAlive(javaThread, false);
			
			// Use the other exception handler
			UncaughtExceptionHandler.handle(t);
			
			// If this is the main thread, now that this thread is exiting
			// unless another exit condition happens this will effectively
			// mean the process fails
			if (ThreadShelf.vmThreadIsMain(vmThread))
				ThreadShelf.setCurrentExitCode(__Start__._UNHANDLED_EXIT_CODE);
		}
	}
	
	/**
	 * This is the entry point for main threads.
	 *
	 * @since 2020/05/31
	 */
	static void __main()
	{
		// Debug
		Debugging.debugNote("Starting main thread...");
		
		// Enter the base thread logic as every thread does the same logic
		__Start__.__base();
		
		// Main thread has terminated, so wait until all other threads have
		// terminated. Only count non-main and non-daemon threads here since
		// otherwise we might have trouble counting everything
		while (ThreadShelf.aliveThreadCount(
			false, false) > 0)
			ThreadShelf.waitForUpdate(__Start__._MS_SECOND);
		
		// Now that the main thread exited, use whatever exit code that was
		// set. This should be zero in most cases.
		RuntimeShelf.exit(ThreadShelf.currentExitCode());
	}
}
