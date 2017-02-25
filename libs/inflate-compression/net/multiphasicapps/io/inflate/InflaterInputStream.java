// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.inflate;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.slidingwindow.SlidingByteWindow;
import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This is used to decompress standard deflate compressed stream.
 *
 * @since 2017/02/24
 */
public class InflaterInputStream
	extends InputStream
{
	/** The size of the sliding window. */
	private static final int _SLIDING_WINDOW_SIZE =
		32768;
	
	/** No compression. */
	private static final int _TYPE_NO_COMPRESSION =
		0b00;
	
	/** Fixed huffman table compression. */
	private static final int _TYPE_FIXED_HUFFMAN =
		0b01;
	
	/** Dynamic huffman table compression. */
	private static final int _TYPE_DYNAMIC_HUFFMAN =
		0b10;
	
	/** An error. */
	private static final int _TYPE_ERROR =
		0b11;
	
	/** The deflated compressed stream to be decompressed. */
	protected final InputStream in;
	
	/** Sliding window for accessing old bytes. */
	protected final SlidingByteWindow window =
		new SlidingByteWindow(_SLIDING_WINDOW_SIZE);
	
	/** If the output cannot be filled, bytes are written here instead. */
	protected final ByteDeque overflow =
		new ByteDeque();
	
	/** Single byte read. */
	private final byte[] _solo =
		new byte[1];
	
	/** The read-in buffer which is used to bulk read input bytes. */
	private final byte[] _readin =
		new byte[4];
	
	/**
	 * The miniature read window, it stores a 32-bit value and is given input
	 * bytes to read along with being used as output. This is an int because it
	 * is faster to work with integer values rather than bytes. It also means
	 * that it is much simpler to work with.
	 */
	private volatile int _miniwindow;
	
	/** Represents the number of bits in the mini window. */
	private volatile int _minisize;
	
	/** EOF has been reached? */
	private volatile boolean _eof;
	
	/**
	 * Initializes the deflate compression stream inflater.
	 *
	 * @param __in The stream to inflate.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/24
	 */
	public InflaterInputStream(InputStream __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/24
	 */
	@Override
	public int available()
		throws IOException
	{
		// Use the number of bytes that are able to be read quickly without
		// requiring decompression
		return this.overflow.available();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/24
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close input
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/24
	 */
	@Override
	public int read()
		throws IOException
	{
		// Try reading a single byte
		byte[] solo = this._solo;
		for (;;)
		{
			int rv = read(solo, 0, 1);
			
			// EOF?
			if (rv < 0)
				return rv;
			
			// Try again
			else if (rv == 0)
				continue;
			
			// Return that byte
			else
				return (solo[0] & 0xFF);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/24
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int bl = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > bl)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		// If there are bytes in the overflow buffer, read them first into the
		// output because they are the result of previous decompression.
		ByteDeque overflow = this.overflow;
		int ovn = overflow.available(),
			ovr = (ovn < __l ? ovn : __l);
		int c = overflow.removeFirst(__b, __o, __l);
		
		// End of stream reached
		if (this._eof)
		{
			// Never return EOF if no bytes were read and bytes were available
			// even when EOF has been triggered.
			if (c > 0 || overflow.available() > 0)
				return c;
			
			// Otherwise EOF
			return -1;
		}
		
		// Only read overflow bytes? Do not bother decompressing more data
		// because it will just be added to the queue
		if (c >= __l)
			return c;
		
		// Try to fit as many bytes as possible into the output
		while (c < __l)
		{
			int rv = __decompress(__b, __o + c, __l - c);
			
			// Ended?
			if (rv < 0)
				this._eof = true;
			
			// Otherwise add those bytes
			c += rv;
		}
		
		// Return the read count
		return c;
	}
	
	/**
	 * Reads the input and performs decompression on the data.
	 *
	 * @param __b The array to write into.
	 * @param __o The offset into the array.
	 * @parma __l The number of bytes to store.
	 * @return The number of stored bytes or a negative value if the stream
	 * has terminated.
	 * @throws IOException On read or decompression errors.
	 * @since 2017/02/25
	 */
	private int __decompress(byte[] __b, int __o, int __l)
		throws IOException
	{
		// Read the final bit which determines if this is the last block
		int finalhit = __readBits(1, false);
		
		// Read the window type
		int type = __readBits(2, false);
		switch (type)
		{
				// None
			case _TYPE_NO_COMPRESSION:
				throw new Error("TODO");
				
				// Fixed huffman
			case _TYPE_FIXED_HUFFMAN:
				throw new Error("TODO");
				
				// Dynamic huffman
			case _TYPE_DYNAMIC_HUFFMAN:
				throw new Error("TODO");
			
				// Error or unknown
			case _TYPE_ERROR:
			default:
				// {@squirreljme.error BY01 Unknown type or the error type
				// was reached. (The type code used in the stream)}
				throw new IOException(String.format("BY01 %d", type));
		}
	}
	
	/**
	 * Reads bits from the input stream.
	 *
	 * @param __n The number of bits to read.
	 * @param __msb If {@code true} the most significant bits are first
	 * @return The read data.
	 * @throws IOException On read errors.
	 * @since 2017/02/25
	 */
	private int __readBits(int __n, boolean __msb)
		throws IOException
	{
		// Nothing to read
		if (__n == 0)
			return 0;
		
		// Get the mini window information
		int miniwindow = this._miniwindow,
			minisize = this._minisize;
		
		// Not enough bits to read the value
		while (minisize < __n)
		{
			// The number of bytes to be read
			int bc = (__n - minisize) / 8;
			if (bc == 0)
				bc = 1;
			
			// Read input bytes
			byte[] readin = this._readin;
			int rc = this.in.read(readin, 0, bc);
			
			// {@squirreljme.error BY02 Reached EOF while reading bytes to
			// decompress.}
			if (rc < 0)
				throw new IOException("BY02");
			
			// Shift in the read bytes to the higher positions
			for (int i = 0; i < rc; i++)
			{
				miniwindow |= ((readin[i] & 0xFF) << minisize);
				minisize += 8;
			}
		}
		
		// Mask in the value, which is always at the lower bits
		int mask = (1 << __n) - 1;
		int rv = miniwindow & mask;
		
		// Shift down the mini window for the next read
		// Make sure the shift down is unsigned so that zeroes are in the
		// higher bits for the filling OR operation.
		miniwindow >>>= __n;
		minisize -= __n;
		
		// Store for next run
		this._miniwindow = miniwindow;
		this._minisize = minisize;
		
		// Want LSB to be first, need to swap all the bits so the lowest ones
		// are at the highest positions
		// Luckily such a method already exists and it could potentially be
		// inlined by the JVM or converted to native code if such an
		// instruction exists.
		if (!__msb)
			return Integer.reverse(rv) >>> (32 - __n);
		
		// Return read result
		return rv;
	}
}

