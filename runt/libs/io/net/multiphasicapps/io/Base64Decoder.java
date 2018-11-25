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
	private volatile boolean _readeof;
	
	/** The current output drain position. */
	private volatile int _drained =
		-1;
	
	/** The maximum value for drained values. */
	private volatile int _drainedmax =
		-1;
	
	/**
	 * Initializes the decode the default MIME alphabet.
	 *
	 * @param __in The input set of characters.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/23
	 */
	public Base64Decoder(Reader __in)
	{
		this(__in, Base64Alphabet.BASIC);
	}
	
	/**
	 * Initializes the decoder using the specified alphabet.
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
	 * @since 2018/11/25
	 */
	@Override
	public final int available()
		throws IOException
	{
		int drained = this._drained;
		
		// There are bytes which are ready and in the drain that we do not
		// need to block reading them?
		if (drained != -1)
			return this._drainedmax - drained;
		return 0;
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
		// If there is stuff to be drained, quickly drain that so we do not
		// need to go deeper into the heavier method
		int drained = this._drained;
		if (drained != -1)
		{
			// Read in drained character
			int rv = this._drain[drained++] & 0xFF;
			
			// Reached the drain limit?
			if (drained == this._drainedmax)
			{
				this._drained = -1;
				this._drainedmax = -1;
			}
			
			// Would still be drain
			else
				this._drained = drained;
			
			// Return the value
			return rv;
		}
		
		// Previously read EOF, so this will just return EOF
		if (this._readeof)
			return -1;
		
		// Otherwise decode and read
		byte[] next = new byte[1];
		for (;;)
		{
			int rc = this.read(next, 0, 1);
			
			// EOF?
			if (rc < 0)
				return -1;
			
			// Missed read
			else if (rc == 0)
				continue;
			
			return (next[0] & 0xFF);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public final int read(byte[] __b)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return this.read(__b, 0, __b.length);
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
		boolean readeof = this._readeof;
		
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
		
		// Keep trying to fill bytes in
		int rv = 0;
		while (rv < __l)
		{
			// Still need to drain bytes away
			if (drained != -1 && drained < drainedmax)
			{
				// Drain it
				__b[__o++] = drain[drained++];
				rv++;
				
				// Drained all the characters
				if (drained == drainedmax)
					drained = drainedmax = -1;
				
				// Try again
				else
					continue;
			}
			
			// EOF was reached
			if (readeof)
				break;
			
			// Read in character and decode it
			int ch = in.read();
			
			// Is EOF?
			if (ch < 0)
			{
				// {@squirreljme.error BD20 Read EOF from input when there
				// were expected to be more characters or the ending padding
				// character. (The bits in the buffer)}
				if (bits != 0)
					throw new IOException("BD20 " + bits);
				
				// Did read EOF
				readeof = true;
				break;
			}
			
			// Determine the value of the character
			if (ch < 128)
				ch = ascii[ch];
			else
			{
				ch = -1;
				for (int i = 0; i < 65; i++)
					if (i == alphabet[i])
					{
						ch = i;
						break;
					}
			}
			
			// Invalid, ignore and continue
			if (ch == -1 || (ignorepadding && ch == 64))
				continue;
			
			// Decoded padding character
			else if (ch == 64)
			{
				// {@squirreljme.error BD21 Did not expect a padding character.
				// (The number of decoded bits in queue)}
				if (bits == 0 || bits == 24)
					throw new IOException("BD21 " + bits);
				
				// Only want to store a single extra byte since that is
				// all that is valid
				else if (bits < 16)
				{
					// {@squirreljme.error BD22 Expected another padding
					// character.}
					if (in.read() != alphabet[64])
						throw new IOException("BD22");
					
					drain[0] = (byte)(buffer >>> 4);
					
					drainedmax = 1;
				}
				
				// Otherwise there will be two characters to drain
				else
				{
					drain[0] = (byte)(buffer >>> 10);
					drain[1] = (byte)(buffer >>> 2);
					
					drainedmax = 2;
				}
				
				// Need to drain all
				drained = 0;
					
				// Clear the buffer
				buffer = bits = 0;
				
				// Did read EOF
				readeof = true;
			}
			
			// Normal data
			else
			{
				// Shift in six bits
				buffer <<= 6;
				buffer |= ch;
				bits += 6;
				
				// Drain and empty the buffer
				if (bits == 24)
				{
					// Fill the drain
					drain[0] = (byte)(buffer >>> 16);
					drain[1] = (byte)(buffer >>> 8);
					drain[2] = (byte)buffer;
					
					// Set these to drain
					drained = 0;
					drainedmax = 3;
					
					// Clear the buffer
					buffer = bits = 0;
				}
			}
		}
		
		// Store state for next run
		this._buffer = buffer;
		this._bits = bits;
		this._readeof = readeof;
		this._drained = drained;
		this._drainedmax = drainedmax;
		
		// Return the read count
		if (readeof && rv == 0)
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
		
		// {@squirreljme.error BD02 Could not decode the input string.}
		catch (IOException e)
		{
			throw new IllegalArgumentException("BD02", e);
		}
	}
}

