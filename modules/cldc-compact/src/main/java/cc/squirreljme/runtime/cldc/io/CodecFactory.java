// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import cc.squirreljme.jvm.BuiltInEncoding;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.UnsupportedEncodingException;

/**
 * This contains the code used to obtain the default encoding as well as
 * obtaining the encoder or decoder as needed.
 *
 * @since 2018/09/16
 */
public final class CodecFactory
{
	/** The encoding to use if it is unknown or not set anywhere. */
	public static final String FALLBACK_ENCODING =
		"utf-8";
	
	/**
	 * Not used.
	 *
	 * @since 2018/09/16
	 */
	private CodecFactory()
	{
	}
	
	/**
	 * Returns a decoder that is based on a built-in one.
	 *
	 * @param __builtIn The {@link BuiltInEncoding}.
	 * @return The encoder that is built in.
	 * @since 2020/04/09
	 */
	public static Decoder decoder(int __builtIn)
	{
		switch (__builtIn)
		{
			case BuiltInEncoding.ASCII:
				return new ASCIIDecoder();
				
			case BuiltInEncoding.IBM037:
				throw Debugging.todo();
				
			case BuiltInEncoding.ISO_8859_1:
				return new ISO88591Decoder();
				
			case BuiltInEncoding.ISO_8859_15:
				return new ISO885915Decoder();
			
			case BuiltInEncoding.UTF8:
				return new UTF8Decoder();
			
				// {@squirreljme.error ZZ0Z Unknown encoding. (The encoding)}
			default:
				throw new IllegalArgumentException("ZZ0Z " + __builtIn);
		}
	}
	
	/**
	 * Returns a decoder for the given encoding.
	 *
	 * @param __enc The encoding to decode for.
	 * @return The decoder for the given encoding.
	 * @throws NullPointerException On null arguments.
	 * @throws UnsupportedEncodingException If the encoding is not supported.
	 * @since 2018/10/13
	 */
	public static final Decoder decoder(String __enc)
		throws NullPointerException, UnsupportedEncodingException
	{
		if (__enc == null)
			throw new NullPointerException("NARG");
		
		// Normalization makes it easier to match
		switch ((__enc = CodecFactory.__normalizeEncodingName(__enc)))
		{
				// ASCII
			case "ascii":
				return new ASCIIDecoder();
				
				// IBM037
			case "ibm037":
				throw new todo.TODO();

				// ISO-8859-1
			case "iso-8859-1":
				return new ISO88591Decoder();
				
				// ISO-8859-15
			case "iso-8859-15":
				return new ISO885915Decoder();
				
				// UTF-8
			case "utf-8":
				return new UTF8Decoder();
			
				// {@squirreljme.error ZZ01 Unknown encoding. (The input
				// encoding)}
			default:
				throw new UnsupportedEncodingException(
					String.format("ZZ01 %s", __enc));
		}
	}
	
	/**
	 * Returns a decoder for the given encoding.
	 *
	 * @param __enc The encoding to decode for.
	 * @return The decoder for the given encoding.
	 * @throws NullPointerException On null arguments.
	 * @throws RuntimeException If the encoding is not supported.
	 * @since 2018/10/13
	 */
	public static final Decoder decoderUnchecked(String __enc)
		throws NullPointerException, RuntimeException
	{
		if (__enc == null)
			throw new NullPointerException("NARG");
		
		// Could fail
		try
		{
			return CodecFactory.decoder(__enc);
		}
		
		// {@squirreljme.error ZZ02 Unknown or unsupported encoding.
		// (The encoding)}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(String.format("ZZ02 %s", __enc), e);
		}
	}
	
	/**
	 * Returns the default decoder.
	 *
	 * @return The default decoder.
	 * @since 2018/10/13
	 */
	public static Decoder defaultDecoder()
	{
		return CodecFactory.decoder(Debugging.<Integer>todoObject());
	}
	
	/**
	 * Returns the default system encoder.
	 *
	 * @return The default encoder.
	 * @since 2018/09/16
	 */
	public static Encoder defaultEncoder()
	{
		return CodecFactory.encoder(Debugging.<Integer>todoObject());
	}
	
	/**
	 * Returns a decoder that is based on a built-in one.
	 *
	 * @param __builtIn The {@link BuiltInEncoding}.
	 * @return The encoder that is built in.
	 * @since 2020/04/09
	 */
	public static Encoder encoder(int __builtIn)
	{
		switch (__builtIn)
		{
			case BuiltInEncoding.ASCII:
				return new ASCIIEncoder();
				
			case BuiltInEncoding.IBM037:
				return new IBM037Encoder();
				
			case BuiltInEncoding.ISO_8859_1:
				return new ISO88591Encoder();
				
			case BuiltInEncoding.ISO_8859_15:
				return new ISO885915Encoder();
			
			case BuiltInEncoding.UTF8:
				return new UTF8Encoder();
			
				// {@squirreljme.error ZZ3Y Unknown encoding. (The encoding)}
			default:
				throw new IllegalArgumentException("ZZ3Y " + __builtIn);
		}
	}
	
	/**
	 * Returns the specified encoder.
	 *
	 * @param __enc The encoding to encode to.
	 * @return The encoder for the given encoding.
	 * @throws NullPointerException On null arguments.
	 * @throws UnsupportedEncodingException If the encoding is not supported.
	 * @since 2018/09/17
	 */
	public static final Encoder encoder(String __enc)
		throws NullPointerException, UnsupportedEncodingException
	{
		if (__enc == null)
			throw new NullPointerException("NARG");
		
		// Normalization makes it easier to match
		switch ((__enc = CodecFactory.__normalizeEncodingName(__enc)))
		{
				// ASCII
			case "ascii":
				return new ASCIIEncoder();
				
				// IBM037
			case "ibm037":
				return new IBM037Encoder();

				// ISO-8859-1
			case "iso-8859-1":
				return new ISO88591Encoder();
				
				// ISO-8859-15
			case "iso-8859-15":
				return new ISO885915Encoder();
				
				// UTF-8
			case "utf-8":
				return new UTF8Encoder();
			
				// {@squirreljme.error ZZ03 Unknown encoding. (The output
				// encoding)}
			default:
				throw new UnsupportedEncodingException(
					String.format("ZZ03 %s", __enc));
		}
	}
	
	/**
	 * Returns the specified encoder.
	 *
	 * @param __enc The encoding to encode to.
	 * @return The encoder for the given encoding.
	 * @throws NullPointerException On null arguments.
	 * @throws RuntimeException If the encoding is not valid.
	 * @since 2018/09/17
	 */
	public static final Encoder encoderUnchecked(String __enc)
		throws NullPointerException, RuntimeException
	{
		if (__enc == null)
			throw new NullPointerException("NARG");
		
		// Could fail
		try
		{
			return CodecFactory.encoder(__enc);
		}
		
		// {@squirreljme.error ZZ04 Unknown or unsupported encoding.
		// (The encoding)}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(String.format("ZZ04 %s", __enc), e);
		}
	}
	
	/**
	 * Normalizes the name of the encoding since there are so many aliases that
	 * way this code can operate very simply.
	 *
	 * @param __n The encoding to normalize.
	 * @return The normalized encoding, if it is not known then the input is
	 * returned.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/19
	 */
	private static String __normalizeEncodingName(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Lowercase and map values
		switch (__n.toLowerCase())
		{
				// ASCII
			case "646":
			case "ansi_x3.4-1968":
			case "ansi_x3.4-1986":
			case "ascii":
			case "ascii7":
			case "cp367":
			case "csascii":
			case "ibm367":
			case "iso_646.irv:1983":
			case "iso_646.irv:1991":
			case "iso646-us":
			case "iso-ir-6":
			case "us":
			case "us-ascii":
				return "ascii";
				
				// IBM037
			case "037":
			case "cp037":
			case "cpibm37":
			case "cs-ebcdic-cp-ca":
			case "cs-ebcdic-cp-nl":
			case "cs-ebcdic-cp-us":
			case "cs-ebcdic-cp-wt":
			case "csibm037":
			case "ebcdic-cp-ca":
			case "ebcdic-cp-nl":
			case "ebcdic-cp-us":
			case "ebcdic-cp-wt":
			case "ibm037":
			case "ibm-037":
			case "ibm-37":
				return "ibm037";
			
				// ISO-8859-1
			case "819":
			case "8859_1":
			case "cp819":
			case "csisolatin1":
			case "ibm819":
			case "ibm-819":
			case "iso_8859_1":
			case "iso_8859-1":
			case "iso-8859-1":
			case "iso8859_1":
			case "iso8859-1":
			case "iso_8859-1:1987":
			case "iso-ir-100":
			case "l1":
			case "latin1":
				return "iso-8859-1";
			
				// UTF-8
			case "unicode-1-1-utf-8":
			case "utf8":
			case "utf-8":
				return "utf-8";
			
				// Unknown use original
			default:
				return __n;
		}
	}
}

