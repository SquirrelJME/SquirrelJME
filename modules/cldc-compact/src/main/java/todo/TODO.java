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

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This exception is thrown.
 *
 * When constructed, this exception does not normall finish execution.
 *
 * @since 2017/02/28
 */
@Deprecated
public class TODO
	extends Error
{
	/**
	 * Initializes the exception, prints the trace, and exits the program.
	 *
	 * @since 2017/02/28
	 */
	@Deprecated
	public TODO()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the exception, prints the trace, and exits the program.
	 *
	 * @param __s Message input.
	 * @since 2018/09/29
	 */
	@Deprecated
	public TODO(String __s)
	{
		throw Debugging.todo(__s);
	}
	
	/**
	 * Specifies that the integer value is missing.
	 *
	 * @return An integer, but is not returned from.
	 * @since 2017/10/27
	 */
	@Deprecated
	public static int missingInteger()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Specifies that the object value is missing.
	 *
	 * @param <T> The object to miss.
	 * @return Should return that object, but never does.
	 * @since 2017/10/24
	 */
	@Deprecated
	public static <T> T missingObject()
	{
		throw Debugging.todo();
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
		Debugging.todoNote(__fmt, __args);
	}
}

