// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import java.io.PrintStream;

/**
 * This method is called when there has been an uncaught exception being
 * handled.
 *
 * @since 2018/10/29
 */
public final class UncaughtExceptionHandler
{
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
	public static final void handle(Throwable __t)
	{
		// Make a nice and big banner on it
		PrintStream out = System.err;
		out.println("****************************************************");
		out.println("UNCAUGHT EXCEPTION:");
		
		if (__t != null)
			__t.printStackTrace(out);
		
		out.println("****************************************************");
		
		// Check if this is the only running thread, if it is then exit the
		// VM with some error status
		throw new todo.TODO();
	}
}

