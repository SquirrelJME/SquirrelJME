// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This class contains software 32-bit floating point support for Java
 * operations.
 *
 * All methods here map to Java byte code instructions.
 *
 * @since 2016/08/29
 */
public final class SoftFloat
{
	/** The shift to get to the exponent. */
	private static final int _EXPONENT_SHIFT =
		23;
	
	/** Bits in the exponent. */
	private static final int _EXPONENT_BITS =
		8;
	
	/** The exponent value mask. */
	private static final int _EXPONENT_VALUE_MASK =
		(1 << _EXPONENT_BITS) - 1;
	
	/** The fraction value mask. */
	private static final int _FRACTION_VALUE_MASK =
		(1 << _EXPONENT_SHIFT) - 1;
	
	/** The sign bit mask. */
	private static final int _SIGN_SHIFTED_MASK =
		(1 << (_EXPONENT_BITS + _EXPONENT_SHIFT));
	
	/**
	 * Not used.
	 *
	 * @since 2916/08/29
	 */
	private SoftFloat()
	{
	}
	
	/**
	 * Adds two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static int floatAdd(int __a, int __b)
	{
		return SoftDouble.doubleToFloat(
			SoftDouble.doubleAdd(floatToDouble(__a), floatToDouble(__b)));
	}
	
	/**
	 * Compares two values, if either side is NaN then {@code 1} is returned.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return Integer based comparison, if either side is NaN then {@code 1}
	 * is returned.
	 * @since 2016/08/29
	 */
	public static int floatCompareGreater(int __a, int __b)
	{
		return SoftDouble.doubleCompareGreater(
			floatToDouble(__a), floatToDouble(__b));
	}
	
	/**
	 * Compares two values, if either side is NaN then {@code -1} is returned.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return Integer based comparison, if either side is NaN then {@code -1}
	 * is returned.
	 * @since 2016/08/29
	 */
	public static int floatCompareLesser(int __a, int __b)
	{
		return SoftDouble.doubleCompareLesser(
			floatToDouble(__a), floatToDouble(__b));
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static int floatDivide(int __a, int __b)
	{
		return SoftDouble.doubleToFloat(
			SoftDouble.doubleDivide(floatToDouble(__a), floatToDouble(__b)));
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static int floatMultiply(int __a, int __b)
	{
		return SoftDouble.doubleToFloat(
			SoftDouble.doubleMultiply(floatToDouble(__a), floatToDouble(__b)));
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __a The value to negate.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static int floatNegate(int __a)
	{
		return SoftDouble.doubleToFloat(
			SoftDouble.doubleNegate(floatToDouble(__a)));
	}
	
	/**
	 * Remainders two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static int floatRemainder(int __a, int __b)
	{
		return SoftDouble.doubleToFloat(SoftDouble.doubleRemainder(
			floatToDouble(__a), floatToDouble(__b)));
	}
	
	/**
	 * Subtracts two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static int floatSubtract(int __a, int __b)
	{
		return SoftDouble.doubleToFloat(
			SoftDouble.doubleSubtract(floatToDouble(__a), floatToDouble(__b)));
	}
	
	/**
	 * Converts the value to a double.
	 *
	 * @param __f The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static long floatToDouble(int __f)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Converts the value to an integer.
	 *
	 * @param __f The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static int floatToInteger(int __f)
	{
		return SoftDouble.doubleToInteger(floatToDouble(__f));
	}
	
	/**
	 * Converts the value to a long.
	 *
	 * @param __f The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static long floatToLong(int __f)
	{
		return SoftDouble.doubleToLong(floatToDouble(__f));
	}
	
	/**
	 * Converts a hardware single precision floating point number to a software
	 * double precision floating point number.
	 *
	 * @param __f The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static long hardFloatToSoftDouble(float __f)
	{
		return floatToDouble(Float.floatToRawIntBits(__f));
	}
}

