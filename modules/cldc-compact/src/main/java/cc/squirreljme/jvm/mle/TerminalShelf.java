// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import cc.squirreljme.jvm.mle.constants.PipeErrorType;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This contains the shell for printing to the console and otherwise.
 *
 * @since 2020/06/14
 */
@SuppressWarnings("UnstableApiUsage")
@SquirrelJMEVendorApi
public final class TerminalShelf
{
	/**
	 * Not used.
	 *
	 * @since 2002/06/14
	 */
	private TerminalShelf()
	{
	}
	
	/**
	 * Returns the number of available bytes for reading, if it is known.
	 * 
	 * @param __fd The descriptor to close.
	 * @return The number of bytes ready for immediate reading, will be
	 * zero if there are none. For errors one of {@link PipeErrorType}.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2020/11/22
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = PipeErrorType.class,
		intValues = {0, -1})
	@NonBlocking
	@CheckReturnValue
	public static native int available(@NotNull PipeBracket __fd)
		throws MLECallError;
	
	/**
	 * Closes the output of the current process.
	 * 
	 * @param __fd The pipe to close.
	 * @return One of {@link PipeErrorType}.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2020/07/02
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = PipeErrorType.class)
	@Blocking
	@CheckReturnValue
	public static native int close(@NotNull PipeBracket __fd)
		throws MLECallError;
	
	/**
	 * Flushes the stream.
	 *
	 * @param __fd The pipe to flush.
	 * @return One of {@link PipeErrorType}.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2018/12/08
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = PipeErrorType.class)
	@Blocking
	@CheckReturnValue
	public static native int flush(@NotNull PipeBracket __fd)
		throws MLECallError;
	
	/**
	 * Returns the pipe to a standardized input/output pipe that is shared
	 * across many systems.
	 * 
	 * @param __fd The {@link StandardPipeType} to get the pipe of.
	 * @return The pipe to the given pipe.
	 * @throws MLECallError If the standard pipe does not exist or is not
	 * valid.
	 * @since 2022/03/19
	 */
	@SquirrelJMEVendorApi
	public static native PipeBracket fromStandard(
		@MagicConstant(valuesFromClass = StandardPipeType.class) int __fd)
		throws MLECallError;
	
	/**
	 * Reads from the given pipe into the output buffer.
	 *
	 * @param __fd The pipe to read from.
	 * @param __b The bytes to read into.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return One of {@link PipeErrorType} or the number of read bytes.
	 * @throws MLECallError If {@code __fd} is not valid, the offset and/or
	 * length are negative or exceed the buffer size, or {@code __b} is
	 * {@code null}.
	 * @since 2018/12/05
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = PipeErrorType.class)
	@Range(from = -2, to = Integer.MAX_VALUE)
	@Blocking
	@CheckReturnValue
	public static native int read(@NotNull PipeBracket __fd,
		@NotNull byte[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l)
		throws MLECallError;
	
	/**
	 * Writes the character to the console output.
	 *
	 * @param __fd The pipe to write to.
	 * @param __c The byte to write, only the lowest 8-bits are used.
	 * @return One of {@link PipeErrorType} or {@code 1} on success.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2018/09/21
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = PipeErrorType.class,
		intValues = {1})
	@Range(from = -2, to = 1)
	@Blocking
	@CheckReturnValue
	public static native int write(@NotNull PipeBracket __fd,
		@Range(from = 0, to = 255) int __c)
		throws MLECallError;
	
	/**
	 * Writes the given bytes to the console output.
	 *
	 * @param __fd The pipe to write to.
	 * @param __b The bytes to write.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return One of {@link PipeErrorType} or {@code __l} on success.
	 * @throws MLECallError If {@code __fd} is not valid, the offset and/or
	 * length are negative or exceed the buffer size, or {@code __b} is
	 * {@code null}.
	 * @since 2018/12/05
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = PipeErrorType.class,
		intValues = {1})
	@Range(from = -2, to = Integer.MAX_VALUE)
	@Blocking
	@CheckReturnValue
	public static native int write(@NotNull PipeBracket __fd,
		@NotNull byte[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l)
		throws MLECallError;
}
