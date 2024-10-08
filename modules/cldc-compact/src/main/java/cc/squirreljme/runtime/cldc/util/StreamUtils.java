// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.MemoryProfileType;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * General utilities for streams.
 *
 * @since 2021/09/06
 */
public final class StreamUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2021/09/06
	 */
	private StreamUtils()
	{
	}
	
	/**
	 * Determines the best available buffer size.
	 * 
	 * @param __in The stream to read from, may be {@code null}.
	 * @return The recommended buffer.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static byte[] buffer(InputStream __in)
		throws IOException, NullPointerException
	{
		return new byte[StreamUtils.bufferSize(__in)];
	}
	
	/**
	 * Determines the best available buffer size.
	 * 
	 * @param __in The stream to read from, may be {@code null}.
	 * @return The recommended buffer size.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/05
	 */
	public static int bufferSize(InputStream __in)
		throws IOException, NullPointerException
	{
		// Determine the initial buffer size
		int initCap;
		switch (RuntimeShelf.memoryProfile())
		{
			case MemoryProfileType.MINIMAL:
				initCap = 4096;
				break;
			
			case MemoryProfileType.NORMAL:
			default:
				initCap = 16384;
				break;
		}
		
		// Number of bytes already available can be used to determine a more
		// optimal buffer size
		int avail = -1;
		if (__in != null)
			avail = Math.max(-1, __in.available());
		
		// Determine the allocation size to use, use the initial cap if
		// the available bytes are unknown or if the buffer size is very
		// small otherwise.
		int allocSize;
		if (avail <= 512)
			allocSize = initCap;
		
		// Otherwise use an allocation size that fits with this, but make sure
		// it is a power of two
		else
			allocSize = Math.min(initCap, Integer.highestOneBit(avail + 1));
		
		// Use this size
		return allocSize;
	}
	
	/**
	 * Copies the given byte array to the given output stream, the stream
	 * is not closed by this method.
	 * 
	 * @param __in The input byte array.
	 * @param __out The output stream.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/10/14
	 */
	public static void copy(byte[] __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		try (ByteArrayInputStream in = new ByteArrayInputStream(__in))
		{
			StreamUtils.copy(in, __out, StreamUtils.buffer(null));
		}
	}
	
	/**
	 * Copies the given input stream to the given output stream, the streams
	 * are not closed by this method.
	 * 
	 * @param __in The input stream.
	 * @param __out The output stream.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void copy(InputStream __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		StreamUtils.copy(__in, __out, StreamUtils.buffer(__in));
	}
	
	/**
	 * Copies the given input stream to the given output stream using the
	 * given buffer as the temporary storage area, the streams
	 * are not closed by this method.
	 * 
	 * @param __in The input stream.
	 * @param __out The output stream.
	 * @param __tempBuf The temporary storage buffer.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/09/06
	 */
	public static void copy(InputStream __in, OutputStream __out,
		byte[] __tempBuf)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null || __tempBuf == null)
			throw new NullPointerException("NARG");
		
		// Perform the copy
		int chunk = __tempBuf.length;
		for (;;)
		{
			int rc = __in.read(__tempBuf);
			
			// EOF?
			if (rc < 0)
				break;
			
			__out.write(__tempBuf, 0, rc);
		}
	}
	
	/**
	 * Reads every byte within the input stream.
	 * 
	 * @param __in The stream to read from.
	 * @return A byte array containing all the stream bytes.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/03
	 */
	public static byte[] readAll(InputStream __in)
		throws IOException, NullPointerException
	{
		return StreamUtils.readAll(StreamUtils.bufferSize(__in), __in);
	}
	
	/**
	 * Reads every byte within the input stream.
	 * 
	 * @param __size The size of the buffer to allocate.
	 * @param __in The stream to read from.
	 * @return A byte array containing all the stream bytes.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/05
	 */
	public static byte[] readAll(int __size, InputStream __in)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		if (__size <= 0)
			throw new IllegalArgumentException("ZERO");
		
		// Setup buffers for temporary copy
		byte[] buf = new byte[__size];
		
		// Write into our target buffer
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(__size))
		{
			for (;;)
			{
				int rc = __in.read(buf, 0, __size);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Store within
				baos.write(buf, 0, rc);
			}
		
			// All done!
			return baos.toByteArray();
		}
	}
	
	/**
	 * Reads every line within the input stream.
	 * 
	 * @param __in The stream to read from.
	 * @param __charset The character set to use.
	 * @return A list of all the lines.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/25
	 */
	public static List<String> readAllLines(InputStream __in, String __charset)
		throws IOException, NullPointerException
	{
		if (__in == null || __charset == null)
			throw new NullPointerException("NARG");
		
		BufferedReader in = new BufferedReader(
			new InputStreamReader(__in, __charset));
		
		// Read all lines
		List<String> result = new ArrayList<>();
		for (;;)
		{
			// Stop on EOF
			String ln = in.readLine();
			if (ln == null)
				break;
			
			// Ignore blank lines
			ln = ln.trim();
			if (ln.isEmpty())
				continue;
			
			// Store as read
			result.add(ln);
		}
		
		return result;
	}
	
	/**
	 * Similarly to {@link DataInputStream#readFully(byte[], int, int)} this
	 * will read all of the bytes within the stream however this will return
	 * a lower return value if EOF was reached.
	 * 
	 * @param __in The stream to read from.
	 * @param __b The output buffer.
	 * @return The number of bytes read or {@code -1} on EOF.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/05
	 */
	public static int readMostly(InputStream __in, byte[] __b)
		throws IOException, NullPointerException
	{
		if (__in == null || __b == null)
			throw new NullPointerException("NARG");
		
		return StreamUtils.readMostly(__in, __b, 0, __b.length);
	}
	
	/**
	 * Similarly to {@link DataInputStream#readFully(byte[], int, int)} this
	 * will read all of the bytes within the stream however this will return
	 * a lower return value if EOF was reached.
	 * 
	 * @param __in The stream to read from.
	 * @param __b The output buffer.
	 * @param __o The offset into the buffer.
	 * @param __l The number of bytes to read.
	 * @return The number of bytes read or {@code -1} on EOF.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/05
	 */
	public static int readMostly(InputStream __in, byte[] __b, int __o,
		int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__in == null || __b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Constantly read in as much as possible
		int rv = 0;
		while (rv < __l)
		{
			// Read entire chunk
			int rc = __in.read(__b, __o + rv, __l - rv);
			
			// Reached EOF
			if (rc < 0)
			{
				if (rv == 0)
					return -1;
				break;
			}
			
			// These many bytes were read, we might try again
			rv += rc;
		}
		
		return rv;
	}
}
