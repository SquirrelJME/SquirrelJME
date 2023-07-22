// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.BuiltInEncodingType;
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
	 * Returns the decoder for the given encoding.
	 *
	 * @param __builtIn The {@link BuiltInEncodingType}.
	 * @return The decoder for the given encoding.
	 * @throws UnsupportedEncodingException If the encoding is invalid.
	 * @since 2020/06/11
	 */
	public static Decoder decoder(int __builtIn)
		throws UnsupportedEncodingException
	{
		switch (__builtIn)
		{
			case BuiltInEncodingType.ASCII:
				return new ASCIIDecoder();
			
			case BuiltInEncodingType.IBM037:
				return new IBM037Decoder();
			
			case BuiltInEncodingType.ISO_8859_1:
				return new ISO88591Decoder();
			
			case BuiltInEncodingType.ISO_8859_15:
				return new ISO885915Decoder();
			
			case BuiltInEncodingType.SHIFT_JIS:
				return new ShiftJisDecoder();
			
			case BuiltInEncodingType.UTF8:
				return new UTF8Decoder();
				
				/* {@squirreljme.error ZZ47 Unsupported decoder.
				(The built-in encoding ID)} */
			case BuiltInEncodingType.UNSPECIFIED:
			default:
				throw new UnsupportedEncodingException("ZZ47 " + __builtIn);
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
	public static Decoder decoder(String __enc)
		throws NullPointerException, UnsupportedEncodingException
	{
		return CodecFactory.decoder(CodecFactory.toBuiltIn(__enc));
	}
	
	/**
	 * Returns the default decoder.
	 *
	 * @return The default decoder.
	 * @since 2018/10/13
	 */
	public static Decoder defaultDecoder()
	{
		try
		{
			return CodecFactory.decoder(RuntimeShelf.encoding());
		}
		catch (UnsupportedEncodingException e)
		{
			/* {@squirreljme.error ZZ02 Built-in encoding is not configured
			properly.} */
			throw new Error("ZZ02", e);
		}
	}
	
	/**
	 * Returns the default system encoder.
	 *
	 * @return The default encoder.
	 * @since 2018/09/16
	 */
	public static Encoder defaultEncoder()
	{
		try
		{
			return CodecFactory.encoder(RuntimeShelf.encoding());
		}
		catch (UnsupportedEncodingException e)
		{
			/* {@squirreljme.error ZZ04 Built-in encoding is not configured
			properly.} */
			throw new Error("ZZ04", e);
		}
	}
	
	/**
	 * Returns the encoder for the given built-in encoding.
	 *
	 * @param __builtIn The {@link BuiltInEncodingType}.
	 * @return The encoder.
	 * @throws UnsupportedEncodingException If the encoder is not valid.
	 * @since 2020/06/11
	 */
	public static Encoder encoder(int __builtIn)
		throws UnsupportedEncodingException
	{
		switch (__builtIn)
		{
			case BuiltInEncodingType.ASCII:
				return new ASCIIEncoder();
			
			case BuiltInEncodingType.IBM037:
				return new IBM037Encoder();
			
			case BuiltInEncodingType.ISO_8859_1:
				return new ISO88591Encoder();
			
			case BuiltInEncodingType.ISO_8859_15:
				return new ISO885915Encoder();
			
			case BuiltInEncodingType.SHIFT_JIS:
				return new ShiftJisEncoder();
			
			case BuiltInEncodingType.UTF8:
				return new UTF8Encoder();
				
				/* {@squirreljme.error ZZ48 Unsupported encoder.
				(The built-in encoding ID)} */
			case BuiltInEncodingType.UNSPECIFIED:
			default:
				throw new UnsupportedEncodingException("ZZ48 " + __builtIn);
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
	public static Encoder encoder(String __enc)
		throws NullPointerException, UnsupportedEncodingException
	{
		return CodecFactory.encoder(CodecFactory.toBuiltIn(__enc));
	}
	
	/**
	 * Returns the built-in encoding ID.
	 *
	 * @param __enc The name of the encoding.
	 * @return The built-in encoding.
	 * @since 2020/06/11
	 */
	public static int toBuiltIn(String __enc)
		throws UnsupportedEncodingException
	{
		if (__enc == null)
			throw new NullPointerException("NARG");
		
		// Normalization makes it easier to match
		switch ((__enc = CodecFactory.__normalizeEncodingName(__enc)))
		{
			case "ascii":		return BuiltInEncodingType.ASCII;
			case "ibm037":		return BuiltInEncodingType.IBM037;
			case "iso-8859-1":	return BuiltInEncodingType.ISO_8859_1;
			case "iso-8859-15":	return BuiltInEncodingType.ISO_8859_15;
			case "shift-jis":	return BuiltInEncodingType.SHIFT_JIS;
			case "utf-8":		return BuiltInEncodingType.UTF8;
			
				/* {@squirreljme.error ZZ01 Unknown encoding. (The input
				encoding)} */
			default:
				throw new UnsupportedEncodingException(
					String.format("ZZ01 %s", __enc));
		}
	}
	
	/**
	 * Returns the string representation of the given built-in encoding.
	 *
	 * @param __builtIn The {@link BuiltInEncodingType}.
	 * @return The string for this built-in encoding.
	 * @throws IllegalArgumentException If the encoding is not valid.
	 * @since 2020/06/11
	 */
	public static String toString(int __builtIn)
		throws IllegalArgumentException
	{
		switch (__builtIn)
		{
			case BuiltInEncodingType.ASCII:			return "ascii";
			case BuiltInEncodingType.IBM037:		return "ibm037";
			case BuiltInEncodingType.ISO_8859_1:	return "iso-8859-1";
			case BuiltInEncodingType.ISO_8859_15:	return "iso-8859-15";
			case BuiltInEncodingType.SHIFT_JIS:		return "shift-jis";
			case BuiltInEncodingType.UTF8:			return "utf-8";
			
				/* {@squirreljme.error ZZ49 Invalid built-in encoding.
				(The built-in ID}} */
			default:
				throw new IllegalArgumentException("ZZ49 " + __builtIn);
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
				
				// Shift-JIS
			case "shift_jis":
			case "shift_jisx0213":
			case "shift-jis":
			case "shift-jisx0213":
				return "shift-jis";
			
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

