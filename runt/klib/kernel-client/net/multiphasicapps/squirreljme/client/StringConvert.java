// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.client;

import java.io.UnsupportedEncodingException;

/**
 * This class contains string conversion methods.
 *
 * @since 2018/01/01
 */
public final class StringConvert
{
	/**
	 * Not used.
	 *
	 * @since 2018/01/01
	 */
	private StringConvert()
	{
	}
	
	/**
	 * Converts byte array to string.
	 *
	 * @param __b The byte array to convert.
	 * @return The converted string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public static final String bytesToString(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return new String(__b, "utf-8");
		}
		
		// {@squirreljme.error AR08 Encoding from UTF-8 should be supported.}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("AR08", e);
		}
	}
	
	/**
	 * Converts byte array to string.
	 *
	 * @param __b The byte array to convert.
	 * @param __o Offset.
	 * @param __l Length.
	 * @return The converted string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public static final String bytesToString(byte[] __b, int __o, int __l)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return new String(__b, __o, __l, "utf-8");
		}
		
		// {@squirreljme.error AR08 Encoding from UTF-8 should be supported.}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("AR08", e);
		}
	}
	
	/**
	 * Converts a String to a byte array.
	 *
	 * @param __s The string to convert.
	 * @return The resulting string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	public static final byte[] stringToBytes(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return __s.getBytes("utf-8");
		}
		
		// {@squirreljme.error AR07 Encoding to UTF-8 should be supported.}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("AR07", e);
		}
	}
}

