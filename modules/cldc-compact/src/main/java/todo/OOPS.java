// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.SystemCallIndex;
import java.io.PrintStream;

/**
 * This is an error which is thrown when a condition which should not occur
 * occurs.
 *
 * @since 2018/11/25
 */
public class OOPS
	extends Error
{
	/**
	 * Generates an oops with no message.
	 *
	 * @since 2018/11/25
	 */
	public OOPS()
	{
		this(null, null);
	}
	
	/**
	 * Generates an oops with the given message.
	 *
	 * @param __m The message to use.
	 * @since 2018/11/25
	 */
	public OOPS(String __m)
	{
		this(__m, null);
	}
	
	/**
	 * Generates an oops with the given cause.
	 *
	 * @param __t The cause to use.
	 * @since 2018/11/25
	 */
	public OOPS(Throwable __t)
	{
		this(null, __t);
	}
	
	/**
	 * Generates an oops with the given message and cause.
	 *
	 * @param __m The message to use.
	 * @param __t The cause.
	 * @since 2018/11/25
	 */
	public OOPS(String __m, Throwable __t)
	{
		super(__m, __t);
		
		// Detect OOPSes/TODOs tripping multiple times and fail
		boolean doubletripped = TODO._DOUBLE_TRIP;
		if (doubletripped)
			Assembly.sysCallP(SystemCallIndex.FATAL_TODO);
		TODO._DOUBLE_TRIP = true;
		
		// Print a starting banner, but only if the error stream exists
		PrintStream ps = System.err;
		if (ps != null)
		{
			// Top banner
			ps.println("****************************************************");
			ps.print("OOPS CONDITION WAS MET: ");
			if (__m != null)
				ps.print(__m);
			ps.println();
			
			// Print the current thread
			ps.print("IN THREAD: ");
			ps.println(Thread.currentThread());
			
			// Spacer
			ps.println();
			
			// Print the trace
			this.printStackTrace(ps);
			
			// Ending banner
			ps.println("****************************************************");
		}
		
		// No streams are currently available, but we would still like to
		// report the trace information to the debugger, we might not be in any
		// condition to actually do printing to the console so this will end
		// here
		else
			Assembly.sysCallP(SystemCallIndex.FATAL_TODO);
		
		// {@squirreljme.property
		// cc.squirreljme.nooopsexit=(boolean)
		// If this is {@code true} then the OOPS exception will not tell the
		// virtual machine to exit.}
		if (!Boolean.valueOf(
			System.getProperty("cc.squirreljme.nooopsexit")))
			try
			{
				System.exit(125);
			}
		
			// Ignore
			catch (SecurityException e)
			{
			}
	}
}

