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
		
		throw new todo.TODO();
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
}

