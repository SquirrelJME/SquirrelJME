// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * This encodes binary data to a base64 representation of that data.
 *
 * @since 2021/05/22
 */
public final class Base64Encoder
	extends Reader
{
	/** Character mask. */
	private static final int _CHAR_MASK =
		0b111_111;
	
	/** Character shift. */
	private static final int _BIT_COUNT =
		6;
	
	/** The stream to read from. */
	protected final InputStream in;
	
	/** The alphabet. */
	private final char[] _alphabet;
	
	/** The current bit stream that is left over. */
	int _bitStream;
	
	/** The number of bits in the stream. */
	byte _count;
	
	/** Was EOF hit? */
	boolean _hitEof;
	
	/** Padding that is left to output, when EOF. */
	byte _paddingLeft =
		-1;
	
	/**
	 * Initializes the base64 encoder, using the basic alphabet.
	 * 
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/22
	 */
	public Base64Encoder(InputStream __in)
		throws NullPointerException
	{
		this(__in, Base64Alphabet.BASIC);
	}
	
	/**
	 * Initializes the base64 encoder.
	 * 
	 * @param __in The stream to read from.
	 * @param __alphabet The alphabet to encode/decode from.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/22
	 */
	public Base64Encoder(InputStream __in, Base64Alphabet __alphabet)
		throws NullPointerException
	{
		if (__in == null || __alphabet == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
		this._alphabet = __alphabet._alphabet;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/22
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/22
	 */
	@Override
	public int read(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
			
		InputStream in = this.in;
		char[] alphabet = this._alphabet;
		
		int bitStream = this._bitStream;
		byte count = this._count;
		boolean hitEof = this._hitEof;
		int paddingLeft = this._paddingLeft;
		
		// We want to restore the data fields back when we are done reading
		try
		{
			int converted = 0;
			while (converted < __l)
			{
				// Is there enough bits to eat?
				if (count >= Base64Encoder._BIT_COUNT || hitEof)
				{
					// Is there padding left to put in
					if (paddingLeft > 0)
					{
						__c[__o + (converted++)] = '=';
						
						// We took this padding
						paddingLeft--;
						continue;
					}
					
					// No data left to encode? Stop now
					if (hitEof && (count == 0 || paddingLeft == 0))
						break;
					
					// Determine the character
					__c[__o + (converted++)] =
						alphabet[bitStream & Base64Encoder._CHAR_MASK];
					
					// Determine the amount of padding to add
					if (hitEof)
						paddingLeft = (count == 0 || count == 24 ? 0 :
							(count > 16 ? 1 : 2));
					
					// Eat up the bit stream
					bitStream >>>= Base64Encoder._BIT_COUNT;
					count -= Base64Encoder._BIT_COUNT;
					
					// No point reading if we already know to stop
					if (hitEof)
						continue;
				}
				
				// Read in more data
				int read = in.read();
				
				// If EOF hit, then we will pad
				if (read < 0)
					hitEof = true;
				
				// Add on top of the stream
				else
				{
					bitStream |= (read & 0xFF) << count;
					count += 8;
				}
			}
			
			return (hitEof && converted == 0 ? -1 : converted);
		}
		
		// Always store the fields back when done
		finally
		{
			this._bitStream = bitStream;
			this._count = count;
			this._hitEof = hitEof;
			this._paddingLeft = (byte)paddingLeft;
		}
	}
}
