// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

/**
 * This class is the base class for all byte oriented output streams.
 *
 * @since 2016/04/16
 */
public abstract class OutputStream
	implements Closeable, Flushable
{
	/**
	 * Initializes the base output stream.
	 *
	 * @since 2016/04/16
	 */
	public OutputStream()
	{
	}
	
	/**
	 * Writes a single byte to the output stream, the byte is in the range of
	 * 0 and 255. If the byte exceeds that range, then it must take the low
	 * order value and write that {@code __a & 0xFF}.
	 *
	 * @param __a The value the low order byte is derived from for output to
	 * this stream.
	 * @throws IOException On write errors.
	 * @since 2016/04/16
	 */
	public abstract void write(int __a)
		throws IOException;
	
	/**
	 * {@inheritDoc}
	 *
	 * If this method is not overridden by a sub-class then this does nothing.
	 *
	 * @since 2016/04/16
	 */
	@Override
	public void close()
		throws IOException
	{	
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * If this method is not overridden by a sub-class then this does nothing.
	 *
	 * @since 2016/04/16
	 */
	@Override
	public void flush()
		throws IOException
	{
		// Does nothing
	}
	
	/**
	 * Writes bytes from the given input array to this output stream.
	 *
	 * If this method is not overidden by a sub-class then this will call
	 * {@link #write(int)} for each byte in the buffer.
	 *
	 * @param __a The source byte array to write.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/16
	 */
	public void write(byte[] __a)
		throws IOException, NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		// Write all bytes
		for (int i = 0, n = __a.length; i < n; i++)
			this.write(__a[i]);
	}
	
	/**
	 * Writes multiple bytes from the specific array into this output stream.
	 *
	 * @param __a The array to source bytes from.
	 * @param __o The offset from the start of the array to start reading from.
	 * @param __l The number of bytes to write.
	 * @throws IndexOutOfBoundsException If the offset or length are negative;
	 * or the offset and the length exceeds the input array.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/16
	 */
	public void write(byte[] __a, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) >= __a.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Write all bytes
		for (int i = 0; i < __l; i++)
			this.write(__a[__o + i]);
	}
}

