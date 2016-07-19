// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.dynhistin;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.util.dynbuffer.DynamicByteBuffer;

/**
 * This is an input stream which allows any future data in the stream to be
 * cached for later actual reading. This class should be used in situations
 * where it is needed to read future bytes in the stream and react to those
 * bytes.
 *
 * @since 2016/07/19
 */
public class DynamicHistoryInputStream
	extends InputStream
{
	/** The backing buffer. */
	protected final DynamicByteBuffer buffer;
	
	/** The source input stream. */
	protected final InputStream input;
	
	/**
	 * Initializes a dynamic history stream which sources data from the given
	 * input stream.
	 *
	 * @param __is The stream to read data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public DynamicHistoryInputStream(InputStream __is)
		throws NullPointerException
	{
		this(__is, DynamicByteBuffer.DEFAULT_CHUNK_SIZE);
	}
	
	/**
	 * Initializes a dynamic history stream which sources data from the given
	 * input stream. An alternative chunk size for the backing
	 * {@link DynamicByteBuffer} may also be specified.
	 *
	 * @param __is The stream to read data from.
	 * @param __cs The chunk size to be used in the {@link DynamicByteBuffer}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public DynamicHistoryInputStream(InputStream __is, int __cs)
		throws NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.input = __is;
		this.buffer = new DynamicByteBuffer(__cs);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Peeks the specified unmber 
	 *
	 * @param __i The number of bytes to read ahead and buffer.
	 * @return The number of bytes which are available for input, this may
	 * be less than or greater than the input parameter.
	 * @throws IndexOutOfBoundsException If the count is negative.
	 * @throws IOException On read errors.
	 * @since 2016/07/19
	 */
	public int peek(int __i)
		throws IndexOutOfBoundsException, IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public int read()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) >= n)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw new Error("TODO");
	}
}

