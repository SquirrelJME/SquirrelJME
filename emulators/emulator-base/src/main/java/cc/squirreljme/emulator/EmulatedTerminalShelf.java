// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

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
@SuppressWarnings("resource")
public class EmulatedTerminalShelf
{
	/**
	 * Closes the output of the current process.
	 * 
	 * @param __fd The {@link StandardPipeType} to close.
	 * @return One of {@link PipeErrorType}.
	 * @throws MLECallError If {@code __fd} is not valid.
	 * @since 2020/07/02
	 */
	public static int close(int __fd)
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
	public static int flush(int __fd)
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
	public static int read(int __fd, byte[] __b, int __o, int __l)
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
	public static int write(int __fd, int __c)
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
	public static int write(int __fd, byte[] __b, int __o, int __l)
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
	private static Closeable __any(int __fd)
		throws MLECallError
	{
		if (__fd == StandardPipeType.STDIN)
			return System.in;
		else if (__fd == StandardPipeType.STDOUT)
			return System.out;
		else if (__fd == StandardPipeType.STDERR)
			return System.err;
		
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
	private static InputStream __input(int __fd)
		throws MLECallError
	{
		if (__fd == StandardPipeType.STDIN)
			return System.in;
		
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
	private static OutputStream __output(int __fd)
		throws MLECallError
	{
		if (__fd == StandardPipeType.STDOUT)
			return System.out;
		else if (__fd == StandardPipeType.STDERR)
			return System.err;
		
		throw new MLECallError("Invalid output: " + __fd);
	}
}
