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
import java.io.Reader;
import java.util.Arrays;

/**
 * This decodes the base64 character set, ignoring invalid characters, and
 * provides the binary data for the input. If the padding character is reached
 * or if the input stream runs out of characters then EOF is triggered.
 *
 * @since 2018/03/05
 */
public final class Base64Decoder
	extends InputStream
{
	/** The source reader. */
	protected final Reader in;
	
	/** Ignore padding characters. */
	protected final boolean ignorepadding;
	
	/** The alphabet to use for decoding. */
	private final char[] _alphabet;
	
	/** The ASCII map for quick lookup. */
	private final byte[] _ascii;
	
	/** The current fill buffer. */
	private volatile int _buffer;
	
	/** The number of bits which are in the buffer. */
	private volatile int _bits;
	
	/** Has EOF been reached if the pad has been detected? */
	private volatile boolean _paddedeof;
	
	/**
	 * Initializes the decoder using the default alphabet.
	 *
	 * @param __in The input set of characters.
	 * @param __chars The pre-defined character set to use for the alphabet.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public Base64Decoder(Reader __in, Base64Alphabet __chars)
		throws NullPointerException
	{
		this(__in, __chars._alphabet, false);
	}
	
	/**
	 * Initializes the decoder using the specified custom alphabet.
	 *
	 * @param __in The input set of characters.
	 * @param __chars The characters to use for the alphabet.
	 * @throws IllegalArgumentException If the alphabet is of the incorrect
	 * size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public Base64Decoder(Reader __in, String __chars)
		throws IllegalArgumentException, NullPointerException
	{
		this(__in, __chars.toCharArray(), false);
	}
	
	/**
	 * Initializes the decoder using the specified custom alphabet.
	 *
	 * @param __in The input set of characters.
	 * @param __chars The characters to use for the alphabet.
	 * @throws IllegalArgumentException If the alphabet is of the incorrect
	 * size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public Base64Decoder(Reader __in, char[] __chars)
		throws IllegalArgumentException, NullPointerException
	{
		this(__in, __chars, false);
	}
	
	/**
	 * Initializes the decoder using the default alphabet.
	 *
	 * @param __in The input set of characters.
	 * @param __chars The pre-defined character set to use for the alphabet.
	 * @param __ip Ignore padding characters and do not treat them as the end
	 * of the stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public Base64Decoder(Reader __in, Base64Alphabet __chars, boolean __ip)
		throws NullPointerException
	{
		this(__in, __chars._alphabet, __ip);
	}
	
	/**
	 * Initializes the decoder using the specified custom alphabet.
	 *
	 * @param __in The input set of characters.
	 * @param __chars The characters to use for the alphabet.
	 * @param __ip Ignore padding characters and do not treat them as the end
	 * of the stream.
	 * @throws IllegalArgumentException If the alphabet is of the incorrect
	 * size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public Base64Decoder(Reader __in, String __chars, boolean __ip)
		throws IllegalArgumentException, NullPointerException
	{
		this(__in, __chars.toCharArray(), __ip);
	}
	
	/**
	 * Initializes the decoder using the specified custom alphabet.
	 *
	 * @param __in The input set of characters.
	 * @param __chars The characters to use for the alphabet.
	 * @param __ip Ignore padding characters and do not treat them as the end
	 * of the stream.
	 * @throws IllegalArgumentException If the alphabet is of the incorrect
	 * size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public Base64Decoder(Reader __in, char[] __chars, boolean __ip)
		throws IllegalArgumentException, NullPointerException
	{
		if (__in == null || __chars == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.erorr BD0g The alphabet to use for the base64
		// decoder must be 64 characters plus one padding character.
		// (The character count)}
		int n;
		if ((n = __chars.length) != 65)
			throw new IllegalArgumentException(String.format("BD0g %d", n));
		
		// Set
		this.in = __in;
		this.ignorepadding = __ip;
		this._alphabet = (__chars = __chars.clone());
		
		// Build ASCII map for quick in-range character lookup
		byte[] ascii = new byte[128];
		for (int i = 0; i < 128; i++)
			ascii[i] = -1;
		for (int i = 0; i < 65; i++)
		{
			int dx = __chars[i];
			if (dx < 128)
				ascii[dx] = (byte)i;
		}
		this._ascii = ascii;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final int read()
		throws IOException
	{
		byte[] next = new byte[1];
		for (;;)
		{
			int rc = this.read(next, 0, 1);
			if (rc < 0)
				return -1;
			else if (rc == 0)
				continue;
			else if (rc == 1)
				return (next[0] & 0xFF);
			else
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Did a previous read cause a padded EOF?
		boolean paddedeof = this._paddedeof;
		if (paddedeof)
			return -1;
		
		// Need lookups
		Reader in = this.in;
		boolean ignorepadding = this.ignorepadding;
		char[] alphabet = this._alphabet;
		byte[] ascii = this._ascii;
		
		// This buffer is filled into as needed when input characters are read
		int buffer = this._buffer,
			bits = this._bits;
		
		// Fill the input in as much as possible
		int base = __o,
			eo = __o + __l;
		boolean goteof = false;
		while (__o < eo)
		{
			// EOF
			int ch = in.read();
			if (ch < 0)
			{
				goteof = true;
				break;
			}
			
			// Determine the value of the character
			int val;
			if (ch < 128)
				val = ascii[ch];
			else
			{
				for (val = 0; val < 65; val++)
					if (ch == alphabet[val])
						break;
				if (val == 65)
					val = -1;
			}
			
			// Value is not valid, ignore it
			if (val < 0)
				continue;
			
			// Special padding value, indicates EOF
			if (val == 64)
			{
				// Treat as an invalid character
				if (ignorepadding)
					continue;
				
				// Trigger padded EOF
				paddedeof = true;
				
				// Finish with the bytes in the buffer
				__b[__o++] = (byte)(buffer & 0xFF);
			}
			
			else
			{
				// Shift in six bits
				buffer <<= 6;
				buffer |= val;
				bits += 6;
			
				// Read enough bits to output data
				if (bits >= 8)
				{
					__b[__o++] = (byte)(buffer & 0xFF);
					buffer >>>= 8;
					bits -= 8;
				}
			}
		}
		
		// Store state for next run
		this._buffer = buffer;
		this._bits = bits;
		this._paddedeof = paddedeof;
		
		// Return the read count
		int rv = __o - base;
		if (goteof && rv == 0)
			return -1;
		return rv;
	}
}

