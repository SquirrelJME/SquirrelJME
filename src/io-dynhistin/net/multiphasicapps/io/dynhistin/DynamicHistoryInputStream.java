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
import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This is an input stream which allows any future data in the stream to be
 * cached for later actual reading. This class should be used in situations
 * where it is needed to read future bytes in the stream and react to those
 * bytes.
 *
 * This class is not thread safe.
 *
 * @since 2016/07/19
 */
public class DynamicHistoryInputStream
	extends InputStream
{
	/** The backing buffer. */
	protected final ByteDeque buffer;
	
	/** The source input stream. */
	protected final InputStream input;
	
	/** Closed? */
	private volatile boolean _closed;
	
	/** EOF reached? */
	private volatile boolean _eof;
	
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
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.input = __is;
		this.buffer = new ByteDeque();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close
		this._closed = true;
		
		// Clear the buffer since it is not needed
		this.buffer.clear();
		
		// Close wrapped stream
		this.input.close();
	}
	
	/**
	 * Grabs the specified number of bytes and loads them into an internal
	 * queue where they may then be obtained using another method. 
	 *
	 * @param __i The number of bytes to read ahead and buffer.
	 * @return The number of bytes which are available for input, this may
	 * be less than or greater than the input parameter.
	 * @throws IndexOutOfBoundsException If the count is negative.
	 * @throws IOException On read errors.
	 * @since 2016/07/19
	 */
	public int grab(int __i)
		throws IndexOutOfBoundsException, IOException
	{
		// {@squirreljme.error BI03 A negative number of bytes cannot be
		// grabbed. (The number of bytes to grab)}
		if (__i < 0)
			throw new IndexOutOfBoundsException(String.format("BI03 %s", __i));
		
		// Lock
		ByteDeque buffer = this.buffer;
		
		// {@squirreljme.error BI04 Cannot grab bytes because the stream
		// is closed.}
		if (this._closed)
			throw new IOException("BI04");
		
		// Already have this number of bytes grabbed
		int cursize = buffer.available();
		if (__i <= cursize)
			return cursize;
		
		// The number of bytes that need to be read
		int diff = __i - cursize;
		
		// Read them from the input
		byte[] qq = new byte[diff];
		int rc = this.input.read(qq);
		
		// If no bytes or EOF was read, then just return the current
		// buffer size
		if (rc <= 0)
			return cursize;
		
		// Add them to the end of the buffer
		buffer.addLast(qq, 0, rc);
		
		// The number of available bytes is the current and the read
		// count
		return cursize + rc;
	}
	
	/**
	 * Reads a single byte that is ahead of the current read position.
	 *
	 * @param __a The position of the byte ahead of the current read position
	 * to read.
	 * @return The read value or a negative value if the byte to be read
	 * exceeds the end of the stream.
	 * @throws IndexOutOfBoundsException If the requested read ahead position
	 * is negative.
	 * @throws IOException On read errors.
	 * @since 2016/07/19
	 */
	public int peek(int __a)
		throws IndexOutOfBoundsException, IOException
	{
		// {@squirreljme.error BI01 Cannot a peek byte which have already been
		// read. (The requested index)}
		if (__a < 0)
			throw new IndexOutOfBoundsException(String.format("BI01 %d", __a));
		
		// Lock
		ByteDeque buffer = this.buffer;
		
		// {@squirreljme.error BI05 Cannot peek a single byte because the
		// stream is closed.}
		if (this._closed)
			throw new IOException("BI05");
		
		// Grab bytes, stop if none are available
		int avail = grab(__a + 1);
		if (avail < __a)
			return -1;
		
		throw new Error("TODO");
	}
	
	/**
	 * Reads multiple bytes which are ahead of the current read position.
	 *
	 * @param __a The start position of the bytes ahead of the current read
	 * position.
	 * @param __b The array which receives the bytes being read.
	 * @return The number of bytes read or a negative value if there are no
	 * bytes to be read because they exceed the end of the stream.
	 * @throws IndexOutOfBoundsException If the requested read ahead position
	 * is negative.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public int peek(int __a, byte[] __b)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		return this.peek(__a, __b, 0, __b.length);
	}
	
	/**
	 * Reads multiple bytes which are ahead of the current read position.
	 *
	 * @param __a The start position of the bytes ahead of the current read
	 * position.
	 * @param __b The array which receives the bytes being read.
	 * @param __o The starting offset into the array to write into.
	 * @param __l The number of bytes to read.
	 * @return The number of bytes read or a negative value if there are no
	 * bytes to be read because they exceed the end of the stream.
	 * @throws IndexOutOfBoundsException If the requested read ahead position
	 * is negative; or the offset and or length exceed the array size.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/19
	 */
	public int peek(int __a, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// {@squirreljme.error BI02 Cannot peek bytes which have already been
		// read. (The requested index)}
		if (__a < 0)
			throw new IndexOutOfBoundsException(String.format("BI02 %d", __a));
		
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock
		ByteDeque buffer = this.buffer;
		
		// {@squirreljme.error BI06 Cannot peek multiple bytes because
		// the stream is closed.}
		if (this._closed)
			throw new IOException("BI06");
		
		// Grab bytes, stop if none are available
		int avail = grab(__a + __l);
		if (avail < __a)
			return -1;
		
		// Not reading anything?
		int rc = Math.min(__l, avail);
		if (rc < 0)
			return 0;
		
		// Read from the buffer
		if (true)
			throw new Error("TODO");
		/*buffer.get(__a, __b, __o, rc);*/
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/19
	 */
	@Override
	public int read()
		throws IOException
	{
		// {@squirreljme.error BI07 Cannot read a single byte because the
		// stream has been closed.}
		if (this._closed)
			throw new IOException("BI07");
		
		// Grab a single byte
		int gc = grab(1);
		
		// Nothing left
		if (gc <= 0)
			return -1;
		
		// Read single byte
		return (this.buffer.removeFirst() & 0xFF);
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
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock
		ByteDeque buffer = this.buffer;
		
		// {@squirreljme BI08 Cannot read multiple bytes because the
		// stream is closed.}
		if (this._closed)
			throw new IOException("BI08");
		
		// Grab multiple bytes
		int gc = grab(__l);
		
		// Nothing left?
		if (gc <= 0)
			return -1;
		
		// No bytes to read?
		int dc = Math.min(gc, __l);
		if (dc <= 0)
			return 0;
		
		// Remove the early bytes
		buffer.removeFirst(__b, __o, dc);
		return dc;
	}
}

