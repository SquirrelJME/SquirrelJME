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
	public static long doubleAdd(long __a, long __b)
	{
		throw new Error("TODO");
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
	public static int doubleCompareGreater(long __a, long __b)
	{
		throw new Error("TODO");
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
	public static int doubleCompareLess(long __a, long __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static long doubleDivide(long __a, long __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static long doubleMultiply(long __a, long __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __a The value to negate.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static long doubleNegate(long __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Remainders two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static long doubleRemainder(long __a, long __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Subtracts two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The result.
	 * @since 2016/08/29
	 */
	public static long doubleSubtract(long __a, long __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Converts the value to a float.
	 *
	 * @param __d The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static int doubleToFloat(long __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Converts the value to an integer.
	 *
	 * @param __d The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static int doubleToInt(long __d)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Converts the value to a long.
	 *
	 * @param __d The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static long doubleToLong(long __d)
	{
		throw new Error("TODO");
	}
}

