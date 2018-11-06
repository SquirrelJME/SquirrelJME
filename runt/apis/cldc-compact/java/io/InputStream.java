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

public abstract class InputStream
	implements Closeable
{
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
	
	public int available()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	public void close()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	public void mark(int __a)
	{
		throw new todo.TODO();
	}
	
	public boolean markSupported()
	{
		throw new todo.TODO();
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
	
	public void reset()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	public long skip(long __a)
		throws IOException
	{
		throw new todo.TODO();
	}
}

