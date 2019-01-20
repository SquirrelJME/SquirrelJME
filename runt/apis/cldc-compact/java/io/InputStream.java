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

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.annotation.ProgrammerTip;

/**
 * This is the base class for any input stream which is used for reading a
 * stream of bytes.
 *
 * @since 2019/01/20
 */
public abstract class InputStream
	implements Closeable
{
	/** The number of bytes to skip at a time. */
	private static final int _SKIP_LEN =
		256;
	
	/**
	 * Initializes the base input stream.
	 *
	 * @since 2018/10/13
	 */
	public InputStream()
	{
	}
	
	/**
	 * Reads a single byte from the input stream.
	 *
	 * @return The read unsigned byte value ({@code 0-255}) or {@code -1} if
	 * the end of stream has been reached.
	 * @throws IOException On read errors.
	 * @since 2018/11/06
	 */
	public abstract int read()
		throws IOException;
	
	/**
	 * Returns the number of bytes which can be read from the stream without
	 * blocking.
	 *
	 * @return The number of bytes which can be read.
	 * @throws IOException On read errors.
	 * @since 2019/01/20
	 */
	@ProgrammerTip("The base implementation always returns 0.")
	public int available()
		throws IOException
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/20
	 */
	@Override
	public void close()
		throws IOException
	{
	}
	
	/**
	 * Marks the stream so that a future {@link #reset()} returns to the point
	 * that was marked, if it is still within the bounds of the read limit.
	 *
	 * After the limit has been reached, the mark may become invalidated.
	 *
	 * @param __l The number of bytes to store for later resets before the
	 * mark becomes invalid.
	 * @since 2019/01/20
	 */
	@ProgrammerTip("The base implementation does nothing.")
	public void mark(int __l)
	{
	}
	
	/**
	 * Returns whether or not marking is supported on the input stream.
	 *
	 * @return If marking is supported.
	 * @since 2019/01/20
	 */
	@ProgrammerTip("The base implementation always returns false.")
	public boolean markSupported()
	{
		return false;
	}
	
	/**
	 * Reads multiple bytes and stores them into the array.
	 *
	 * @param __b The destination array.
	 * @return The number of bytes read or {@code -1} if the end of stream has
	 * been reached.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/06
	 */
	public int read(byte[] __b)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// No bytes to read?
		int n = __b.length;
		if (n <= 0)
			return 0;
		
		// Read bytes into the array
		for (int i = 0, o = 0; i < n; i++)
		{
			int rv = this.read();
			
			// EOF?
			if (rv < 0)
				return (i == 0 ? -1 : i);
			
			__b[o++] = (byte)rv;
		}
		
		// Read all bytes
		return n;
	}
	
	/**
	 * Reads multiple bytes and stores them into the array.
	 *
	 * @param __b The destination array.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to read.
	 * @return The number of bytes read or {@code -1} if the end of stream has
	 * been reached.
	 * @throws IndexOutOfBoundsException If the offset and/or length 
	 * negative or exceed the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/06
	 */
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// No bytes to read?
		if (__l <= 0)
			return 0;
		
		// Read bytes into the array
		for (int i = 0; i < __l; i++)
		{
			int rv = this.read();
			
			// EOF?
			if (rv < 0)
				return (i == 0 ? -1 : i);
			
			__b[__o++] = (byte)rv;
		}
		
		// Read all bytes
		return __l;
	}
	
	/**
	 * Resets the stream to a previously marked position.
	 *
	 * @throws IOException If marking is not supported or the mark has been
	 * invalidated by reading more than the limit bytes.
	 * @since 2019/01/20
	 */
	public void reset()
		throws IOException
	{
		// {@squirreljme.error ZZ3t Reset is not supported.}
		throw new IOException("ZZ3t");
	}
	
	/**
	 * Attemps to skip over the given number of bytes.
	 *
	 * It is not required for the actual number of bytes skipped to match the
	 * input amount, this could be specific to the input stream implementation
	 * or the end of file has been reached.
	 *
	 * @param __n The number of bytes to skip, if this value is negative then
	 * no bytes are skipped.
	 * @return The number of skipped bytes.
	 * @throws IOException On read errors.
	 * @since 2019/01/20
	 */
	@ProgrammerTip("The base implementation allocates an array and reads " +
		"into that array until all bytes have been skipped.")
	public long skip(long __n)
		throws IOException
	{
		// Not doing anything
		if (__n <= 0)
			return 0;
		
		// Keep reading bytes
		byte[] buf = new byte[_SKIP_LEN];
		long actual = 0;
		while (__n > 0)
		{
			// Only skip the small group
			int now = (int)(_SKIP_LEN < __n ? _SKIP_LEN : __n);
			
			// Try to skip these bytes
			int rv = this.read(buf, 0, now);
			
			// EOF?
			if (rv < 0)
				return actual;
			
			// Discard the bytes
			actual += rv;
			__n -= rv;
		}
		
		return actual;
	}
}

