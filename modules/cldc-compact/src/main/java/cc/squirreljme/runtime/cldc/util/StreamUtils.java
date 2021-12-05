// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.MemoryProfileType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
		
		// Use a fresh allocation of that
		return new byte[allocSize];
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
}
