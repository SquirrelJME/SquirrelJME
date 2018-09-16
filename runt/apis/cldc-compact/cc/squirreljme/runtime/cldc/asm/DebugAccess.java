// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.debug.CallTraceElement;

/**
 * This is used to provide debug access to the virtual machine and to do
 * some unsafe things.
 *
 * @since 2018/09/16
 */
public final class DebugAccess
{
	/** The elements per trace. */
	public static final int TRACE_COUNT =
		11;
	
	/**
	 * Not used.
	 *
	 * @since 2018/09/16
	 */
	private DebugAccess()
	{
	}
	
	/**
	 * Returns the raw call trace without any objects.
	 *
	 * The values are in groups of longs for each individual element:
	 *  0,1. Pointer to class.
	 *  2,3. Pointer to string, specifying the method.
	 *  4,5. Pointer to string, specifying the method descriptor.
	 *  6,7. The address of the program counter if it is possible to get.
	 *  8,9. Pointer to string, specifying the file if it is possible to get.
	 *  10 . The line of code in the file this is in, if it is possible to get.
	 *
	 * If any value is unknown then {@code -1} will be used as the value.
	 *
	 * @return The raw call trace in pointer and value format.
	 * @since 2018/09/16
	 */
	public static final native int[] rawCallTrace();
	
	/**
	 * Returns the current call trace in wrapped special types.
	 *
	 * @return The current call trace.
	 * @since 2018/09/16
	 */
	public static final CallTraceElement[] callTrace()
	{
		return DebugAccess.resolveRawCallTrace(DebugAccess.rawCallTrace());
	}
	
	/**
	 * This translates a raw call trace into a wrapped call trace to make it
	 * simpler to easier to parse.
	 *
	 * @param __v The input raw call trace.
	 * @return The translated call trace.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	public static final CallTraceElement[] resolveRawCallTrace(int[] __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

