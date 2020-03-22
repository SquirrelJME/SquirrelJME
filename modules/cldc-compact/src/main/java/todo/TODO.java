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
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import java.util.Arrays;

/**
 * This exception is thrown.
 *
 * When constructed, this exception does not normall finish execution.
 *
 * @since 2017/02/28
 */
@Deprecated
public class TODO
	extends CodeProgressError
{
	/**
	 * Initializes the exception, prints the trace, and exits the program.
	 *
	 * @since 2017/02/28
	 */
	@Deprecated
	public TODO()
	{
		this(null);
	}
	
	/**
	 * Initializes the to-do exception.
	 *
	 * @param __m The message.
	 * @since 2020/03/15
	 */
	@SuppressWarnings("ThrowableNotThrown")
	@Deprecated
	public TODO(String __m)
	{
		super(__m);
		
		TODO.__todo(true, CallTraceElement.traceRaw(), __m);
	}
	
	/**
	 * Constructor which does not call the static handler.
	 *
	 * @param __ignore Not used.
	 * @param __m The message.
	 * @since 2020/03/15
	 */
	@Deprecated
	TODO(@SuppressWarnings("unused") boolean __ignore, String __m)
	{
		super(__m);
	}
	
	/**
	 * Specifies that the integer value is missing.
	 *
	 * @return An integer, but is not returned from.
	 * @since 2017/10/27
	 */
	@Deprecated
	@SuppressWarnings("ThrowableNotThrown")
	public static int missingInteger()
	{
		TODO.__todo(false, CallTraceElement.traceRaw());
		return 0;
	}
	
	/**
	 * Specifies that the object value is missing.
	 *
	 * @param <T> The object to miss.
	 * @return Should return that object, but never does.
	 * @since 2017/10/24
	 */
	@Deprecated
	@SuppressWarnings("ThrowableNotThrown")
	public static <T> T missingObject()
	{
		TODO.__todo(false, CallTraceElement.traceRaw());
		return null;
	}
	
	/**
	 * Prints a note to standard error about something that is incomplete.
	 *
	 * @param __fmt The format string.
	 * @param __args The arguments to the call.
	 * @since 2018/04/02
	 */
	@Deprecated
	public static void note(String __fmt, Object... __args)
	{
		__Utilities__.dumpFormatLine('T', 'O', __fmt, __args);
	}
	
	/**
	 * Handles the To-Do logic and returns To-Do exception.
	 *
	 * @param __args The arguments to the To-Do.
	 * @return The resultant exception.
	 * @since 2020/03/15
	 */
	@Deprecated
	@SuppressWarnings({"NewMethodNamingConvention",
		"MethodNameSameAsClassName"})
	public static TODO TODO(Object... __args)
	{
		return TODO.__todo(false, CallTraceElement.traceRaw(),
			__args);
	}
	
	/**
	 * Handles the To-Do logic and returns To-Do exception.
	 *
	 * @param __fromInit From initializer?
	 * @param __trace The stack trace.
	 * @param __args The arguments to the To-Do.
	 * @return The resultant exception.
	 * @since 2020/03/15
	 */
	@Deprecated
	static TODO __todo(boolean __fromInit, int[] __trace, Object... __args)
	{
		// Perform printing logic for this trace
		__Utilities__.dumpTrace('T', 'D', __trace, __args);
			
		// Exit on to-do?
		if ((Assembly.sysCallPV(SystemCallIndex.DEBUG_FLAGS) &
			Constants.DEBUG_NO_TODO_EXIT) != 0)
			try
			{
				System.exit(126);
			}
			catch (SecurityException e)
			{
				// Could not use normal exit, so try this method
				Assembly.sysCallP(SystemCallIndex.FATAL_TODO);
			}
		
		// Build exception if not from an initializer
		if (__fromInit)
			return null;
		return new TODO(false, Arrays.asList(__args).toString());
	}
}

