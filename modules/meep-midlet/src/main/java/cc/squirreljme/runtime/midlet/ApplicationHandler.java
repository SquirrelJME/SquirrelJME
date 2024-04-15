// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.midlet;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.midlet.MIDlet;

/**
 * This handles the main starting loop and otherwise for applications, it is
 * used in conjunction with {@link ApplicationInterface}.
 *
 * @see ApplicationInterface
 * @since 2021/11/30
 */
@SquirrelJMEVendorApi
public final class ApplicationHandler
{
	/** Undefined application name. */
	public static final String UNDEFINED_NAME =
		"UndefinedName";
	
	/** The current application interface. */
	private static volatile ApplicationInterface<?> _CURRENT_INTERFACE;
	
	/** The current application instance. */
	private static volatile Object _CURRENT_INSTANCE;
	
	/** The current vendor. */
	private static String _CURRENT_VENDOR;
	
	/** The current name. */
	private static String _CURRENT_NAME;
	
	/** One second in milliseconds. */
	private static final int _TERM_WAIT_TIME =
		30_000;
	
	/** Maximum settle time after starting. */
	private static final long _SETTLE_NS =
		2_000_000_000;
	
	/**
	 * Returns the current application interface.
	 * 
	 * @return The current application interface.
	 * @since 2022/02/14
	 */
	@SquirrelJMEVendorApi
	public static ApplicationInterface<?> currentInterface()
	{
		return ApplicationHandler._CURRENT_INTERFACE;
	}
	
	/**
	 * Returns the current application instance.
	 * 
	 * @return The current application instance.
	 * @since 2022/02/14
	 */
	@SquirrelJMEVendorApi
	public static Object currentInstance()
	{
		return ApplicationHandler._CURRENT_INSTANCE;
	}
	
	/**
	 * Returns the current name.
	 *
	 * @return The current name.
	 * @since 2019/04/14
	 */
	@SquirrelJMEVendorApi
	public static String currentName()
	{
		String rv;
		synchronized (ApplicationHandler.class)
		{
			rv = ApplicationHandler._CURRENT_NAME;
			if (rv != null)
				return rv;
		}
		
		// TODO: Better means of getting the current name
		Debugging.todoNote("Better means of currentName()");
		
		// Try through the current MIDlet properties
		if (rv == null)
		{
			MIDlet mid = ActiveMidlet.optional();
			if (mid != null)
				rv = mid.getAppProperty("MIDlet-Name");
		}
		
		// Fallback
		if (rv == null)
			rv = ApplicationHandler.UNDEFINED_NAME;
		
		// Cache and return
		synchronized (ApplicationHandler.class)
		{
			ApplicationHandler._CURRENT_NAME = rv;
			return rv;
		}
	}
	
	/**
	 * Returns the current vendor.
	 *
	 * @return The current vendor.
	 * @since 2019/04/14
	 */
	@SquirrelJMEVendorApi
	public static String currentVendor()
	{
		String rv;
		synchronized (ApplicationHandler.class)
		{
			rv = ApplicationHandler._CURRENT_VENDOR;
			if (rv != null)
				return rv;
		}
		
		// TODO: Better means of getting the current name
		Debugging.todoNote("Better means of currentVendor()");
		
		// Try through the current MIDlet properties
		if (rv == null)
		{
			MIDlet mid = ActiveMidlet.optional();
			if (mid != null)
				rv = mid.getAppProperty("MIDlet-Vendor");
		}
		
		// Fallback
		if (rv == null)
			rv = "UndefinedVendor";
		
		// Cache and return
		synchronized (ApplicationHandler.class)
		{
			ApplicationHandler._CURRENT_VENDOR = rv;
			return rv;
		}
	}
	
	/**
	 * Handles the main application handling and logic.
	 *
	 * @param <T> The type of instance used.
	 * @param __ai The interface to the program.
	 * @throws NullPointerException on null arguments.
	 * @throws Throwable On any exception.
	 * @since 2021/11/30
	 */
	@SquirrelJMEVendorApi
	public static <T> void main(ApplicationInterface<T> __ai)
		throws NullPointerException, Throwable
	{
		if (__ai == null)
			throw new NullPointerException("NARG");
		
		// Setup new instance of the application
		T instance = __ai.<T>newInstance();
		
		// Store current application that is being used
		ApplicationHandler._CURRENT_INTERFACE = __ai;
		ApplicationHandler._CURRENT_INSTANCE = instance;
		
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
	
	/**
	 * Forces the set of the suite name and vendor.
	 * 
	 * @param __name The name to set.
	 * @param __vend The vendor to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/02
	 */
	@SquirrelJMEVendorApi
	public static void setNameAndVendor(String __name, String __vend)
		throws NullPointerException
	{
		if (__name == null || __vend == null)
			throw new NullPointerException("NARG");
		
		synchronized (ApplicationHandler.class)
		{
			ApplicationHandler._CURRENT_NAME = __name;
			ApplicationHandler._CURRENT_VENDOR = __vend;
		}
	}
}
