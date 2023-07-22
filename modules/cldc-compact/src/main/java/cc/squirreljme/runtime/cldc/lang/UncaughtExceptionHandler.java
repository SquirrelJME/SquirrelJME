// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This method is called when there has been an uncaught exception being
 * handled.
 *
 * @since 2018/10/29
 */
@SquirrelJMEVendorApi
public final class UncaughtExceptionHandler
{
	/** Exit status for when this is hit. */
	private static final int _EXIT_STATUS =
		62;
	
	/**
	 * Not used.
	 *
	 * @since 2018/10/29
	 */
	private UncaughtExceptionHandler()
	{
	}
	
	/**
	 * Performs some default handling for exceptions that were not caught
	 * anywhere.
	 *
	 * @param __t The throwable to handle.
	 * @since 2018/10/29
	 */
	@SquirrelJMEVendorApi
	public static void handle(Throwable __t)
	{
		// Make sure this does not cause the thread to die again
		try
		{
			Debugging.debugNote(
				"*****************************************");
			Debugging.debugNote("UNCAUGHT EXCEPTION IN THREAD: ");
			
			__t.printStackTrace();
			
			Debugging.debugNote(
				"*****************************************");
		}
		
		// Stop this from failing
		catch (Throwable e)
		{
			// Emit a message to indicate the reason
			Debugging.debugNote("UNCAUGHT DOUBLE-EXCEPTION?");
			Debugging.debugNote("Class: %s", e.getClass().getName());
			Debugging.debugNote("Message: %s", e.getMessage());
		}
	}
}

