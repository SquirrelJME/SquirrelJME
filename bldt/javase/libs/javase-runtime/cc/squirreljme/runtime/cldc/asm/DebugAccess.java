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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This is used to provide debug access to the virtual machine and to do
 * some unsafe things. This file is for the Java SE bootstrap since it does not
 * know what SquirrelJME does in its library.
 *
 * @since 2018/09/29
 */
public final class DebugAccess
{
	/** The elements per trace. */
	public static final int TRACE_COUNT =
		11;
	
	/** Lock. */
	private static final Object _LOCK =
		new Object();
	
	/** Long to string map. */
	private static final Map<Long, String> _LONG_TO_STRING =
		new HashMap<>();
	
	/** String to long map. */
	private static final Map<String, Long> _STRING_TO_LONG =
		new HashMap<>();
	
	/** The next long to choose. */
	private static long _nextlong;
	
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
		new Error(Arrays.toString(__rct)).printStackTrace(System.err);
		System.exit(-1);
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
		// Get origin trace
		StackTraceElement[] from = new Throwable().getStackTrace();
		int n = from.length;
		
		// Convert to call trace elements
		CallTraceElement[] elems = new CallTraceElement[n];
		for (int i = 0; i < n; i++)
			elems[i] = DebugAccess.__stackJavaToDebug(from[i]);
		
		// Snip top stack entries off accordingly
		int elemlen = elems.length,
			baseoff = 0;
		while (baseoff < elemlen)
		{
			CallTraceElement e = elems[baseoff];
			if (e == null)
				continue;
			
			String clname = e.className();
			if (clname != null &&
				(clname.startsWith("cc/squirreljme/runtime/cldc/asm/") ||
				clname.startsWith("cc.squirreljme.runtime.cldc.asm.")))
				baseoff++;
			else
				break;
		}
		
		// Convert to raw entries
		int[] rv = new int[TRACE_COUNT * (elemlen - baseoff)];
		for (int i = baseoff, o = 0, dn = elemlen; i < dn;
			i++, o += TRACE_COUNT)
		{
			CallTraceElement e = elems[i];
			
			DebugAccess.__longToInt(o + 0, rv,
				DebugAccess.__unresolveString(
					DebugAccess.__normalize(e.className())));
			DebugAccess.__longToInt(o + 2, rv,
				DebugAccess.__unresolveString(e.methodName()));
			DebugAccess.__longToInt(o + 4, rv,
				DebugAccess.__unresolveString(e.methodDescriptor()));
			DebugAccess.__longToInt(o + 6, rv,
				e.address());
			DebugAccess.__longToInt(o + 8, rv,
				DebugAccess.__unresolveString(e.file()));
			rv[o + 10] = e.line();
		}
		
		return rv;
	}
	
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
		
		int rawlen = __v.length,
			numframes = rawlen / TRACE_COUNT;
		
		CallTraceElement[] rv = new CallTraceElement[numframes];
		
		// Convert frames
		for (int o = 0, i = 0; o < numframes; o++, i += TRACE_COUNT)
			rv[o] = new CallTraceElement(
				DebugAccess.resolveString(DebugAccess.__intToLong(i + 0, __v)),
				DebugAccess.resolveString(DebugAccess.__intToLong(i + 2, __v)),
				DebugAccess.resolveString(DebugAccess.__intToLong(i + 4, __v)),
				DebugAccess.__intToLong(i + 6, __v),
				DebugAccess.resolveString(DebugAccess.__intToLong(i + 8, __v)),
				__v[i + 10]);
		
		return rv;
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
		if (__p == -1L)
			return null;
		
		synchronized (DebugAccess._LOCK)
		{
			return DebugAccess._LONG_TO_STRING.get(__p);
		}
	}
	
	/**
	 * Combines an integer to long.
	 *
	 * @param __dx The index.
	 * @param __v The input integers.
	 * @return The resulting long.
	 * @since 2018/09/29
	 */
	static final long __intToLong(int __dx, int[] __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		return (((long)__v[__dx]) << 32L) |
			(((long)__v[__dx + 1]) & 0xFFFFFFFFL);
	}
	
	/**
	 * Splits long to integers.
	 *
	 * @param __dx The index.
	 * @param __v The output integers.
	 * @param __l The input long.
	 * @since 2018/09/29
	 */
	static final void __longToInt(int __dx, int[] __v, long __l)
	{
		__v[__dx] = (int)(__l >>> 32);
		__v[__dx + 1] = (int)__l;
	}
	
	/**
	 * Normalize class name string.
	 *
	 * @param __s The input string.
	 * @return The normalized string.
	 * @since 2018/09/29
	 */
	static final String __normalize(String __s)
	{
		if (__s == null)
			return null;
		
		return __s.replace('.', '/');
	}
	
	/**
	 * Converts a debug call trace element to a stack trace element.
	 *
	 * @param __e The input element.
	 * @return The converted element.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	static StackTraceElement __stackDebugToJava(CallTraceElement __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		String clname = __e.className();
		if (clname != null)
			clname = clname.replace('/', '.');
		
		return new StackTraceElement(clname, __e.methodName(),
			__e.file(), __e.line());
	}
	
	/**
	 * Converts a stack trace element to a debug call trace element.
	 *
	 * @param __e The input element.
	 * @return The converted element.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	static CallTraceElement __stackJavaToDebug(StackTraceElement __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		String clname = __e.getClassName();
		if (clname != null)
			clname = clname.replace('.', '/');
		
		return new CallTraceElement(clname, __e.getMethodName(),
			null, -1, __e.getFileName(), __e.getLineNumber());
	}
	
	/**
	 * Unresolves the given string.
	 *
	 * @param __s The string to unresolve.
	 * @return The pointer to the string.
	 * @since 2018/09/29
	 */
	static long __unresolveString(String __s)
	{
		if (__s == null)
			return -1L;
		
		synchronized (DebugAccess._LOCK)
		{
			Long rv = DebugAccess._STRING_TO_LONG.get(__s);
			if (rv != null)
				return rv.longValue();
			
			Long next = Long.valueOf(++DebugAccess._nextlong);
			DebugAccess._STRING_TO_LONG.put(__s, next);
			DebugAccess._LONG_TO_STRING.put(next, __s);
			
			return next;
		}
	}
}

