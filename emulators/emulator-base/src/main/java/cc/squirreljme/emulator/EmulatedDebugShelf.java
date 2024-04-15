// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.DebugShelf;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;

/**
 * Emulates {@link DebugShelf} for Java SE.
 *
 * @since 2023/07/19
 */
public class EmulatedDebugShelf
{
	
	/**
	 * If there is a debugger attached, then this will emit a breakpoint to
	 * halt execution accordingly. This might not be supported by all
	 * SquirrelJME implementations.
	 *
	 * @since 2024/01/30
	 */
	@SquirrelJMEVendorApi
	public static void breakpoint()
	{
		try
		{
			throw new __PseudoBreakpoint__();
		}
		catch (__PseudoBreakpoint__ __ignored)
		{
			// Ignore
		}
	}
	
	/**
	 * Resolves the class from the given point.
	 *
	 * @param __point The point to resolve.
	 * @return The class.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	public static String pointClass(@NotNull TracePointBracket __point)
	{
		if (__point == null)
			throw new MLECallError("Null arguments.");
		
		return ((EmulatedTracePointBracket)__point).element.getClassName();
	}
	
	/**
	 * Traces the entire stack, the top-most trace point in the stack is always
	 * first.
	 *
	 * @return The stack trace.
	 * @since 2023/07/19
	 */
	@SquirrelJMEVendorApi
	public static TracePointBracket[] traceStack()
	{
		StackTraceElement[] trace = new Throwable().getStackTrace();
		
		// We need to remove ourselves from the top accordingly
		int n = trace.length;
		int off = 1;
		TracePointBracket[] result = new TracePointBracket[n - off];
		for (int i = off; i < n; i++)
			result[i - off] = new EmulatedTracePointBracket(trace[i]);
		
		return result;
	}
}
