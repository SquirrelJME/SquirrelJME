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

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This class supports decompressing ZLib streams.
 *
 * Associated standards:
 * {@link https://www.ietf.org/rfc/rfc1950.txt}.
 *
 * This class is not thread safe.
 *
 * @since 2017/03/04
 */
public class ZLibDecompressor
	extends DecompressionInputStream
{
	/** Compression method mask. */
	private static final int _CMF_COMPRESSION_METHOD_MASK =
		0b00001111;
	
	/** Compression info mask. */
	private static final int _CMF_COMPRESSION_INFO_MASK =
		0b11110000;
	
	/** Compression info shift. */
	private static final int _CMF_COMPRESSION_INFO_SHIFT =
		4;
	
	/** Deflate compression method. */
	private static final int _CMF_METHOD_DEFLATE =
		8;
	
	/** Is a preset dictionary being used? */
	private static final int _FLAG_PRESET_DICTIONARY =
		1 << 5;
	
	/** The source stream. */
	protected final DataInputStream in;
	
	/** Single byte array to be shared for single reads. */
	private final byte[] _solo =
		new byte[1];
	
	/** The current checksum being calculated. */
	private final Adler32Calculator _checksum =
		new Adler32Calculator();
	
	/** Current stream to read data from, will change for blocks. */
	private volatile DecompressionInputStream _current;
	
	/** Has EOF been read? */
	private volatile boolean _eof;
	
	/** The number of uncompressed bytes. */
	private volatile long _uncomp;
	
	/** The base number of compressed bytes. */
	private volatile long _basecomp;
	
	/**
	 * Initializes the ZLib decompressor.
	 *
	 * @param __in The stream to read data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/04
	 */
	public ZLibDecompressor(InputStream __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = new DataInputStream(__in);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/04
	 */
	@Override
	public int available()
		throws IOException
	{
		// If the current stream is known, it is possible that the number
		// of available bytes will be known from it
		InputStream current = this._current;
		if (current != null)
			return current.available();
		
		// Otherwise no amount is known
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/04
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
		
		// Close the current stream also
		InputStream current = this._current;
		if (current != null)
			current.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public long compressedBytes()
	{
		// It is really hard to tell how many compressed bytes were read so
		// if there is a current stream then use it from the base because
		// otherwise it would be really unknown how many bytes were truly
		// read from the compressed stream
		long basecomp = this._basecomp;
		DecompressionInputStream current = this._current;
		if (current != null)
			return basecomp + current.compressedBytes();
		return basecomp;
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
	 * @since 2017/03/04
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
	 * @since 2017/03/04
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Read EOF?
		if (this._eof)
			return -1;
		
		// Try to fill the buffer up as much as possible
		int rv = 0;
		boolean eof = false;
		DataInputStream in = this.in;
		DecompressionInputStream current = this._current;
		Adler32Calculator checksum = this._checksum;
		for (int at = __o, rest = __l; rv < __l;)
		{
			// There is a current stream being read
			if (current != null)
			{
				int rc = current.read(__b, at, rest);
				
				// If EOF is reached then the checksum must be checked
				if (rc < 0)
				{
					// The number of compressed bytes always acts as a base
					// since it is unknown how many bytes were read by the
					// decompression stream. So when it has ended it a new base
					// must be set for every byte which was read.
					this._basecomp += current.compressedBytes();
					
					// {@squirreljme.error BF27 The checksum for the ZLib
					// stream is not valid. (The desired checksum; The actual
					// checksum)}
					int want = in.readInt(),
						was = checksum.checksum();
					if (want != was)
						throw new IOException(String.format("BF27 %08x %08x",
							want, was));
					
					// This is the checksum
					this._basecomp += 4;
					
					// Clear current because no more data can be read from
					// it
					current = null;
					this._current = null;
				}
				
				// Otherwise consume those bytes
				else
				{
					at += rc;
					rv += rc;
					rest -= rc;
					
					// Count those bytes as uncompressed
					this._uncomp += rc;
				}
			}
			
			// Otherwise try to see if there is data to be read
			else
			{
				// Compression method and flags
				int cmf = in.read();
				
				// The end of stream could be at this point at which point it
				// is acceptable to stop reading data
				if (cmf < 0)
				{
					eof = true;
					this._eof = true;
					break;
				}
				
				// Count single compressed byte
				this._basecomp++;
				
				// {@squirreljme.error BF28 Only deflate compressed ZLib
				// streams are supported. (The compression method used)}
				int method = (cmf & _CMF_COMPRESSION_METHOD_MASK);
				if (_CMF_METHOD_DEFLATE != method)
					throw new IOException(String.format("BF28 %d", method));
				
				// {@squirreljme.error BF29 The specified binary logarithm
				// specified for the sliding window is not valid. (The binary
				// logarithm of the sliding window)}
				// The specification says that higher sliding windows are not
				// allowed, but skirt that requirement
				int slwin = ((cmf & _CMF_COMPRESSION_INFO_MASK) >>>
					_CMF_COMPRESSION_INFO_SHIFT) + 8;
				if (slwin < 0 || slwin > 30)
					throw new IOException(String.format("BF29 %d", slwin));
				
				// Shift up
				slwin = 1 << slwin;
				
				// Read more flags
				int mf = in.readUnsignedByte();
				
				// Count single compressed byte
				this._basecomp++;
				
				// {@squirreljme.error BF2a The checksum for the starting
				// ZLib header is not a multiple of 31. (The checksum
				// remainder)}
				// This is a basic check to ensure that in most cases that the
				// header of the ZLib chunk is not corrupt.
				int was = ((cmf * 256) + mf) % 31;
				if (was != 0)
					throw new IOException(String.format("BF2a %d", was));
				
				// {@squirreljme.error BF2b Preset dictionaries in ZLib
				// streams are not supported.}
				if ((mf & _FLAG_PRESET_DICTIONARY) != 0)
					throw new IOException("BF2b");
				
				// Setup inflate stream
				checksum.reset();
				current = new InflaterInputStream(in, slwin, checksum);
				this._current = current;
			}
		}
		
		// Return EOF or the read count
		return (eof && rv == 0 ? -1 : rv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public long uncompressedBytes()
	{
		return this._uncomp;
	}
}

