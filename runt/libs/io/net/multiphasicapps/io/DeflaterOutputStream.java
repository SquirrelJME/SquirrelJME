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

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is used to compress to standard deflate streams.
 *
 * Associated standards:
 * {@link https://www.ietf.org/rfc/rfc1951.txt}.
 *
 * This class is not thread safe.
 *
 * @since 2018/11/10
 */
public class DeflaterOutputStream
	extends OutputStream
	implements CompressionStream
{
	/** Stream to write compressed data to. */
	protected final OutputStream out;
	
	/** The block size to compress for. */
	private final int _blocksize;
	
	/** The bytes to process first. */
	private final byte[] _fill;
	
	/** The number of bytes in the fill. */
	private int _fillbytes;
	
	/** Has this been closed? */
	private boolean _closed;
	
	/** Compressed bytes. */
	private long _ncompressed;
	
	/** Uncompressed bytes. */
	private long _nuncompressed;
	
	/** The temporary bits for output. */
	private int _wout;
	
	/** The number to bits available to the output. */
	private int _wbits;
	
	/**
	 * Initializes the deflation stream.
	 *
	 * @param __os The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public DeflaterOutputStream(OutputStream __os)
		throws NullPointerException
	{
		this(__os, CompressionLevel.DEFAULT);
	}
	
	/**
	 * Initializes the deflation stream.
	 *
	 * @param __os The output stream.
	 * @param __cl The compression level to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public DeflaterOutputStream(OutputStream __os, CompressionLevel __cl)
		throws NullPointerException
	{
		if (__os == null || __cl == null)
			throw new NullPointerException("NARG");
		
		this.out = __os;
		
		// Process data by blocks for efficiency
		int blocksize = __cl.blockSize();
		this._fill = new byte[blocksize];
		this._blocksize = blocksize;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final void close()
		throws IOException
	{
		// Only close once!
		if (!this._closed)
		{
			// Is closed
			this._closed = true;
			
			// Process any fill remaining so it gets compressed
			if (this._fillbytes > 0)
				this.__processFill();
			
			// Mark final block
			this.__bitOut(1, 1, false);
			
			// Fixed huffman
			this.__bitOut(InflaterInputStream._TYPE_FIXED_HUFFMAN, 2, false);
			
			// Write code 256 which means to end processing the data, this
			// is just 0b000 + 0b0000 for the offset value
			this.__bitOut(0b000_0000, 7, true);
			
			// Pad to 8 bytes so partial bits for the end are not lost
			this.__bitPad(8);
			
			// Perform final flushing before closing to make sure everything
			// is written
			this.__bitFlush();
			this.flush();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final long compressedBytes()
	{
		return this._ncompressed;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final void flush()
		throws IOException
	{
		// Flush all the bits
		this.__bitFlush();
		
		// Then flush the stream itself
		this.out.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final long uncompressedBytes()
	{
		return this._nuncompressed;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		// Just forward write call since it is easier
		this.write(new byte[]{(byte)__b}, 0, 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/10
	 */
	@Override
	public final void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		byte[] fill = this._fill;
		int blocksize = this._blocksize,
			fillbytes = this._fillbytes;
		
		// Write into the fill buffer, but do fill in chunks since that is
		// more optimized
		boolean addedfill = false;
		while (__l > 0)
		{
			// We can only fit so many bytes in the fill before it is full
			int leftinfill = Math.min(blocksize - fillbytes, __l);
			
			// Copy bytes into the fill
			for (int i = 0; i < leftinfill; i++)
				fill[fillbytes++] = __b[__o++];
			
			// Length is dropped by the fill
			__l -= leftinfill;
			
			// Process entire block of bytes
			if (fillbytes == blocksize)
			{
				// Need to store the number of bytes written before we
				// continue
				this._fillbytes = fillbytes;
				this.__processFill();
				
				// Since the entire fill was drained, this would have been
				// reset
				fillbytes = 0;
			}
			
			// Fill would have been added, so need to say that there are
			// bytes in here currently
			else
				addedfill = true;
		}
		
		// These original bytes were added
		this._nuncompressed += __l;
		
		// Bytes were added to the fill, record those
		if (addedfill)
			this._fillbytes = fillbytes;
	}
	
	/**
	 * Flushes the input bits to the output stream.
	 *
	 * @throws IOException On write errors.
	 * @since 2018/11/01
	 */
	final void __bitFlush()
		throws IOException
	{
		// Only write if there are enough bits to write
		int wbits = this._wbits;
		if (wbits >= 8)
		{
			OutputStream out = this.out;
			int wout = this._wout;
			long ncompressed = this._ncompressed;
			
			// Send to the output
			while (wbits >= 8)
			{
				// Send to output
				out.write(wout & 0xFF);
				
				// Clip down
				wout >>>= 8;
				wbits -= 8;
				
				// Single byte was written
				ncompressed++;
			}
			
			// Store new values
			this._wbits = wbits;
			this._wout = wout;
			this._ncompressed = ncompressed;
		}
	}
	
	/**
	 * Writes the specified bits to the output.
	 *
	 * @param __v The value to write.
	 * @param __n The number of bits to store.
	 * @param __msb Is the most significant bit first?
	 * @throws IOException On write errors.
	 * @since 2018/11/10
	 */
	final void __bitOut(int __v, int __n, boolean __msb)
		throws IOException
	{
		// If writing with the most significant bit first, flip
		if (__msb)
			__v = Integer.reverse(__v) >>> (32 - __n);
		
		// Bit storage
		int wout = this._wout,
			wbits = this._wbits;
		
		// Need to mask off so sign values do not mess anything up above
		int mask = (1 << __n) - 1;
		
		// Add the new value to the top of the bits
		wout |= (__v & mask) << wbits;
		wbits += __n;
		
		// Store for next cycle (or out flush)
		this._wout = wout;
		this._wbits = wbits;
		
		// There are too many bits in the output, so send them to the stream
		// accordingly
		if (wbits >= 24)
			this.__bitFlush();
	}
	
	/**
	 * Pads the output bits the given number.
	 *
	 * @param __n The number of bits to pad to.
	 * @throws IOException On write errors.
	 * @since 2018/11/10
	 */
	final void __bitPad(int __n)
		throws IOException
	{
		// If we have two bits 0b00 and we pad to 8 we want 0b00000000 then
		// we add six extra bits. But if we are at 8 already we just keep
		// it as is. In this code wbits is 2, pad is 8... 2 % 8 is 2, then
		// we just take 2 from 8 and we get 6.
		int wbits = this._wbits,
			rem = wbits % __n;
		this._wbits = wbits + (__n - rem);
	}
	
	/**
	 * Processes the bytes which are in the fill buffer.
	 *
	 * @throws IOException On write errors.
	 * @since 2018/11/10
	 */
	final void __processFill()
		throws IOException
	{
		// Get fill parameters
		byte[] fill = this._fill;
		int fillbytes = this._fillbytes;
		
		// Determine the best way to handle this block
		int hufftype = 0;
		
		// Compress with fixed table
		if (hufftype == 1)
		{
			throw new todo.TODO();
		}
		
		// Compress with dynamically generated table
		else if (hufftype == 2)
		{
			throw new todo.TODO();
		}
		
		// No compression used
		else
		{
			// Write all the bytes with no compression at all
			// Write no-compression marker, stream not ended yet
			this.__bitOut(0, 1, false);
			this.__bitOut(InflaterInputStream._TYPE_NO_COMPRESSION, 2, false);
			
			// Pad because byte boundary
			this.__bitPad(8);
			
			// Length and complement of that
			this.__bitOut(fillbytes, 16, false);
			this.__bitOut(fillbytes ^ 0xFFFF, 16, false);
			
			// Then write every individual byte
			for (int i = 0; i < fillbytes; i++)
				this.__bitOut(fill[i], 8, false);
		}
		
		// Remove the fill
		this._fillbytes = 0;
	}
}

