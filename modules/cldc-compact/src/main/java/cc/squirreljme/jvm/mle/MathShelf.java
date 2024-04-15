// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This contains various math functions.
 *
 * @since 2020/06/18
 */
@SquirrelJMEVendorApi
public final class MathShelf
{
	/**
	 * Not used.
	 *
	 * @since 2020/06/18
	 */
	private MathShelf()
	{
	}
	
	/**
	 * Packs the given two integers to a double value.
	 *
	 * @param __lo The low value.
	 * @param __hi The high value.
	 * @return The double value.
	 * @since 2019/06/21
	 */
	@SquirrelJMEVendorApi
	public static native double doublePack(int __lo, int __hi);
	
	/**
	 * Unpacks the high value of a double.
	 *
	 * @param __d The double to unpack.
	 * @return The unpacked high value.
	 * @since 2020/02/24
	 */
	@SquirrelJMEVendorApi
	public static native int doubleUnpackHigh(double __d);
	
	/**
	 * Unpacks the low value of a double.
	 *
	 * @param __d The double to unpack.
	 * @return The unpacked low value.
	 * @since 2020/02/24
	 */
	@SquirrelJMEVendorApi
	public static native int doubleUnpackLow(double __d);
	
	/**
	 * Packs the given two integers to a long value.
	 *
	 * @param __lo The low value.
	 * @param __hi The high value.
	 * @return The long value.
	 * @since 2019/06/21
	 */
	@SquirrelJMEVendorApi
	public static native long longPack(int __lo, int __hi);
	
	/**
	 * Unpack high value from long.
	 *
	 * @param __v The long value.
	 * @return The unpacked fragment.
	 * @since 2019/06/21
	 */
	@SquirrelJMEVendorApi
	public static native int longUnpackHigh(long __v);
	
	/**
	 * Unpack low value from long.
	 *
	 * @param __v The long value.
	 * @return The unpacked fragment.
	 * @since 2019/06/21
	 */
	@SquirrelJMEVendorApi
	public static native int longUnpackLow(long __v);
	
	/**
	 * Extracts the raw bits of the given value.
	 *
	 * @param __v The value to extract.
	 * @return The raw bits.
	 * @since 2020/06/18
	 */
	@SquirrelJMEVendorApi
	public static native long rawDoubleToLong(double __v);
	
	/**
	 * Extracts the raw bits of the given value.
	 *
	 * @param __v The value to extract.
	 * @return The raw bits.
	 * @since 2020/06/18
	 */
	@SquirrelJMEVendorApi
	public static native int rawFloatToInt(float __v);
	
	/**
	 * Composes the value from the given bits
	 *
	 * @param __b The raw bits.
	 * @return The value.
	 * @since 2020/06/18
	 */
	@SquirrelJMEVendorApi
	public static native float rawIntToFloat(int __b);
	
	/**
	 * Composes the value from the given bits
	 *
	 * @param __b The raw bits.
	 * @return The value.
	 * @since 2020/06/18
	 */
	@SquirrelJMEVendorApi
	public static native double rawLongToDouble(long __b);
}
