// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

/**
 * This contains various math functions.
 *
 * @since 2020/06/18
 */
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
	 * Extracts the raw bits of the given value.
	 *
	 * @param __v The value to extract.
	 * @return The raw bits.
	 * @since 2020/06/18
	 */
	public static native long rawDoubleToLong(double __v);
	
	/**
	 * Extracts the raw bits of the given value.
	 *
	 * @param __v The value to extract.
	 * @return The raw bits.
	 * @since 2020/06/18
	 */
	public static native int rawFloatToInt(float __v);
	
	/**
	 * Composes the value from the given bits
	 *
	 * @param __b The raw bits.
	 * @return The value.
	 * @since 2020/06/18
	 */
	public static native float rawIntToFloat(int __b);
	
	/**
	 * Composes the value from the given bits
	 *
	 * @param __b The raw bits.
	 * @return The value.
	 * @since 2020/06/18
	 */
	public static native double rawLongToDouble(long __b);
}
