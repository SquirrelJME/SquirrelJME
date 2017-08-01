// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This class contains software 64-bit floating point support for Java
 * operations.
 *
 * All methods here map to Java byte code instructions.
 *
 * @since 2016/08/29
 */
public final class SoftDouble
{
	/**
	 * Not used.
	 *
	 * @since 2016/08/29
	 */
	private SoftDouble()
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
	public static double doubleAdd(double __a, double __b)
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
	public static int doubleCompareGreater(double __a, double __b)
	{
		// If either side is NaN, stop
		if (doubleIsNaN(__a) || doubleIsNaN(__b))
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
	public static int doubleCompareLesser(double __a, double __b)
	{
		// If either side is NaN, stop
		if (doubleIsNaN(__a) || doubleIsNaN(__b))
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
	public static double doubleDivide(double __a, double __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns {@code true} if the represented double value represents NaN.
	 *
	 * @param __a The value to check.
	 * @return {@code true} if the value is NaN.
	 * @since 2016/08/29
	 */
	public static boolean doubleIsNaN(double __a)
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
	public static double doubleMultiply(double __a, double __b)
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
	public static double doubleNegate(double __a)
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
	public static double doubleRemainder(double __a, double __b)
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
	public static double doubleSubtract(double __a, double __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Converts the value to a float.
	 *
	 * @param __d The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static float doubleToFloat(double __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Converts the value to an integer.
	 *
	 * @param __d The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static int doubleToInteger(double __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Converts the value to a long.
	 *
	 * @param __d The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static long doubleToLong(double __d)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns a raw bit representation of the given value.
	 *
	 * @param __v The input value.
	 * @return The raw bits.
	 * @since 2017/04/01
	 */
	public static long doubleToRawLong(double __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Translates the raw bits to the specified type of value.
	 *
	 * @param __v The input raw bits.
	 * @return The value of those bits.
	 * @since 2017/04/01
	 */
	public static double rawLongToDouble(long __v)
	{
		throw new todo.TODO();
	}
}

