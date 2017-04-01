// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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
	public static float floatAdd(float __a, float __b)
	{
		throw new todo.TODO();
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
	public static int floatCompareGreater(float __a, float __b)
	{
		// If either side is NaN, stop
		if (floatIsNaN(__a) || floatIsNaN(__b))
			return 1;
		
		throw new todo.TODO();
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
	public static int floatCompareLesser(float __a, float __b)
	{
		// If either side is NaN, stop
		if (floatIsNaN(__a) || floatIsNaN(__b))
			return -1;
		
		throw new todo.TODO();
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static float floatDivide(float __a, float __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Checks if the specified floating point value is NaN.
	 *
	 * @param __v The value to check.
	 * @return {@code true} if the value is NaN.
	 * @since 2017/04/01
	 */
	public static final boolean floatIsNaN(float __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static float floatMultiply(float __a, float __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __a The value to negate.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static float floatNegate(float __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Remainders two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static float floatRemainder(float __a, float __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Subtracts two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static float floatSubtract(float __a, float __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Converts the value to a double.
	 *
	 * @param __f The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static double floatToDouble(float __f)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Converts the value to an integer.
	 *
	 * @param __f The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static int floatToInteger(float __f)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Converts the value to a long.
	 *
	 * @param __f The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static long floatToLong(float __f)
	{
		throw new todo.TODO();
	}
}

