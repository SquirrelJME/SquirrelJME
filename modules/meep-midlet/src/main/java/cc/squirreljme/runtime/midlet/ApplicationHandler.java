// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.midlet;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.runtime.cldc.Poking;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This handles the main starting loop and otherwise for applications, it is
 * used in conjunction with {@link ApplicationInterface}.
 *
 * @see ApplicationInterface
 * @since 2021/11/30
 */
public final class ApplicationHandler
{
	/** One second in milliseconds. */
	private static final int _TERM_WAIT_TIME =
		30_000;
	
	/** Maximum settle time after starting. */
	private static final long _SETTLE_NS =
		2_000_000_000;
	
	/**
	 * Handles the main application handling and logic.
	 *
	 * @param <T> The type of instance used.
	 * @param __ai The interface to the program.
	 * @throws NullPointerException on null arguments.
	 * @throws Throwable On any exception.
	 * @since 2021/11/30
	 */
	public static <T> void main(ApplicationInterface<T> __ai)
		throws NullPointerException, Throwable
	{
		if (__ai == null)
			throw new NullPointerException("NARG");
		
		// We might be on the emulator, so ensure our native interfaces and
		// otherwise are properly loaded
		Poking.poke();
		
		// Setup new instance of the application
		T instance = __ai.<T>newInstance();
		
		// Start the application and perform any potential handling of it
		Throwable throwable = null;
		try
		{
			// Used to settle before checking threads
			long settledNs = System.nanoTime() + ApplicationHandler._SETTLE_NS;
			
			// It is possible that attempting to start the application causes
			// a failure, in which case we want to wrap the exception
			// accordingly
			try
			{
				// Initialize the application
				__ai.<T>startApp(instance);
				
				// Debug
				Debugging.debugNote("Application started normally.");
			}
			catch (Throwable cause)
			{
				throwable = cause;
				
				// Show a noisy banner to make this visible
				System.err.println("****************************************");
				System.err.println("APPLICATION THREW EXCEPTION:");
				
				// Make sure the output is printed
				throwable.printStackTrace(System.err);
				
				// End of banner
				System.err.println("****************************************");
			}
			
			// After termination of the MIDlet wait for threads to settle
			// before checking them
			long lastTime;
			while ((lastTime = System.nanoTime()) < settledNs)
				try
				{
					Thread.sleep((settledNs - lastTime) / 1_000_000L);
				}
				catch (Throwable ignored)
				{
				}
			
			// Although we did start the application, the startApp only
			// ever does initialization and sets some events and otherwise...
			// So actually stop when the alive count goes to zero
			// If the application did start graphics, then there will be
			// a daemon graphics thread which we want to count as well.
			for (int currentCount = -1;;)
			{
				// Get the current thread count with daemon threads
				currentCount = ThreadShelf.aliveThreadCount(
					false, true);
				
				// Stopping?
				if (currentCount <= 0)
				{
					// Note exited
					Debugging.debugNote("Application finished! (%s)",
						currentCount);
					
					break;
				}
				
				// Wait for there to be an update to the thread state before
				// checking again
				ThreadShelf.waitForUpdate(ApplicationHandler._TERM_WAIT_TIME);
			}
			
			// If an exception was thrown then fail here
			if (throwable != null)
				throw throwable;
		}
		finally
		{
			Debugging.debugNote("Cleaning up application...");
			
			// Destroy the instance
			__ai.<T>destroy(instance, throwable);
		}
	}
}
