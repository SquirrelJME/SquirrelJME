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

import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.runtime.cldc.system.SystemCall;
import java.io.PrintStream;

/**
 * This exception is thrown.
 *
 * When constructed, this exception does not normall finish execution.
 *
 * @since 2017/02/28
 */
public class TODO
	extends Error
{
	/**
	 * Initializes the exception, prints the trace, and exits the program.
	 *
	 * @since 2017/02/28
	 */
	public TODO()
	{
		// Print a starting banner
		PrintStream ps = System.err;
		ps.println("*******************************************************");
		ps.println("INCOMPLETE CODE HAS BEEN REACHED:");
		
		// Print the trace
		printStackTrace(ps);
		
		// Ending banner
		ps.println("*******************************************************");
		
		// {@squirreljme.property
		// cc.squirreljme.notodoexit=(boolean)
		// If this is {@code true} then the ToDo exception will not tell the
		// virtual machine to exit.}
		if (!Boolean.valueOf(
			System.getProperty("cc.squirreljme.notodoexit")))
			try
			{
				System.exit(3);
			}
		
			// Ignore
			catch (SecurityException e)
			{
			}
	}
	
	/**
	 * Specifies that the integer value is missing.
	 *
	 * @return An integer, but is not returned from.
	 * @since 2017/10/27
	 */
	public static final int missingInteger()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Specifies that the object value is missing.
	 *
	 * @param <T> The object to miss.
	 * @return Should return that object, but never does.
	 * @since 2017/10/24
	 */
	public static final <T> T missingObject()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Prints a note to standard error about something that is incomplete.
	 *
	 * @param __fmt The format string.
	 * @param __args The arguments to the call.
	 * @since 2018/04/02
	 */
	public static final void note(String __fmt, Object... __args)
	{
		// Determine where it came from
		CallTraceElement[] elems = SystemCall.EASY.throwableGetStack(
			new Throwable());
		CallTraceElement elem;
		int n = elems.length;
		if (n > 1)
			elem = elems[1];
		else if (n > 0)
			elem = elems[0];
		else
			elem = new CallTraceElement();
		
		// Print it out
		PrintStream ps = System.err;
		ps.print("TODO -- ");
		ps.printf("%s::%s %s @ 0x%X (%s:%d)", elem.className(),
			elem.methodName(), elem.methodDescriptor(), elem.address(),
			elem.file(), elem.line());
		ps.print(" -- ");
		ps.printf(__fmt, __args);
		ps.println();
	}
}

