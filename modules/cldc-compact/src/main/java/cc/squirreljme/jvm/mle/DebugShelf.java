// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.jvm.mle.constants.VerboseDebugFlag;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.TestOnly;

/**
 * This is the shelf used for accessing the debugging features of SquirrelJME
 * along with miscellaneous debugging utilities.
 *
 * @since 2020/06/11
 */
@SquirrelJMEVendorApi
public final class DebugShelf
{
	/** Verbose ID for internal threads. */
	@SquirrelJMEVendorApi
	public static final int INTERNAL_THREAD_VERBOSE_ID =
		Integer.MIN_VALUE;
	
	/**
	 * Not used.
	 *
	 * @since 2020/06/11
	 */
	private DebugShelf()
	{
	}
	
	/**
	 * If there is a debugger attached, then this will emit a breakpoint to
	 * halt execution accordingly. This might not be supported by all
	 * SquirrelJME implementations.
	 *
	 * @since 2024/01/30
	 */
	@SquirrelJMEVendorApi
	public static native void breakpoint();
	
	/**
	 * Returns the trace that is part of the given throwable.
	 *
	 * @param __t The throwable to gets it's trace of.
	 * @return The trace that was within the given throwable.
	 * @since 2020/06/11
	 */
	@SquirrelJMEVendorApi
	public static native TracePointBracket[] getThrowableTrace(
		@NotNull Throwable __t);
	
	/**
	 * Resolves the address from the given point.
	 *
	 * @param __point The point to resolve.
	 * @return The address.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@Range(from = -1, to = Long.MAX_VALUE)
	public static native long pointAddress(@NotNull TracePointBracket __point);
	
	/**
	 * Resolves the class from the given point.
	 *
	 * @param __point The point to resolve.
	 * @return The class.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@Nullable
	public static native String pointClass(@NotNull TracePointBracket __point);
	
	/**
	 * Resolves the file from the given point.
	 *
	 * @param __point The point to resolve.
	 * @return The file.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@Nullable
	public static native String pointFile(@NotNull TracePointBracket __point);
	
	/**
	 * Resolves the Java address from the given point.
	 *
	 * @param __point The point to resolve.
	 * @return The Java address.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@Range(from = -1, to = Long.MAX_VALUE)
	public static native int pointJavaAddress(
		@NotNull TracePointBracket __point);
	
	/**
	 * Resolves the Java operation from the given point.
	 *
	 * @param __point The point to resolve.
	 * @return The Java operation.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@Range(from = -1, to = 255)
	public static native int pointJavaOperation(
		@NotNull TracePointBracket __point);
	
	/**
	 * Resolves the line from the given point.
	 *
	 * @param __point The point to resolve.
	 * @return The line.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@Range(from = -1, to = Integer.MAX_VALUE)
	public static native int pointLine(@NotNull TracePointBracket __point);
	
	/**
	 * Resolves the method name from the given point.
	 *
	 * @param __point The point to resolve.
	 * @return The method name.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@Nullable
	public static native String pointMethodName(
		@NotNull TracePointBracket __point);
	
	/**
	 * Resolves the method type from the given point.
	 *
	 * @param __point The point to resolve.
	 * @return The method type.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	@Nullable
	public static native String pointMethodType(
		@NotNull TracePointBracket __point);
	
	/**
	 * Traces the entire stack, the top-most trace point in the stack is always
	 * first.
	 *
	 * @return The stack trace.
	 * @since 2020/06/11
	 */
	@SquirrelJMEVendorApi
	public static native TracePointBracket[] traceStack();
	
	/**
	 * Starts performing verbose virtual machine outputs for debugging.
	 * 
	 * This method may or may not have an actual effect.
	 * 
	 * When the calling method exits, verbosity should be terminated although
	 * it is not required. This is so that outer calls do not cause verbosity
	 * to happen.
	 * 
	 * @param __flags The {@link VerboseDebugFlag}s.
	 * @return An integer to be passed to {@link DebugShelf#verboseStop(int)}.
	 * @since 2020/07/11
	 */
	@SquirrelJMEVendorApi
	@TestOnly
	public static native int verbose(
		@MagicConstant(flagsFromClass = VerboseDebugFlag.class) int __flags);
	
	/**
	 * Similar to {@link #verbose(int)} this will apply verbose flags to
	 * threads that are created by the virtual machine internally such as
	 * for UI callbacks and otherwise. Note that this there is only a single
	 * state which affects all internally created threads, as such if this is
	 * set again the previous verbosity will be removed. Additionally, it will
	 * affect the entire state similarly to
	 * {@link VerboseDebugFlag#INHERIT_VERBOSE_FLAGS}.
	 * 
	 * This method may or may not have an actual effect.
	 * 
	 * @param __flags The {@link VerboseDebugFlag}s.
	 * @return Always returns {@link #INTERNAL_THREAD_VERBOSE_ID} which
	 * can be passed to {@link DebugShelf#verboseStop(int)}.
	 * @since 2022/06/12
	 */
	@SquirrelJMEVendorApi
	@TestOnly
	public static native int verboseInternalThread(
		@MagicConstant(flagsFromClass = VerboseDebugFlag.class) int __flags);
	
	/**
	 * Stops performing verbosity output.
	 * 
	 * If {@link #INTERNAL_THREAD_VERBOSE_ID} is used, this may only take
	 * effect for newly created internal threads.
	 * 
	 * @param __code The previous verbosity state, or
	 * {@link #INTERNAL_THREAD_VERBOSE_ID} to remove verbosity on internal
	 * threads.
	 * @since 2020/07/11
	 */
	@SquirrelJMEVendorApi
	@TestOnly
	public static native void verboseStop(int __code);
}
