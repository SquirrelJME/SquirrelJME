// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.jvm.mle.constants.PipeErrorType;
import cc.squirreljme.jvm.mle.constants.StandardBusIds;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
import cc.squirreljme.jvm.mle.constants.TaskStatusType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.Closeable;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * This shelf allows for the management of tasks and otherwise.
 *
 * @since 2020/07/02
 */
@SuppressWarnings("UnstableApiUsage")
@SquirrelJMEVendorApi
public final class TaskShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/07/02
	 */
	private TaskShelf()
	{
	}
	
	/**
	 * Returns the tasks which are active.
	 * 
	 * @return The active tasks.
	 * @since 2020/07/09
	 */
	@SquirrelJMEVendorApi
	public static native TaskBracket[] active();
	
	/**
	 * Receives the next message from the SquirrelJME bus. 
	 *
	 * @param __from The task that this message is from.
	 * @param __busId The channel to read from.
	 * @param __blocking Block until the next message occurs?
	 * @param __data The data to send.
	 * @param __off The offset into the data.
	 * @param __len The length of the data.
	 * @return The number of bytes for the message, if this value is negative
	 * it means the buffer is too small to receive the data. The negative size
	 * will be the number of bytes required as a negative value. If there
	 * is no message or this was interrupted, this will
	 * return {@link Integer#MIN_VALUE}.
	 * @throws MLECallError If the data could not be received; on null
	 * arguments or if the offset and/or length are invalid or exceed the
	 * array bounds.
	 * @since 2024/08/13
	 */
	@SquirrelJMEVendorApi 
	@CheckReturnValue
	public static native int busReceive(@NotNull TaskBracket[] __from,
		@MagicConstant(valuesFromClass = StandardBusIds.class) int __busId,
		boolean __blocking,
		@NotNull byte[] __data,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws MLECallError;
	
	/**
	 * Transmits data over the SquirrelJME bus.
	 *
	 * @param __to The target task, if {@code null} this is a broadcast
	 * message to every task.
	 * @param __busId The bus channel identifier, may be one of
	 * the IDs from {@link StandardBusIds}.
	 * @param __data The data to send.
	 * @param __off The offset into the data.
	 * @param __len The length of the data.
	 * @throws MLECallError If the data could not be sent; or if the offset
	 * and/or length are invalid or exceed the array bounds.
	 * @since 2024/08/13
	 */
	@SquirrelJMEVendorApi
	public static native void busSend(@Nullable TaskBracket __to,
		@MagicConstant(valuesFromClass = StandardBusIds.class) int __busId,
		@NotNull byte[] __data,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws MLECallError;
	
	/**
	 * Returns the current task.
	 * 
	 * @return The current task.
	 * @since 2020/07/02
	 */
	@SquirrelJMEVendorApi
	public static native TaskBracket current();
	
	/**
	 * Checks if the two given tasks are the same.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these tasks are the same.
	 * @throws MLECallError If either argument is {@code null}.
	 * @since 2020/07/02
	 */
	@SquirrelJMEVendorApi
	public static native boolean equals(TaskBracket __a, TaskBracket __b)
		throws MLECallError;
	
	/**
	 * Returns the current exit code of the given task.
	 * 
	 * @param __task The task to get the exit code of.
	 * @return The exit code, note that if it has not exited yet it may have
	 * still set it via {@link ThreadShelf#setCurrentExitCode(int)}. 
	 * @throws MLECallError If the task is not valid.
	 * @since 2020/07/02
	 */
	@SquirrelJMEVendorApi
	@CheckReturnValue
	public static native int exitCode(@NotNull TaskBracket __task)
		throws MLECallError;
	
	/**
	 * Gets the trace that was set by the program.
	 * 
	 * @param __task The task to get the trace from.
	 * @param __outMessage The output array to store the message.
	 * @return The trace for the given task or {@code null} if there is none.
	 * @throws MLECallError On null arguments, if the task is not valid, or
	 * if {@code __outMessage} is too small.
	 * @since 2020/07/02
	 */
	@SquirrelJMEVendorApi
	public static native TracePointBracket[] getTrace(
		@NotNull TaskBracket __task,
		@NotNull String[] __outMessage)
		throws MLECallError;
	
	/**
	 * If a task was started with {@link TaskPipeRedirectType#BUFFER} then the
	 * contents of the task's output buffer can be read with this.
	 * 
	 * This call must never block.
	 * 
	 * End-of-file is reached when the buffer is completely drained and the
	 * task either the task is {@link TaskStatusType#EXITED} or
	 * {@link Closeable#close()} was called on the output pipe.
	 * 
	 * @param __task The task to 
	 * @param __fd The {@link StandardPipeType} to read from.
	 * @param __b The buffer to read into.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return One of {@link PipeErrorType} or the number of bytes which were
	 * read.
	 * @throws MLECallError If the task does not use buffering for the given
	 * descriptor, {@code __fd} is not valid, or the offset and/or length
	 * are negative or exceed the array bounds.
	 * @since 2020/07/02
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = PipeErrorType.class)
	@Range(from = -2, to = Integer.MAX_VALUE)
	@CheckReturnValue
	public static native int read(@NotNull TaskBracket __task,
		@MagicConstant(valuesFromClass = StandardPipeType.class) int __fd,
		@NotNull byte[] __b,
		@Range(from = 0, to = Integer.MAX_VALUE) int __o,
		@Range(from = 0, to = Integer.MAX_VALUE) int __l)
		throws MLECallError;
	
	/**
	 * Launches another task with the specified class path, main class, and
	 * otherwise.
	 * 
	 * The first JAR in the classpath must be the same as our current classpath
	 * to prevent the CLDC library from being switched out.
	 * 
	 * Note that the first task implicitly created by the virtual machine
	 * will have its standard input be read from the standard input, whereas
	 * for sub-tasks it will be sourced from a buffer.
	 * 
	 * @param __classPath The class path that the launched application should
	 * use.
	 * @param __mainClass The main class to execute, this must be a class which
	 * contains {@code static void main(String[] __args)}.
	 * @param __args The arguments to the main class.
	 * @param __sysPropPairs System property pairs, even values are keys and
	 * odd values are values. This array must always be a multiple of two.
	 * @param __stdOut The {@link TaskPipeRedirectType} for standard output.
	 * @param __stdErr The {@link TaskPipeRedirectType} for standard error.
	 * @return The bracket that represents the task.
	 * @throws MLECallError If any argument is {@code null}, or an array
	 * contains a {@code null} value, the {@code __sysPropPairs} is not a
	 * multiple of two, {@code __classPath[0]} is not the same Jar package
	 * as our own classpath's first JAR, if the task could not be created,
	 * or if either the {@code __stdOut} or {@code __stdErr} redirect types
	 * are not valid.
	 * @since 2020/07/02
	 */
	@SquirrelJMEVendorApi
	public static native TaskBracket start(
		@NotNull JarPackageBracket[] __classPath, @NotNull String __mainClass,
		@NotNull String[] __args,
		@NotNull String[] __sysPropPairs,
		@MagicConstant(valuesFromClass = TaskPipeRedirectType.class)
			int __stdOut,
		@MagicConstant(valuesFromClass = TaskPipeRedirectType.class)
			int __stdErr)
		throws MLECallError;
	
	/**
	 * Returns the status of the given task.
	 * 
	 * @param __task The task to request the status of.
	 * @return One of {@link TaskStatusType}.
	 * @throws MLECallError If the task is not valid.
	 * @since 2020/07/02
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = TaskStatusType.class)
	public static native int status(@NotNull TaskBracket __task)
		throws MLECallError;
}
