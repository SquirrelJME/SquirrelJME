// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.jvm.mle.MathShelf;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Software integer operations.
 *
 * @since 2019/05/27
 */
@SquirrelJMEVendorApi
@SuppressWarnings("MagicNumber")
public final class SoftInteger
{
	/**
	 * Not used.
	 *
	 * @since 2019/05/27
	 */
	private SoftInteger()
	{
	}
	
	/**
	 * Converts to double.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static double toDouble(int __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Converts to float.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static float toFloat(int __a)
	{
		boolean sign = (__a < 0);
		
		// if ( ! (a & 0x7FFFFFFF) ) {
		if ((__a & 0x7FFF_FFFF) == 0)
		{
			// uZ.ui = sign ? packToF32UI( 1, 0x9E, 0 ) : 0;
			return Float.intBitsToFloat((sign ?
				SoftFloat.__packToF32UI(true, 0x9E, 0) :
				0));
		}
		
		int absA = sign ? -__a : __a;
		
		// return softfloat_normRoundPackToF32( sign, 0x9C, absA );
		return Float.intBitsToFloat(
			SoftFloat.__normRoundPackToF32(sign, 0x9C, absA));
	}
	
	/**
	 * Converts to long.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SquirrelJMEVendorApi
	public static long toLong(int __a)
	{
		// If the integer has the sign bit, then it will be sign extended
		// meaning all the upper bits get set
		if ((__a & 0x80000000) != 0)
			return MathShelf.longPack(__a, 0xFFFFFFFF);
		
		// Otherwise, the top is just zero
		else
			return MathShelf.longPack(__a, 0);
	}
}

