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

import java.io.ByteArrayOutputStream;
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
	
	/** Output bytes to drain. */
	private final byte[] _drain =
		new byte[3];
	
	/** The current fill buffer. */
	private volatile int _buffer;
	
	/** The number of bits which are in the buffer. */
	private volatile int _bits;
	
	/** Has EOF been reached if the pad has been detected? */
	private volatile boolean _paddedeof;
	
	/** The current output drain position. */
	private volatile int _drained =
		-1;
	
	/** The maximum value for drained values. */
	private volatile int _drainedmax =
		3;
	
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
		
		// Need lookups
		Reader in = this.in;
		boolean ignorepadding = this.ignorepadding;
		char[] alphabet = this._alphabet;
		byte[] ascii = this._ascii;
		byte[] drain = this._drain;
		
		// This buffer is filled into as needed when input characters are read
		int buffer = this._buffer,
			bits = this._bits,
			drained = this._drained,
			drainedmax = this._drainedmax;
		
		// Fill the input in as much as possible
		int base = __o,
			eo = __o + __l;
		while (__o < eo)
		{
			// Enough bits were read to add to the output, so do so
			if (bits >= 24)
			{
				drain[0] = (byte)((buffer & 0xFF0000) >>> 16);
				drain[1] = (byte)((buffer & 0x00FF00) >>> 8);
				drain[2] = (byte)((buffer & 0x0000FF));
				
				// Empty buffer
				buffer = 0;
				bits = 0;
				drained = 0;
			}
			
			// Bytes to drain to the output?
			if (drained >= 0)
			{
				__b[__o++] = drain[drained++];
				
				// No more bytes to drain
				if (drained == drainedmax)
					drained = -1;
				
				// Continue draining
				else
					continue;
			}
			
			// Previous read ended in EOF of padded data
			if (paddedeof)
				break;
			
			// EOF
			int val = 0;
			int ch = in.read();
			if (ch < 0)
			{
				paddedeof = true;
				continue;
			}
			
			// Determine the value of the character
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
				
				// Consume extra equal sign for missing bits
				if (bits < 16)
				{
					int padchar,
						gotchar;
					
					// {@squirreljme.error BD0g Expected padding character to
					// follow for the last remaining bytes. (The expected
					// character; The read character)}
					if ((gotchar = in.read()) != (padchar = alphabet[64]))
						throw new IOException(String.format("BD0g %c %c",
							padchar, gotchar));
				}
				
				// The number of valid bytes in the drained data is only
				// equal to the number of read bytes
				drainedmax = (bits / 8);
				
				// Fill to 24
				buffer <<= (24 - bits);
				bits = 24;
			}
			
			// Is valid character
			else
			{
				// Shift in six bits
				buffer <<= 6;
				buffer |= val;
				bits += 6;
			}
		}
		
		// Store state for next run
		this._buffer = buffer;
		this._bits = bits;
		this._paddedeof = paddedeof;
		this._drained = drained;
		this._drainedmax = drainedmax;
		
		// Return the read count
		int rv = __o - base;
		if (paddedeof && rv == 0)
			return -1;
		return rv;
	}
	
	/**
	 * Decodes the input string to byte values.
	 *
	 * @param __in The string to decode.
	 * @param __ab The alphabet to use.
	 * @return The resulting byte array.
	 * @throws IllegalArgumentException If the input string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/06
	 */
	public static final byte[] decode(String __in, Base64Alphabet __ab)
		throws IllegalArgumentException, NullPointerException
	{
		return Base64Decoder.decode(__in, __ab, false);
	}
	
	/**
	 * Decodes the input string to byte values.
	 *
	 * @param __in The string to decode.
	 * @param __ab The alphabet to use.
	 * @param __ip Is padding ignored?
	 * @return The resulting byte array.
	 * @throws IllegalArgumentException If the input string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/04
	 */
	public static final byte[] decode(String __in, Base64Alphabet __ab,
		boolean __ip)
		throws IllegalArgumentException, NullPointerException
	{
		if (__in == null || __ab == null)
			throw new NullPointerException("NARG");
		
		// Wrap in a reader to decode
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			byte[] buf = new byte[32];
			
			// Loop handle bytes
			try (InputStream in = new Base64Decoder(
				new StringReader(__in), __ab, __ip))
			{
				for (;;)
				{
					int rc = in.read(buf);
					
					// EOF?
					if (rc < 0)
						break;
					
					// Copy
					baos.write(buf, 0, rc);
				}
			}
			
			// Return resulting byte array
			return baos.toByteArray();
		}
		
		// {@squirreljme.error BD0m Could not decode the input string.}
		catch (IOException e)
		{
			throw new IllegalArgumentException("BD0m", e);
		}
	}
}

