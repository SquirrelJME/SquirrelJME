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

import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This contains the code used to obtain the default encoding as well as
 * obtaining the encoder or decoder as needed.
 *
 * @since 2018/09/16
 */
public final class CodecFactory
{
	/**
	 * Not used.
	 *
	 * @since 2018/09/16
	 */
	private CodecFactory()
	{
	}
	
	/**
	 * Returns the default system encoder.
	 *
	 * @return The default encoder.
	 * @since 2018/09/16
	 */
	public static final Encoder defaultEncoder()
	{
		return CodecFactory.encoderUnchecked(CodecFactory.defaultEncoding());
	}
	
	/**
	 * Returns the default encoding.
	 *
	 * @return The default encoding.
	 * @since 2018/09/16
	 */
	public static final String defaultEncoding()
	{
		try
		{
			return System.getProperty("microedition.encoding");
		}
		catch (SecurityException e)
		{
			return "utf-8";
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
		
		// Lowercasing makes it easier to match
		switch ((__enc = CodecFactory.__normalizeEncodingName(__enc)))
		{
				// ASCII
			case "ascii":
				throw new todo.TODO();

				// ISO-8859-1
			case "iso-8859-1":
				throw new todo.TODO();
				
				// UTF-8
			case "utf-8":
				throw new todo.TODO();
			
				// {@squirreljme.error ZZ16 Unknown encoding. (The input
				// encoding)}
			default:
				throw new UnsupportedEncodingException(
					String.format("ZZ16 %s", __enc));
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
		
		// {@squirreljme.error ZZ11 Unknown or unsupported encoding.
		// (The encoding)}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(String.format("ZZ11 %s", __enc), e);
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
	private static final String __normalizeEncodingName(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Store original since it will be lowercase
		String orign = __n;
		
		// Lowercase and map values
		switch ((__n = __n.toLowerCase()))
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
				return orign;
	}
}

