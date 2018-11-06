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

import java.util.Arrays;

/**
 * This an output stream which outputs any written bytes to a byte array.
 *
 * @since 2018/11/06
 */
public class ByteArrayOutputStream
	extends OutputStream
{
	/** The initial capacity. */
	private static final int _CAPACITY =
		32;
	
	/** The bytes in the buffer. */
	protected byte[] buf;
	
	/** The number of valid bytes in the buffer. */
	protected int count;
	
	/**
	 * Initializes the output stream using the default capacity of 32 bytes.
	 *
	 * @since 2018/11/06
	 */
	public ByteArrayOutputStream()
	{
		this(_CAPACITY);
	}
	
	/**
	 * Initializes the output stream using the given capacity.
	 *
	 * @param __cap The capacity to use.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2018/11/06
	 */
	public ByteArrayOutputStream(int __cap)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ34 Cannot initialize output stream using a
		// negative capacity.}
		if (__cap < 0)
			throw new IllegalArgumentException("ZZ34");
		
		this.buf = new byte[__cap];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/06
	 */
	@Override
	public void close()
		throws IOException
	{
	}
	
	/**
	 * Resets the count back to zero while keeping the buffer, allowing it
	 * to be reused.
	 *
	 * @since 2018/11/06
	 */
	public void reset()
	{
		synchronized (this)
		{
			this.count = 0;
		}
	}
	
	/**
	 * Returns the number of bytes that are valid in the internal buffer.
	 *
	 * @return The number of bytes that are valid in the internal buffer.
	 * @since 2018/11/06
	 */
	public int size()
	{
		synchronized (this)
		{
			return this.count;
		}
	}
	
	/**
	 * Returns a newly allocated copy of the byte array.
	 *
	 * @return A copy of the byte array.
	 * @since 2018/11/06
	 */
	public byte[] toByteArray()
	{
		synchronized (this)
		{
			return Arrays.copyOf(this.buf, this.count);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/06
	 */
	@Override
	public String toString()
	{
		synchronized (this)
		{
			return new String(this.buf, 0, this.count);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/06
	 */
	@Override
	public void write(int __b)
	{
		byte[] buf = this.buf;
		int count = this.count;
		
		synchronized (this)
		{
			// Not enough room? Increase buffer size
			if (count == buf.length)
				this.buf = (buf = Arrays.copyOf(buf, count + _CAPACITY));
			
			// Write byte at end position
			buf[count] = (byte)__b;
			this.count = count + 1;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/06
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		int bufn = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bufn)
			throw new IndexOutOfBoundsException("IOOB");
		
		byte[] buf = this.buf;
		int count = this.count;
		
		synchronized (this)
		{
			// Reallocate buffer if it cannot be store data
			int endcount = count + __l;
			if (endcount > buf.length)
				this.buf = (buf = Arrays.copyOf(buf, endcount + _CAPACITY));
			
			// Write bytes
			for (int i = 0; i < __l; i++)
				buf[count++] = __b[__o++];
			
			this.count = endcount;
		}
	}
	
	/**
	 * Writes the internal buffer to the given output stream.
	 *
	 * @param __os The output stream to write to.
	 * @throws IOException On write.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/06
	 */
	public void writeTo(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			__os.write(this.buf, 0, this.count);
		}
	}
}

