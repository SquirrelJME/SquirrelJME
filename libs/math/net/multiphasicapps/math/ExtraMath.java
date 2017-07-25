// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.math;

/**
 * This class contains the extra math library and contains functions which
 * should be in {@link Math} but are not there.
 *
 * @since 2017/07/24
 */
public class ExtraMath
{
	/**
	 * Returns the natural log of the given value.
	 *
	 * @param __v The input value.
	 * @return The natural log of the given value.
	 * @since 2017/07/25
	 */
	public static float log(float __v)
	{
		return log2(__v) * 0x1.62E42FEFA39EFP-1F;
	}
	
	/**
	 * Returns the natural log of the given value.
	 *
	 * @param __v The input value.
	 * @return The natural log of the given value.
	 * @since 2017/07/25
	 */
	public static double log(double __v)
	{
		return log2(__v) * 0x1.62E42FEFA39EFP-1D;
	}
	
	/**
	 * Returns the decimal log of the given value.
	 *
	 * @param __v The input value.
	 * @return The decimal log of the given value.
	 * @since 2017/07/25
	 */
	public static float log10(float __v)
	{
		return log(__v) / 0x1.26BB1BBB55516P1F;
	}
	
	/**
	 * Returns the decimal log of the given value.
	 *
	 * @param __v The input value.
	 * @return The decimal log of the given value.
	 * @since 2017/07/25
	 */
	public static double log10(double __v)
	{
		return log(__v) / 0x1.26BB1BBB55516P1D;
	}
	
	/**
	 * Returns the binary log of the given value.
	 *
	 * @param __v The input value.
	 * @return The binary log of the given value.
	 * @since 2017/07/25
	 */
	public static float log2(float __v)
	{
		int x = Float.floatToRawIntBits(__v);
		float log2 = (float)(((x >> 23) & 255) - 128);
		x &= ~2139095040;
		x += 1065353216;
		float back = Float.intBitsToFloat(x);
		return log2 + (((-0.34484843F) * back + 2.02466578F) * back -
			0.67487759F);
	}
	
	/**
	 * Returns the binary log of the given value.
	 *
	 * @param __v The input value.
	 * @return The binary log of the given value.
	 * @since 2017/07/25
	 */
	public static double log2(double __v)
	{
		return (double)log2((float)__v);
	}
}

