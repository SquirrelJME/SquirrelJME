// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.runtime.cldc.Poking;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class is the main entry point for anything that implements
 * {@link MIDlet}.
 *
 * @since 2020/02/29
 */
final class __MainHandler__
{
	/** One second in milliseconds. */
	private static final int _MS_SECOND =
		1_000;
	
	/** Maximum settle time after starting. */
	private static final long _SETTLE_NS =
		1_000_000_000;
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws Throwable On any exception.
	 * @since 2020/02/29
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// {@squirreljme.error AD02 No main MIDlet class specified.}
		if (__args == null || __args.length < 1 || __args[0] == null)
			throw new IllegalArgumentException("AD02");
		
		// We might be on the emulator
		Poking.poke();
		
		// Debug where we are going in
		Debugging.debugNote("Entering MIDlet: %s", __args[0]);
		
		// Locate the main class before we initialize it
		Class<?> classType;
		try
		{
			classType = Class.forName(__args[0]);
		}
		catch (ClassNotFoundException e)
		{
			// {@squirreljme.error AD03 Could not find main MIDlet. (Class)}
			throw new RuntimeException(String.format(
				"AD03 %s", __args[0]), e);
		}
		
		// Create instance of the MIDlet
		MIDlet instance;
		try
		{
			// Create it
			Object rawInstance = classType.newInstance();
			
			// Catch class casts here because if there is one while the
			// instance is being created it will not be erroneously caught
			try
			{
				instance = (MIDlet)rawInstance;
			}
			catch (ClassCastException e)
			{
				// {@squirreljme.error AD05 Class not a MIDlet.}
				throw new RuntimeException("AD05", e);
			}
		}
		catch (IllegalAccessException|InstantiationException e)
		{
			// {@squirreljme.error AD04 Could not instantiate class.}
			throw new RuntimeException("AD04", e);
		}
		
		// Start the MIDlet and perform any potential handling of it
		try
		{
			// Used to settle before checking threads
			long settledNs = System.nanoTime() + __MainHandler__._SETTLE_NS;
			
			// It is possible that attempting to start the application causes
			// a failure, in which case we want to wrap the exception
			// accordingly
			Throwable throwable = null;
			try
			{
				// Initialize the MIDlet
				instance.startApp();
			}
			catch (Throwable cause)
			{
				throwable = cause;
			}
			
			// After termination of the MIDlet wait for threads to settle
			// before checking them
			while (System.currentTimeMillis() < settledNs)
				try
				{
					Thread.sleep(__MainHandler__._MS_SECOND);
				}
				catch (InterruptedException ignored)
				{
				}
			
			// Although we did start the application, the startApp only
			// ever does initialization and sets some events and otherwise...
			// So actually stop when the alive count goes to zero
			// If the application did start graphics, then there will be
			// a daemon graphics thread which we want to count as well.
			while (ThreadShelf.aliveThreadCount(
				false, true) > 0)
				ThreadShelf.waitForUpdate(__MainHandler__._MS_SECOND);
			
			// If an exception was thrown then fail here
			if (throwable != null)
			{
				// Show a noisy banner to make this visible
				System.err.println("****************************************");
				System.err.println("MIDLET THREW EXCEPTION:");
				
				// Make sure the output is printed
				throwable.printStackTrace(System.err);
				
				// End of banner
				System.err.println("****************************************");
				
				// Toss it
				throw throwable;
			}
		}
		finally
		{
			// Always try to destroy the MIDlet
			try
			{
				instance.destroyApp(true);
			}
			catch (@SuppressWarnings("deprecation")
				MIDletStateChangeException e)
			{
				// Ignore, but still print a trace
				e.printStackTrace(System.err);
			}
			
			// Application is gone now, exit
			instance.notifyDestroyed();
		}
	}
}
