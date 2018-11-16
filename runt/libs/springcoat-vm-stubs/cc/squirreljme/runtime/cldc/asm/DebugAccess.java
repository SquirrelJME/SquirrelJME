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
	 * Reports that the given raw trace information using some given means,
	 * this is to detect early TODOs before there is any real code that exists.
	 * Since the code might end up infinite looping while constantly hitting
	 * TODOs, this is a means it might not be reported properly. This call
	 * should be fatal and terminate the machine.
	 *
	 * @param __rct The raw trace code.
	 * @since 2018/09/19
	 */
	public static final void fatalTodoReport(int[] __rct)
	{
		System.err.print("[");
		for (int i = 0, n = __rct.length; i < n; i++)
			System.err.printf("%08x ", __rct[i]);
		System.err.println("]");
		System.exit(127);
	}
	
	/**
	 * Returns the raw call trace without any objects.
	 *
	 * The values are in groups of longs for each individual element, dual
	 * elements are in {@code [high, low]} order:
	 *  0,1. Pointer to string, specifying the class.
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
	public static final int[] rawCallTrace()
	{
		return new int[0];
	}
	
	/**
	 * Resolves the given string pointer.
	 *
	 * @param __p The pointer.
	 * @return The string at the given pointer or {@code null} if it has no
	 * resolution.
	 * @since 2018/09/29
	 */
	public static final String resolveString(long __p)
	{
		return null;
	}
	
	/**
	 * Unresolves the string to the given pointer.
	 *
	 * @param __s The string to resolve.
	 * @return The pointer to the string, or {@code -1} if it is not valid.
	 * @since 2018/09/29
	 */
	public static final long unresolveString(String __s)
	{
		return -1;
	}
	
	/**
	 * Combines an integer to long.
	 *
	 * @param __dx The index.
	 * @param __v The input integers.
	 * @return The resulting long.
	 * @since 2018/09/29
	 */
	public static final long intToLong(int __dx, int[] __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		return (((long)__v[__dx]) << 32L) |
			(((long)__v[__dx + 1]) & 0xFFFFFFFFL);
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
		
		int rawlen = __v.length,
			numframes = rawlen / TRACE_COUNT;
		
		CallTraceElement[] rv = new CallTraceElement[numframes];
		
		// Convert frames
		for (int o = 0, i = 0; o < numframes; o++, i += TRACE_COUNT)
			rv[o] = new CallTraceElement(
				DebugAccess.resolveString(DebugAccess.intToLong(i + 0, __v)),
				DebugAccess.resolveString(DebugAccess.intToLong(i + 2, __v)),
				DebugAccess.resolveString(DebugAccess.intToLong(i + 4, __v)),
				DebugAccess.intToLong(i + 6, __v),
				DebugAccess.resolveString(DebugAccess.intToLong(i + 8, __v)),
				__v[i + 10]);
		
		return rv;
	}
}

