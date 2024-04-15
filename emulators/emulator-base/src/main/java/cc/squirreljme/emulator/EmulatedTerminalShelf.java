// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import cc.squirreljme.jvm.mle.constants.PipeErrorType;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Java implementation of the terminal shelves.
 *
 * @since 2020/11/21
 */
@SuppressWarnings({"resource", "unused"})
public class EmulatedTerminalShelf
{
	/**
	 * Returns the number of available bytes for reading, if it is known.
	 * 
	 * @param __fd The {@link StandardPipeType} to close.
	 * @return The number of bytes ready for immediate reading, will be
	 * zero if there are none. For errors one of {@link PipeErrorType}.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2020/11/22
	 */
	public static int available(PipeBracket __fd)
		throws MLECallError
	{
		try
		{
			return EmulatedTerminalShelf.__input(__fd).available();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			return PipeErrorType.IO_EXCEPTION;
		}
	}
	
	/**
	 * Closes the output of the current process.
	 * 
	 * @param __fd The {@link StandardPipeType} to close.
	 * @return One of {@link PipeErrorType}.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2020/07/02
	 */
	public static int close(PipeBracket __fd)
		throws MLECallError
	{
		try
		{
			EmulatedTerminalShelf.__any(__fd).close();
			
			return PipeErrorType.NO_ERROR;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			return PipeErrorType.IO_EXCEPTION;
		}
	}
	
	/**
	 * Flushes the stream.
	 *
	 * @param __fd The {@link StandardPipeType} to flush.
	 * @return One of {@link PipeErrorType}.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2018/12/08
	 */
	public static int flush(PipeBracket __fd)
		throws MLECallError
	{
		try
		{
			EmulatedTerminalShelf.__output(__fd).flush();
			
			return PipeErrorType.NO_ERROR;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			return PipeErrorType.IO_EXCEPTION;
		}
	}
	
	/**
	 * Returns the pipe to a standardized input/output pipe that is shared
	 * across many systems.
	 * 
	 * @param __fd The pipe to get the pipe of.
	 * @return The pipe to the given pipe.
	 * @throws MLECallError If the standard pipe does not exist or is not
	 * valid.
	 * @since 2022/03/19
	 */
	public static PipeBracket fromStandard(int __fd)
		throws MLECallError
	{
		switch (__fd)
		{
			case StandardPipeType.STDIN:
				return new EmulatedPipeBracket(System.in);
			
			case StandardPipeType.STDOUT:
				return new EmulatedPipeBracket(System.out);
			
			case StandardPipeType.STDERR:
				return new EmulatedPipeBracket(System.err);
			
			default:
				throw new MLECallError("Invalid pipe: " + __fd);
		}
	}
	
	/**
	 * Reads from the given pipe into the output buffer.
	 *
	 * @param __fd The {@link StandardPipeType} to read from.
	 * @param __b The bytes to read into.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return One of {@link PipeErrorType} or the number of read bytes.
	 * @throws MLECallError If {@code __fd} is not valid, the offset and/or
	 * length are negative or exceed the buffer size, or {@code __b} is
	 * {@code null}.
	 * @since 2018/12/05
	 */
	public static int read(PipeBracket __fd, byte[] __b, int __o, int __l)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Null or Out of bounds I/O.");
		
		try
		{
			int rv = EmulatedTerminalShelf.__input(__fd).read(__b, __o, __l);
			return (rv < 0 ? PipeErrorType.END_OF_FILE : rv);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			return PipeErrorType.IO_EXCEPTION;
		}
	}
	
	/**
	 * Writes the character to the console output.
	 *
	 * @param __fd The {@link StandardPipeType} to write to.
	 * @param __c The byte to write, only the lowest 8-bits are used.
	 * @return One of {@link PipeErrorType} or {@code 1} on success.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2018/09/21
	 */
	public static int write(PipeBracket __fd, int __c)
		throws MLECallError
	{
		try
		{
			EmulatedTerminalShelf.__output(__fd).write(__c);
			return 1;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			return PipeErrorType.IO_EXCEPTION;
		}
	}
	
	/**
	 * Writes the given bytes to the console output.
	 *
	 * @param __fd The {@link StandardPipeType} to write to.
	 * @param __b The bytes to write.
	 * @param __o The offset.
	 * @param __l The length.
	 * @return One of {@link PipeErrorType} or {@code __l} on success.
	 * @throws MLECallError If {@code __fd} is not valid, the offset and/or
	 * length are negative or exceed the buffer size, or {@code __b} is
	 * {@code null}.
	 * @since 2018/12/05
	 */
	public static int write(PipeBracket __fd, byte[] __b, int __o, int __l)
		throws MLECallError
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new MLECallError("Null or Out of bounds I/O.");
		
		try
		{
			EmulatedTerminalShelf.__output(__fd).write(__b, __o, __l);
			return __l;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			return PipeErrorType.IO_EXCEPTION;
		}
	}
	
	/**
	 * Obtains the input or output stream.
	 * 
	 * @param __fd The file descriptor to get the stream of.
	 * @return The stream.
	 * @throws MLECallError If the stream is not valid.
	 * @since 2020/11/21
	 */
	private static Closeable __any(PipeBracket __fd)
		throws MLECallError
	{
		if (__fd instanceof EmulatedPipeBracket)
			return (Closeable)__fd;
		
		throw new MLECallError("Invalid any: " + __fd);
	}
	
	/**
	 * Obtains the input stream.
	 * 
	 * @param __fd The file descriptor to get the stream of.
	 * @return The stream.
	 * @throws MLECallError If the stream is not valid.
	 * @since 2020/11/21
	 */
	private static InputStream __input(PipeBracket __fd)
		throws MLECallError
	{
		if (__fd instanceof EmulatedPipeBracket)
		{
			EmulatedPipeBracket emul = (EmulatedPipeBracket)__fd;
			
			if (emul._in != null)
				return emul._in;
		}
		
		throw new MLECallError("Invalid input: " + __fd);
	}
	
	/**
	 * Obtains the output stream.
	 * 
	 * @param __fd The file descriptor to get the stream of.
	 * @return The stream.
	 * @throws MLECallError If the stream is not valid.
	 * @since 2020/11/21
	 */
	private static OutputStream __output(PipeBracket __fd)
		throws MLECallError
	{
		if (__fd instanceof EmulatedPipeBracket)
		{
			EmulatedPipeBracket emul = (EmulatedPipeBracket)__fd;
			
			if (emul._out != null)
				return emul._out;
		}
		
		throw new MLECallError("Invalid output: " + __fd);
	}
}
