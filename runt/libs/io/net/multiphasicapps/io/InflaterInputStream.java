// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;

/**
 * This is used to decompress standard deflate compressed stream.
 *
 * Associated standards:
 * {@link https://www.ietf.org/rfc/rfc1951.txt}.
 *
 * This class is not thread safe.
 *
 * @since 2017/02/24
 */
public class InflaterInputStream
	extends DecompressionInputStream
{
	/** The size of the sliding window. */
	private static final int _DEFAULT_SLIDING_WINDOW_SIZE =
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
	
	/** The maximum number of bits in the code length tree. */
	private static final int _MAX_BITS =
		15;
	
	/** Shuffled bit values when reading values. */
	private static final int[] _SHUFFLE_BITS =
		new int[]
		{
			16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15
		};
	
	/** The deflated compressed stream to be decompressed. */
	protected final InputStream in;
	
	/** Sliding window for accessing old bytes. */
	protected final SlidingByteWindow window;
	
	/** If the output cannot be filled, bytes are written here instead. */
	protected final ByteDeque overflow =
		new ByteDeque();
	
	/** When bytes are read, a checkum will be calculated for it, optional. */
	protected final Checksum checksum;
	
	/** Single byte read. */
	private final byte[] _solo =
		new byte[1];
	
	/** The read-in buffer which is used to bulk read input bytes. */
	private final byte[] _readin =
		new byte[4];
	
	/** The bit source for reading. */
	private final BitSource _bitsource =
		new __BitSource__();
	
	/**
	 * Raw code lengths (allocated once), the size is the max code length
	 * count.
	 */
	private final int[] _rawcodelens =
		new int[19];
	
	/**
	 * Raw literal and distances (allocated once), the size is the total of
	 * both the maximum length count and distance count.
	 */
	private final int[] _rawlitdistlens =
		new int[322];
	
	/** Used to store bit length counts. */
	private final int[] _blcount =
		new int[_MAX_BITS + 1];
	
	/** Used to store the next code. */
	private final int[] _nextcode =
		new int[_MAX_BITS + 1];
	
	/** The number of compressed bytes. */
	private volatile long _compressedsize;
	
	/** The number of uncompressed bytes. */
	private volatile long _uncompressedsize;
	
	/** The code length tree. */
	private volatile Reference<HuffmanTreeInt> _codelentree;
	
	/** The literal tree. */
	private volatile Reference<HuffmanTreeInt> _literaltree;
	
	/** The distance tree. */
	private volatile Reference<HuffmanTreeInt> _distancetree;
	
	/** Window reader. */
	private volatile Reference<byte[]> _readwindow;
	
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
		this(__in, _DEFAULT_SLIDING_WINDOW_SIZE);
	}
	
	/**
	 * Initializes the deflate compression stream inflater, an optional
	 * checksum calculator may be specified also.
	 *
	 * @param __in The stream to inflate.
	 * @param __cs
	 * @throws NullPointerException On null arguments, except for {@code __cs}.
	 * @since 2017/02/24
	 */
	public InflaterInputStream(InputStream __in, Checksum __cs)
		throws NullPointerException
	{
		this(__in, _DEFAULT_SLIDING_WINDOW_SIZE, __cs);
	}
	
	/**
	 * Initializes the deflate compression stream inflater with a custom
	 * size specified for the sliding window.
	 *
	 * @param __in The stream to inflate.
	 * @param __sls Custom size to the sliding window.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/04
	 */
	public InflaterInputStream(InputStream __in, int __sls)
	{
		this(__in, __sls, null);
	}
	
	/**
	 * Initializes the deflate compression stream inflater with a custom
	 * size specified for the sliding window, an optional checksum calculator
	 * may be specified also.
	 *
	 * @param __in The stream to inflate.
	 * @param __sls Custom size to the sliding window.
	 * @param __checksum If not {@code null} then when bytes are read from this
	 * stream they will have their checksum calculated. The checksum is
	 * calculated on the uncompressed bytes.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __checksum}.
	 * @since 2017/08/22
	 */
	public InflaterInputStream(InputStream __in, int __sls,
		Checksum __checksum)
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
		this.window = new SlidingByteWindow(__sls);
		this.checksum = __checksum;
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
	 * @since 2017/08/22
	 */
	@Override
	public long compressedBytes()
	{
		return this._compressedsize;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public boolean detectsEOF()
	{
		return true;
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
		
		// More bytes can be read from the input compressed data because the
		// overflow buffer has been emptied
		boolean eof = this._eof;
		if (!eof && c < __l)
		{
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
				{
					this._eof = true;
					break;
				}
			
				// Otherwise add those bytes
				c += rv;
			}
		}
		
		// Calculate CRC for this output data
		Checksum checksum = this.checksum;
		if (checksum != null)
			checksum.offer(__b, __o, c);
		
		// Count uncompressed size
		if (c > 0)
			this._uncompressedsize += c;
		
		// Return the read count or end of file if the end of the stream has
		// been reached
		// But never leave bytes waiting in the overflow buffer ever
		return (c == 0 && eof && overflow.isEmpty() ? -1 : c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public long uncompressedBytes()
	{
		return this._uncompressedsize;
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
		// Do nothing on EOF
		if (this._eof)
			return -1;
		
		// The target offset on entry
		int enteroff = this._targoff;
		
		// Read the final bit which determines if this is the last block
		int finalhit = __readBits(1, false);
		
		// Read the window type
		int type = __readBits(2, false);
		switch (type)
		{
				// None
			case _TYPE_NO_COMPRESSION:
				__decompressNone();
				break;
				
				// Fixed huffman
			case _TYPE_FIXED_HUFFMAN:
				__decompressFixed();
				break;
				
				// Dynamic huffman
			case _TYPE_DYNAMIC_HUFFMAN:
				__decompressDynamic();
				break;
			
				// Error or unknown
			case _TYPE_ERROR:
			default:
				// {@squirreljme.error BD15 Unknown type or the error type
				// was reached. (The type code used in the stream)}
				throw new IOException(String.format("BD15 %d", type));
		}
		
		// If this was the last block to read, then return EOF if no data
		// was actually read, but mark EOF otherwise
		int rv = (this._targoff - enteroff);
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
	 * Decompress dynamic huffman code.
	 *
	 * @throws On read or decompression errors.
	 * @since 2017/02/25
	 */
	private void __decompressDynamic()
		throws IOException
	{
		// Read the code length parameters
		int dhlit = __readBits(5, false) + 257;
		int dhdist = __readBits(5, false) + 1;
		int dhclen = __readBits(4, false) + 4;
		
		// Read the code length tree
		HuffmanTreeInt codelentree = __decompressDynamicLoadLenTree(dhclen);
		
		// Read the literal and distance trees
		HuffmanTreeInt literaltree = __obtainLiteralTree(),
			distancetree = __obtainDistanceTree();
		__decompressDynamicLoadLitDistTree(codelentree, dhlit, dhdist,
			literaltree, distancetree);
		
		// Decode input
		for (;;)
		{
			// Read code
			int code = literaltree.getValue(this._bitsource);
			
			// Literal byte value
			if (code >= 0 && code <= 255)
				__write(code, 0xFF, false);
		
			// Stop processing
			else if (code == 256)
				return;
		
			// Window based result
			else if (code >= 257 && code <= 285)
				__decompressWindow(__handleLength(code),
					distancetree.getValue(this._bitsource));
			
			// {@squirreljme.error BD16 Illegal dynamic huffman code. (The
			// code.)}
			else
				throw new IOException(String.format("BD16 %d", code));
		}
	}
	
	/**
	 * Reads the literal and distance trees.
	 *
	 * @param __cltree The code length tree.
	 * @param __dhlit The literal count.
	 * @param __dhdist The distance count.
	 * @param __ltree The literal tree.
	 * @param __dtree The distance tree.
	 * @throws IOException On read errors.
	 * @since 2017/02/25
	 */
	private void __decompressDynamicLoadLitDistTree(HuffmanTreeInt __cltree,
		int __dhlit, int __dhdist, HuffmanTreeInt __ltree,
		HuffmanTreeInt __dtree)
		throws IOException
	{
		// Determine the maximum bit count that is used when reading values
		int total = __dhlit + __dhdist;
		
		// Cached, erase the data because later reads may have less
		int[] rawlitdistlens = this._rawlitdistlens;
		for (int i = 0, n = rawlitdistlens.length; i < n; i++)
			rawlitdistlens[i] = 0;
		
		// Read every code
		try
		{
			for (int next = 0; next < total;)
				next += __readCodeBits(__cltree, rawlitdistlens, next);
		}

		// {@squirreljme.error BD17 The compressed stream is
		// damaged by being too short or having an illegal tree
		// access.}				
		catch (NoSuchElementException e)
		{
			throw new IOException("BD17", e);
		}
		
		// Initialize both trees
		__thunkCodeLengthTree(__ltree, rawlitdistlens, 0, __dhlit);
		__thunkCodeLengthTree(__dtree, rawlitdistlens, __dhlit, __dhdist);
	}
	
	/**
	 * Reads the code length tree.
	 *
	 * @param __dhclen The code length size.
	 * @throws IOException On read errors.
	 * @since 2017/02/25
	 */
	private HuffmanTreeInt __decompressDynamicLoadLenTree(int __dhclen)
		throws IOException
	{
		// Target tree
		HuffmanTreeInt codelentree = __obtainCodeLenTree();
		
		// {@squirreljme.error BD18 There may only be at most 19 used
		// code lengths. (The number of code lengths)}
		if (__dhclen > 19)
			throw new IOException(String.format("BD18 %d", __dhclen));
		
		// The same array is used for reading code lengths but the next time
		// around it is possible that less code lengths are read, so if the
		// higher elements have previously been set they will be used
		int[] rawcodelens = this._rawcodelens;
		for (int i = 0, n = rawcodelens.length; i < n; i++)
			rawcodelens[i] = 0;
		
		// Read lengths, they are just 3 bits but their placement values are
		// shuffled since some sequences are more common than others
		int[] hsbits = _SHUFFLE_BITS;
		for (int next = 0; next < __dhclen; next++)
			rawcodelens[hsbits[next]] = __readBits(3, false);
		
		// Thunk the tree and return it
		return __thunkCodeLengthTree(codelentree, rawcodelens, 0,
			rawcodelens.length);
	}
	
	/**
	 * Decodes decompressed data stored with the fixed huffman table and
	 * decompresses it.
	 *
	 * @throws IOException On read or decompression errors.
	 * @since 2017/02/25
	 */
	private void __decompressFixed()
		throws IOException
	{
		// Read until the sequence has ended
		for (;;)
		{
			// Read code
			int code = __readFixedHuffman();
				
			// Literal byte value
			if (code >= 0 && code <= 255)
				__write(code, 0xFF, false);
		
			// Stop processing
			else if (code == 256)
				return;
		
			// Window based result
			else if (code >= 257 && code <= 285)
				__decompressWindow(__handleLength(code), Integer.MIN_VALUE);
		
			// {@squirreljme.error BD19 Illegal fixed huffman code. (The
			// code.)}
			else
				throw new IOException(String.format("BD19 %d", code));
		}
	}
	
	/**
	 * Decompresses uncompressed data.
	 *
	 * @throws IOException On read errors.
	 * @since 2017/02/25
	 */
	private void __decompressNone()
		throws IOException
	{
		// Throw out bits that have been read so that the following reads are
		// aligned to byte boundaries
		int minisub = this._minisize & 7;
		if (minisub > 0)
			__readBits(minisub, false);
		
		// Read length and the one's complement of it
		int len = __readBits(16, false);
		int com = __readBits(16, false);
		
		// The complemented length must be equal to the complement
		// {@squirreljme.error BD1a Value mismatch reading the number of
		// uncompressed symbols that exist. (The length; The complement;
		// The complemented input length; The complemented input complement)}
		if ((len ^ 0xFFFF) != com)
			throw new IOException(String.format("BD1a %04x %04x %04x %04x",
				len, com, len ^ 0xFFFF, com ^ 0xFFFF));
		
		// Read all bytes
		for (int i = 0; i < len; i++)
			__write(__readBits(8, false), 0xFF, false);
	}
	
	/**
	 * Handles decompressing window data.
	 *
	 * @param __len The length to read, must be prehandled.
	 * @param __dist The distance to read.
	 * @throws IOException On read errors.
	 * @since 2017/02/25
	 */
	private void __decompressWindow(int __len, int __dist)
		throws IOException
	{
		// Handle distance
		__dist = __handleDistance(__dist);
	
		// Get the maximum valid length, so for example if the length
		// is 5 and the distance is two, then only read two bytes.
		int maxlen;
		if (__dist < __len)
			maxlen = __dist;
		else
			maxlen = __len;
		
		// Create a byte array from the sliding window data
		byte[] winb = new byte[maxlen];
		try
		{
			this.window.get(__dist, winb, 0, maxlen);
		}
	
		// Bad window read
		catch (IndexOutOfBoundsException ioobe)
		{
			// {@squirreljme.error BD1b Window access out of range.
			// (The distance; The length)}
			throw new IOException(String.format(
				"BD1b %d %d", __dist, __len), ioobe);
		}
	
		// Add those bytes to the output, handle wrapping around if the
		// length is greater than the current position
		for (int i = 0, v = 0; i < __len; i++)
		{
			// Write byte
			__write(winb[v], 0xFF, false);
		
			// Wrap around
			if ((++v) >= maxlen)
				v = 0;
		}
	}
	
	/**
	 * Handles fixed huffman distance.
	 *
	 * @param __code The input code.
	 * @return The ditsance read.
	 * @throws IOException On read errors.
	 * @since 2017/02/25
	 */
	private int __handleDistance(int __code)
		throws IOException
	{
		// Read distance
		if (__code == Integer.MIN_VALUE)
			__code = __readBits(5, true);
		
		// {@squirreljme.error BD1c Illegal fixed distance code. (The distance
		// code)}
		if (__code > 29)
			throw new IOException(String.format("BD1c %d", __code));
		
		// Calculate the required distance to use
		int rv = 1;
		for (int i = 0; i < __code; i++)
		{
			// This uses a similar pattern to the length code, however the
			// division is half the size (so there are groups of 2 now).
			int v = ((i / 2) - 1);
			if (v >= 0)
				rv += (1 << v);
			else
				rv++;
		}
		
		// Determine the number of extra bits that make up the distance which
		// is used as an additional distance value
		int extrabits = ((__code / 2) - 1);
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
		// The maximum length that can ever be used is 258, it has no bits also
		if (__c == 285)
			return 258;
		
		// Get the base code
		int base = __c - 257;
		
		// {@squirreljme.error BD1d Illegal length code. (The length code)}
		if (base < 0)
			throw new IOException(String.format("BD1d %d", __c));
		
		// Calculate the required length to use
		int rv = 3;
		for (int i = 0; i < base; i++)
		{
			// Determine how many groups of 4 the code is long. Since zero
			// appears as items then subtract 1 to make it longer. However
			// after the first 8 it goes up in a standard pattern.
			int v = ((i / 4) - 1);
			if (v > 0)
				rv += (1 << v);
			else
				rv++;
		}
		
		// Add extra bits which are used to modify the amount of data read
		int extrabits = (base / 4) - 1;
		if (extrabits > 0)
			rv += (extrabits = __readBits(extrabits, false));
		
		// Return the length
		return rv;
	}
	
	/**
	 * Obtains the code length tree.
	 *
	 * @return The code length tree.
	 * @since 2017/02/27
	 */
	private HuffmanTreeInt __obtainCodeLenTree()
	{
		Reference<HuffmanTreeInt> ref = this._codelentree;
		HuffmanTreeInt rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._codelentree = new WeakReference<>(
				(rv = new HuffmanTreeInt()));
		
		// Clear before return
		rv.clear();
		return rv;
	}
	
	/**
	 * Obtains the distance tree.
	 *
	 * @return The distance tree.
	 * @since 2017/02/27
	 */
	private HuffmanTreeInt __obtainDistanceTree()
	{
		Reference<HuffmanTreeInt> ref = this._distancetree;
		HuffmanTreeInt rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._distancetree = new WeakReference<>(
				(rv = new HuffmanTreeInt()));
		
		// Clear before return
		rv.clear();
		return rv;
	}
	
	/**
	 * Obtains the literal tree.
	 *
	 * @return The literal tree.
	 * @since 2017/02/27
	 */
	private HuffmanTreeInt __obtainLiteralTree()
	{
		Reference<HuffmanTreeInt> ref = this._literaltree;
		HuffmanTreeInt rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._literaltree = new WeakReference<>(
				(rv = new HuffmanTreeInt()));
		
		// Clear before return
		rv.clear();
		return rv;
	}
	
	/**
	 * Obtains the read window.
	 *
	 * @return The read window.
	 * @since 2017/02/26
	 */
	private byte[] __obtainReadWindow()
	{
		Reference<byte[]> ref = this._readwindow;
		byte[] rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._readwindow = new WeakReference<>(
				(rv = new byte[128]));
		
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
	int __readBits(int __n, boolean __msb)
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
			
			// {@squirreljme.error BD1e Reached EOF while reading bytes to
			// decompress. (Bits in the queue; Requested number of bits)}
			if (rc < 0)
				throw new IOException(String.format("BD1e %d %d", minisize,
					__n));
			
			// Shift in the read bytes to the higher positions
			for (int i = 0; i < rc; i++)
			{
				miniwindow |= ((readin[i] & 0xFF) << minisize);
				minisize += 8;
			}
			
			// Count the number of compressed bytes
			this._compressedsize += rc;
		}
		
		// Mask in the value, which is always at the lower bits
		int rv = miniwindow & ((1 << __n) - 1);
		
		// Shift down the mini window for the next read
		// Make sure the shift down is unsigned so that zeroes are in the
		// higher bits for the filling OR operation.
		miniwindow >>>= __n;
		minisize -= __n;
		
		// Store for next run
		this._miniwindow = miniwindow;
		this._minisize = minisize;
		
		// Want MSB to be first, need to swap all the bits so the lowest ones
		// are at the highest positions
		// Luckily such a method already exists and it could potentially be
		// inlined by the JVM or converted to native code if such an
		// instruction exists.
		if (__msb)
			return Integer.reverse(rv) >>> (32 - __n);
		
		// Return read result
		return rv;
	}
	
	/**
	 * Reads code bits using the given huffman tree and into the specified
	 * array.
	 *
	 * @param __codes The huffman tree which contains the length codes which
	 * the values being read are encoded with.
	 * @param __out The output array.
	 * @param __next The next value to read.
	 * @throws IOException On read or decompression errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/28
	 */
	private int __readCodeBits(HuffmanTreeInt __codes, int[] __out,
		int __next)
		throws IOException, NullPointerException
	{
		// Check
		if (__codes == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Read in code based on an input huffman tree
		int basenext = __next;
		int code = __codes.getValue(this._bitsource);
		
		// Literal length, the input is used
		if (code >= 0 && code < 16)
			__out[__next++] = code;
		
		// Repeating
		else
		{
			// Repeat this value and for this many lengths
			int repval;
			int repfor;
			
			// Repeat the previous length 3-6 times
			if (code == 16)
			{
				// {@squirreljme.error BD1f A repeat code was specified,
				// however this is the first entry. (The last length index)}
				int lastlendx = __next - 1;
				if (lastlendx < 0)
					throw new IOException(String.format("BD1f %d",
						lastlendx));
				
				// Read the last
				repval = __out[lastlendx];
				
				// Read the repeat count
				repfor = 3 + __readBits(2, false);
			}
			
			// Repeat zero for 3-10 times
			else if (code == 17)
			{
				// Use zero
				repval = 0;
				
				// Read 3 bits
				repfor = 3 + __readBits(3, false);
			}
			
			// Repeat zero for 11-138 times
			else if (code == 18)
			{
				// Use zero
				repval = 0;
				
				// Read 7 bits
				repfor = 11 + __readBits(7, false);
			}
			
			// {@squirreljme.error BD1g Illegal code. (The code)}
			else
				throw new IOException(String.format("BD1g %d", code));
			
			// Could fail
			try
			{
				// Place in repeated values
				for (int i = 0; i < repfor; i++)
					__out[__next++] = repval;
			}
			
			// Out of bounds entry
			catch (IndexOutOfBoundsException ioobe)
			{
				// {@squirreljme.error BD1h Out of bounds index read.}
				throw new IOException("BD1h", ioobe);
			}
		}
		
		// Skip count
		return __next - basenext;
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
		if (__readBits(1, true) != 0)
			if (__readBits(1, true) != 0)
				if (__readBits(1, true) != 0)
					return 192 + __readBits(6, true);
				else
					if (__readBits(1, true) != 0)
						return 160 + __readBits(5, true);
					else
						if (__readBits(1, true) != 0)
							return 144 + __readBits(4, true);
						else
							return 280 + __readBits(3, true);
			else
				return 80 + __readBits(6, true);
		else
			if (__readBits(1, true) != 0)
				return 16 + __readBits(6, true);
			else
				if (__readBits(1, true) != 0)
					if (__readBits(1, true) != 0)
						return 0 + __readBits(4, true);
					else
						return 272 + __readBits(3, true);
				else
					return 256 + __readBits(4, true);
	}
	
	/**
	 * Creates a huffman tree from the given code lengths. These generate
	 * symbols which are used to determine how the dynamic huffman data is to
	 * be decoded.
	 *
	 * @param __rv The tree to output.
	 * @param __lens The input code lengths.
	 * @param __o The starting offset.
	 * @param __l The number of lengths to decode.
	 * @return A huffman tree from the code length input.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/28
	 */
	private HuffmanTreeInt __thunkCodeLengthTree(HuffmanTreeInt __tree,
		int[] __lens, int __o, int __l)
		throws NullPointerException
	{
		// Check
		if (__lens == null)
			throw new NullPointerException("NARG");
		
		// Initialize both arrays with zero
		int[] bl_count = this._blcount;
		int[] next_code = this._nextcode;
		for (int i = 0, n = bl_count.length; i < n; i++)
		{
			bl_count[i] = 0;
			next_code[i] = 0;
		}
		
		// Determine the bitlength count for all of the inputs
		for (int i = 0, p = __o; i < __l; i++, p++)
			bl_count[__lens[p]]++;
		bl_count[0] = 0;
		
		// Find the numerical value of the smallest code for each code
		// length.
		int code = 0;
		for (int bits = 1; bits <= _MAX_BITS; bits++)
		{
			code = (code + bl_count[bits - 1]) << 1;
			next_code[bits] = code;
		}
	
		// Assign values to all codes
		__tree.clear();
		for (int q = 0, p = __o; q < __l; q++, p++)
		{
			// Get length
			int len = __lens[p];
		
			// Add code length to the huffman tree
			if (len != 0)
				__tree.add(q, (next_code[len])++, (1 << len) - 1);
		}
		
		// Return it
		return __tree;
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
		
		// Write LSB value, need to swap bits if writing MSB
		__v &= __mask;
		if (__msb)
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
	
	/**
	 * Bit source for huffman reads.
	 *
	 * @since 2017/02/25
	 */
	private final class __BitSource__
		implements BitSource
	{
		/**
		 * {@inheritDoc}
		 * @since 2017/02/25
		 */
		@Override
		public boolean nextBit()
			throws IOException
		{
			return 0 != InflaterInputStream.this.__readBits(1, true);
		}
	}
}

