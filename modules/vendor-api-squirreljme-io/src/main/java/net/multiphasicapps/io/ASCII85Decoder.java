// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Decodes ASCII85.
 *
 * @since 2024/06/09
 */
public class ASCII85Decoder
	extends InputStream
{
	/** Divisor for characters. */
	private static final int _DIVISOR =
		85;
	
	/** The character data to read from. */
	protected final Reader in;
	
	/** Single byte read. */
	private final byte[] _single =
		new byte[1];
	
	/** The current bit buffer. */
	private volatile int _buffer;
	
	/** The current read index. */
	private volatile int _index;
	
	/** The number of bits left in the output. */
	private volatile int _leftBits;
	
	/** Stop at this number of left bits. */
	private volatile int _leftStop;
	
	/** Was EOF hit? */
	private volatile boolean _eof;
	
	/**
	 * Initializes the decoder.
	 *
	 * @param __in The input character data.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/09
	 */
	public ASCII85Decoder(Reader __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/09
	 */
	@Override
	public int read()
		throws IOException
	{
		byte[] single = this._single;
		for (;;)
		{
			int rc = this.read(single, 0, 1);
			
			if (rc < 0)
				return -1;
			else if (rc == 1)
				return single[0] & 0xFF;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/09
	 */
	@Override
	public int read(byte[] __b)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return this.read(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/09
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length ||
			(__o + __l) < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Pointless?
		if (__l == 0)
			return 0;
		
		// Where we are reading from
		Reader in = this.in;
		
		// Load in existing state
		int buffer = this._buffer;
		int index = this._index;
		int leftBits = this._leftBits;
		int leftStop = this._leftStop;
		boolean eof = this._eof;
		try
		{
			// Read the number of requested bytes
			int fillCount = 0;
			while (fillCount < __l)
			{
				// Bits are left in the buffer?
				if (leftBits > leftStop)
				{
					// Store shifted in value
					__b[__o + (fillCount++)] = (byte)(buffer >>>
						(leftBits - 8));
					leftBits -= 8;
					
					// Skip if there still bits left
					if (leftBits > leftStop)
						continue;
					
					// Clear for future decoding
					buffer = 0;
					index = 0;
					
					// These need to be cleared in the event leftStop is
					// used for output trimming
					leftBits = 0;
					leftStop = 0;
				}
				
				// Previously was EOF? Needs to be checked after we read
				// since we want to drain any bits that are left
				if (eof)
					break;
				
				// Read in encoded character
				int c = in.read();
				
				// EOF?
				if (c < 0)
				{
					// Incomplete sequence, need to decode that
					if (index > 0)
					{
						// Always fill in
						int was = index;
						while (index < 5)
						{
							// These are filled implicitly with 'u' digits
							// which basically just fill with ones
							buffer = (buffer * ASCII85Decoder._DIVISOR) +
								('u' - '!');
							index++;
						}
						
						// Make all these bits available
						leftBits = 32;
						
						// At the final end, whatever padding we add we need
						// to just remove
						leftStop = 8 * (5 - was);
					}
					
					// Mark EOF, run loop again to drain bits
					eof = true;
					continue;
				}
				
				// Ignore whitespace and control characters
				else if (c <= ' ')
					continue;
				
				// All zero group?
				else if (c == 'z')
				{
					// The index must always be zero since this represents
					// a full group
					/* {@squirreljme.error BD09 Zero groups may only
					appear on their own and not with any group.} */
					if (index != 0)
						throw new IOException(
							ErrorCode.__error__("BD09 %d", index));
					
					// Set values directly
					buffer = 0;
					leftBits = 32;
				}
				
				// Otherwise decode in
				else
				{
					// Multiply in
					buffer = (buffer * ASCII85Decoder._DIVISOR) +
						(c - '!');
					index++;
					
					// Full sequence?
					if (index == 5)
						leftBits = 32;
				}
			}
			
			// Return whatever we read in, do also check for EOF read
			if (fillCount == 0 && eof)
				return -1;
			return fillCount;
		}
		
		// Always store these back for the next read
		finally
		{
			this._buffer = buffer;
			this._index = index;
			this._leftBits = leftBits;
			this._leftStop = leftStop;
			this._eof = eof;
		}
	}
}
