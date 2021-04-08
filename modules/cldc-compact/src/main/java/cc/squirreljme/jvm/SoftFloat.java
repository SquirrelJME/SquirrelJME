// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.runtime.cldc.util.UnsignedInteger;

/**
 * Software math operations on 32-bit floats.
 * 
 * This source file uses parts of the Berkeley SoftFloat Release 3e library,
 * converted into Java. See the 3rd party licenses documentation.
 *
 * @since 2019/05/24
 */
@SuppressWarnings("CommentedOutCode")
public final class SoftFloat
{
	/** The sign mask. */
	public static final int SIGN_MASK =
		0b1000_0000_0000_0000__0000_0000_0000_0000;
	
	/** Exponent Mask. */
	public static final int EXPONENT_MASK =
		0b0111_1111_1000_0000__0000_0000_0000_0000;
	
	/** Fraction Mask. */
	public static final int FRACTION_MASK =
		0b0000_0000_0111_1111__1111_1111_1111_1111;
	
	/** The mask for NaN values. */
	public static final int NAN_MASK =
		0b0111_1111_1000_0000__0000_0000_0000_0000;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/24
	 */
	private SoftFloat()
	{
	}
	
	/**
	 * Adds two values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float add(int __a, int __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two values, NaN returns {@code -1}.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SuppressWarnings("SpellCheckingInspection")
	public static int cmpl(int __a, int __b)
	{
		if (SoftFloat.isNaN(__a) || SoftFloat.isNaN(__b))
			return -1;
		
		return SoftFloat.__cmp(__a, __b);
	}
	
	/**
	 * Compares two values, NaN returns {@code 1}.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	@SuppressWarnings("SpellCheckingInspection")
	public static int cmpg(int __a, int __b)
	{
		if (SoftFloat.isNaN(__a) || SoftFloat.isNaN(__b))
			return 1;
		
		return SoftFloat.__cmp(__a, __b);
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float div(int __a, int __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Is this Not a Number?
	 * 
	 * @param __a The value to check.
	 * @return If this is not a number.
	 * @since 2021/04/07
	 */
	public static boolean isNaN(int __a)
	{
		return SoftFloat.NAN_MASK == (__a & SoftFloat.NAN_MASK);
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float mul(int __a, int __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float neg(int __a)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Ors a value, used for constant loading.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float or(int __a, int __b)
	{
		return Assembly.intBitsToFloat(__a | __b);
	}
	
	/**
	 * Remainders a value.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float rem(int __a, int __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Subtracts values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float sub(int __a, int __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to double.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static double toDouble(int __a)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to integer.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static int toInteger(int __a)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to long.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long toLong(int __a)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result.
	 * @since 2021/04/07
	 */
	private static int __cmp(int __a, int __b)
	{
		// Equality, note second means -0 == 0
		// return (uiA == uiB) || ! (uint32_t) ((uiA | uiB)<<1);
		if (__a == __b || ((__a | __b) << 1) == 0)
			return 0;
		
		// Less than
		// (signA != signB) ? signA && ((uint32_t) ((uiA | uiB)<<1) != 0)
		// : (uiA != uiB) && (signA ^ (uiA < uiB));
		boolean signA = (0 != (__a & SoftFloat.SIGN_MASK));
		boolean signB = (0 != (__b & SoftFloat.SIGN_MASK));
		if (signA != signB)
		{
			// signA && ((uint32_t) ((uiA | uiB)<<1) != 0)
			if (signA && ((__a | __b) << 1) != 0)
				return -1;
		}
		
		// (uiA != uiB) && (signA ^ (uiA < uiB))
		// ^^^ const ^^
		else if (signA ^ (UnsignedInteger.compareUnsigned(__a, __b) < 0))
			return -1;
		
		// Anything else assume greater than
		return 1;
	}
}

