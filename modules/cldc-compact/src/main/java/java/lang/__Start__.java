// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.DebugShelf;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.brackets.VMThreadBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.lang.UncaughtExceptionHandler;
import java.util.Objects;

/**
 * This is the starting point for every thread regardless of if it is a main
 * thread or a non-main thread.
 *
 * @since 2020/05/31
 */
@SuppressWarnings("unused")
final class __Start__
{
	/** The time to wait between each termination. */
	private static final int _TERM_WAIT_TIME =
		30_000;
	
	/** Exit code for un-handled main exceptions. */
	private static final int _UNHANDLED_EXIT_CODE =
		61;
	
	/**
	 * This is the entry point for any thread which is not a main thread and
	 * the main thread.
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
		}
		
		// Missed an exception, so handle it there
		catch (Throwable t)
		{
			// Use the other exception handler
			UncaughtExceptionHandler.handle(t);
			
			// If this is the main thread, now that this thread is exiting
			// unless another exit condition happens this will effectively
			// mean the process fails
			if (ThreadShelf.vmThreadIsMain(vmThread))
			{
				// Exit with un-handled exception
				ThreadShelf.setCurrentExitCode(__Start__._UNHANDLED_EXIT_CODE);
				
				// Report the trace of this throwable to hopefully help
				// debug it
				ThreadShelf.setTrace(Objects.toString(t.getMessage(), 
					"No message."), DebugShelf.getThrowableTrace(t));
			}
		}
		
		// Make sure the thread is not marked as being alive on termination
		finally
		{
			// Mark the thread as dead
			ThreadShelf.javaThreadSetAlive(javaThread, false);
			ThreadShelf.vmThreadEnd(vmThread);
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
			ThreadShelf.waitForUpdate(__Start__._TERM_WAIT_TIME);
		
		// Now that the main thread exited, use whatever exit code that was
		// set. This should be zero in most cases.
		RuntimeShelf.exit(ThreadShelf.currentExitCode());
	}
}
