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
 * This is an error which is thrown when a condition which should not occur
 * occurs.
 *
 * @since 2018/11/25
 */
public class OOPS
	extends CodeProgressError
{
	/**
	 * Initializes the oops exception.
	 *
	 * @since 2020/03/15
	 */
	@Deprecated
	public OOPS()
	{
		this(null);
	}
	
	/**
	 * Initializes the oops exception.
	 *
	 * @param __m The message.
	 * @since 2020/03/15
	 */
	@SuppressWarnings("ThrowableNotThrown")
	@Deprecated
	public OOPS(String __m)
	{
		super(__m);
		
		OOPS.__oops(true, CallTraceElement.traceRaw(), __m);
	}
	
	/**
	 * Constructor which does not call the static handler.
	 *
	 * @param __ignore Not used.
	 * @param __m The message.
	 * @since 2020/03/15
	 */
	OOPS(@SuppressWarnings("unused") boolean __ignore, String __m)
	{
		super(__m);
	}
	
	/**
	 * Handles the OOPS logic and returns OOPS exception.
	 *
	 * @param __args The arguments to the OOPS.
	 * @return The resultant exception.
	 * @since 2020/03/15
	 */
	@SuppressWarnings({"NewMethodNamingConvention",
		"MethodNameSameAsClassName"})
	public static OOPS OOPS(Object... __args)
	{
		return OOPS.__oops(false, CallTraceElement.traceRaw(),
			__args);
	}
	
	/**
	 * Handles the OOPS logic and returns OOPS exception.
	 *
	 * @param __fromInit From initializer?
	 * @param __trace The trace.
	 * @param __args The arguments to the OOPS.
	 * @return The resultant exception.
	 * @since 2020/03/15
	 */
	static OOPS __oops(boolean __fromInit, int[] __trace, Object... __args)
	{
		// Perform printing logic for this trace
		__Utilities__.dumpTrace('O', 'P', __trace, __args);
			
		// Exit on oops?
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
		return new OOPS(false, Arrays.asList(__args).toString());
	}
}

