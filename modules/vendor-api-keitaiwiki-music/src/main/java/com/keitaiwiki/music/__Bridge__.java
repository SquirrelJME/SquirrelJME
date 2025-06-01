// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.util.ExtraMath;
import net.multiphasicapps.io.Base64Alphabet;
import net.multiphasicapps.io.Base64Decoder;

/**
 * Bridge utilities.
 *
 * @since 2025/06/01
 */
@SquirrelJMEVendorApi
final class __Bridge__
{
	
	/**
	 * Decodes base64 data.
	 *
	 * @param __base64 The base64 data to decode.
	 * @return The decoded data.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/01
	 */
	@SquirrelJMEVendorApi
	public static byte[] base64Decode(String __base64)
		throws NullPointerException
	{
		if (__base64 == null)
			throw new NullPointerException("NARG");
		
		return Base64Decoder.decode(__base64, Base64Alphabet.BASIC);
	}
	
	/**
	 * Is the given value finite?
	 *
	 * @param __v The value.
	 * @return Is this value finite?
	 * @since 2025/06/01
	 */
	@SquirrelJMEVendorApi
	public static boolean doubleIsFinite(double __v)
	{
		return !Double.isInfinite(__v);
	}
	
	/**
	 * Is the given value finite? 
	 *
	 * @param __v The value.
	 * @return Is this value finite?
	 * @since 2025/06/01
	 */
	@SquirrelJMEVendorApi
	public static boolean floatIsFinite(float __v)
	{
		return !Float.isInfinite(__v);
	}
	
	/**
	 * Math log function.
	 *
	 * @param __v The input value.
	 * @return The resultant value.
	 * @since 2025/06/01
	 */
	@SquirrelJMEVendorApi
	public static double log(double __v)
	{
		return ExtraMath.log(__v);
	}
	
	/**
	 * Math pow function.
	 *
	 * @param __a The first value.
	 * @param __b The second value.
	 * @return The resultant value.
	 * @since 2025/06/01
	 */
	public static double pow(double __a, double __b)
	{
		return ExtraMath.pow(__a, __b);
	}
}
