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
	
	/** The output write window, this is used to shift out writes as needed. */
	private volatile int _writewindow;
	
	/** The number of bits in the write window. */
	private volatile int _writesize;
	
	/** EOF has been reached? */
	private volatile boolean _eof;
	
	/** The target byte array for writes. */
	private byte[] _targ;
	
	/** The target offset for writes. */
	private int _targoff;
	
	/** The target end offset for writes. */
	private int _targend;
	
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
		
		// Store write information
		this._targ = __b;
		
		// Try to fit as many bytes as possible into the output
		while (c < __l)
		{
			// Decompress
			int base;
			this._targoff = (base = __o + c);
			this._targend = base + (__l - c);
			int rv = __decompress();
			
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
	 * @return The number of stored bytes or a negative value if the stream
	 * has terminated.
	 * @throws IOException On read or decompression errors.
	 * @since 2017/02/25
	 */
	private int __decompress()
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
				__decompressFixed();
				break;
				
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
		
		// If this was the last block to read, then return EOF if no data
		// was actually read, but mark EOF otherwise
		int rv = (this._targend - this._targoff);
		if (finalhit != 0)
		{
			this._eof = true;
			return (rv == 0 ? -1 : rv);
		}
		
		// Just the read count
		else
			return rv;
	}
	
	/**
	 * Decompressed data stored with the fixed huffman table and decompresses
	 * it.
	 *
	 * @throws IOException On read or decompression errors.
	 * @since 2017/02/25
	 */
	private void __decompressFixed()
		throws IOException
	{
		// Read until the sequence has ended
		for (;;)
			if (__handleFixedCode(__readFixedHuffman()))
				break;
	}
	
	/**
	 * Handles a fixed huffman code
	 *
	 * @param __c The code used.
	 * @return {@code true} on termination.
	 * @throws IOException On read or decompression errors.
	 * @since 2017/02/25
	 */
	private boolean __handleFixedCode(int __c)
		throws IOException
	{
		// Literal byte value
		if (__c >= 0 && __c <= 255)
		{
			__write(__c, 0xFF, false);
			
			// Do not break
			return false;
		}
		
		// Stop processing
		else if (__c == 256)
			return true;
		
		// Window based result
		else if (__c >= 257 && __c <= 285)
		{
			// Read the length
			int lent = __handleLength(__c);
			
			// Read the distance
			int dist = __handleFixedDistance();
			
			// Get the maximum valid length, so for example if the length is 5
			// and the distance is two, then only read two bytes.
			int maxlen;
			if (dist - lent < 0)
				maxlen = dist;
			else
				maxlen = lent;
			
			// Create a byte array from the sliding window data
			byte[] winb = new byte[maxlen];
			try
			{
				this.window.get(dist, winb, 0, maxlen);
			}
			
			// Bad window read
			catch (IndexOutOfBoundsException ioobe)
			{
				// {@squirreljme.error BY06 Window access out of range.
				// (The distance; The length)}
				throw new IOException(String.format(
					"BY06 %d %d", dist, lent), ioobe);
			}
			
			// Add those bytes to the output, handle wrapping around if the
			// length is greater than the current position
			for (int i = 0, v = 0; i < lent; i++, v++)
			{
				// Write byte
				__write(winb[v] & 0xFF, 0xFF, false);
				
				// Wrap around
				if (v >= maxlen)
					v = 0;
			}
			
			// Do not break the loop
			return false;
		}
		
		// {@squirreljme.error BY05 Illegal code. (The code.)}
		else
			throw new IOException(String.format("BY05 %d", __c));
	}
	
	/**
	 * Handles fixed huffman distance.
	 *
	 * @return The ditsance read.
	 * @throws IOException On read errors.
	 * @since 2017/02/25
	 */
	private int __handleFixedDistance()
		throws IOException
	{
		// If using fixed huffman read 5 bits since they are all the same
		int code = __readBits(5, true);
		
		// {@squirreljme.error BY04 Illegal fixed distance code. (The distance
		// code)}
		if (code > 29)
			throw new IOException(String.format("BY04 %d", code));
		
		// Calculate the required distance to use
		int rv = 1;
		for (int i = 0; i < code; i++)
		{
			// This uses a similar pattern to the length code, however the
			// division is half the size (so there are groups of 2 now).
			rv += (1 << ((i / 2) - 1));
		}
		
		// Determine the number of extra bits
		// If there are bits to read then read them in
		int extrabits = ((code / 2) - 1);
		if (extrabits > 0)
			rv += __readBits(extrabits, false);
		
		// Return it
		return rv;
	}
	
	/**
	 * Reads length codes from the input.
	 *
	 * @param __c Input code value.
	 * @throws IOException On read/write errors.
	 * @since 2016/03/12
	 */
	private int __handleLength(int __c)
		throws IOException
	{
		// If the code is 285 then the length will be that
		if (__c == 285)
			return 285;
		
		// Get the base code
		int base = __c - 257;
		
		// {@squirreljme.error BY03 Illegal length code. (The length code)}
		if (base < 0)
			throw new IOException(String.format("BY03 %d", __c));
		
		// Calculate the required length to use
		int rv = 3;
		for (int i = 0; i < base; i++)
		{
			// Determine how many groups of 4 the code is long. Since zero
			// appears as items then subtract 1 to make it longer. However
			// after the first 8 it goes up in a standard pattern.
			rv += (1 << ((i / 4) - 1));
		}
		
		// Calculate the number of extra bits to read
		int extrabits = (base / 4) - 1;
		
		// Read in those bits, if applicable
		if (extrabits > 0)
			rv += __readBits(extrabits, false);
		
		// Return the length
		return rv;
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
	
	/**
	 * Reads a fixed huffman code for use by the {@code _TYPE_FIXED_HUFFMAN}
	 * state. This method does not traverse a huffman tree so to speak, but it
	 * instead uses many if statements. Initially every consideration was made
	 * but now instead it uses ranges once it keeps deep enough into the tree.
	 * This method is faster and provides a built-in huffman tree while not
	 * taking up too many bytes in the byte code.
	 *
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @since 2016/03/10
	 */
	private int __readFixedHuffman()
		throws IOException
	{
		// The long if statement block
		if (__readBits(1, true) == 1)
			if (__readBits(1, true) == 1)
				if (__readBits(1, true) == 1)
					return 192 + __readBits(6, true);
				else
					if (__readBits(1, true) == 1)
						return 160 + __readBits(5, true);
					else
						if (__readBits(1, true) == 1)
							return 144 + __readBits(4, true);
						else
							return 280 + __readBits(3, true);
			else
				return 80 + __readBits(6, true);
		else
			if (__readBits(1, true) == 1)
				return 16 + __readBits(6, true);
			else
				if (__readBits(1, true) == 1)
					if (__readBits(1, true) == 1)
						return 0 + __readBits(4, true);
					else
						return 272 + __readBits(3, true);
				else
					return 256 + __readBits(4, true);
	}
	
	/**
	 * Writes the specified value to the output.
	 *
	 * @param __v The value to write.
	 * @param __mask The mask of the value.
	 * @param __msb Most significant bits first?
	 * @throws IOException On write errors.
	 * @since 2017/02/25
	 */
	private void __write(int __v, int __mask, boolean __msb)
		throws IOException
	{
		// Count bits to write
		int bits = Integer.bitCount(__mask);
		
		// Write LSB value, need to swap bits
		__v &= __mask;
		if (!__msb)
			__v = Integer.reverse(__v) >>> (32 - bits);
		
		// Get the current write window
		int writewindow = this._writewindow,
			writesize = this._writesize;
		
		// Add bits to write
		writewindow |= __v << writesize;
		writesize += bits;
		
		// Enough bytes to write to the output?
		if (writesize >= 8)
		{
			// Write to a byte array first
			byte[] targ = this._targ;
			int targoff = this._targoff,
				targend = this._targend;
			
			// But if writes overflow then add to the overflow buffer
			ByteDeque overflow = this.overflow;
			
			// Any bytes written are appended to the sliding window
			SlidingByteWindow window = this.window;
			
			// Write bytes
			do
			{
				// Read input byte
				byte b = (byte)writewindow;
				System.err.printf("DEBUG -- Write 0x%02x (%c)%n", b,
					(b >= ' ' ? (char)b : '?'));
				writewindow >>>= 8;
				writesize -= 8;
				
				// Can fit in the output buffer
				if (targoff < targend)
					targ[targoff++] = b;
				
				// Overflows
				else
					overflow.offerLast(b);
				
				// Append to the window
				window.append(b);
			} while (writesize >= 8);
			
			// Store new position
			this._targoff = targoff;
		}
		
		// Store the write window info
		this._writewindow = writewindow;
		this._writesize = writesize;
	}
}

