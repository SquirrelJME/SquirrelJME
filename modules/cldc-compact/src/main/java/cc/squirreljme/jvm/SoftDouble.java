// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * Software math operations on 64-bit double.
 *
 * @since 2019/05/24
 */
public class SoftDouble
{
	/** The zero check mask. */
	public static final long ZERO_CHECK_MASK =
		0x7FFFFFFFFFFFFFFFL;
	
	/** The mask for NaN values. */
	public static final long NAN_MASK =
		0b0111111111111000000000000000000000000000000000000000000000000000L;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/24
	 */
	private SoftDouble()
	{
	}
	
	/**
	 * Adds two values.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @param __bl B low.
	 * @param __bh B high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static double add(int __al, int __ah, int __bl, int __bh)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @param __bl B low.
	 * @param __bh B high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static int cmpl(int __al, int __ah, int __bl, int __bh)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @param __bl B low.
	 * @param __bh B high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static int cmpg(int __al, int __ah, int __bl, int __bh)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @param __bl B low.
	 * @param __bh B high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static double div(int __al, int __ah, int __bl, int __bh)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Is this Not a Number?
	 * 
	 * @param __a The value to check.
	 * @return If this is not a number.
	 * @since 2022/01/06
	 */
	public static boolean isNaN(long __a)
	{
		return SoftDouble.NAN_MASK == (__a & SoftDouble.NAN_MASK);
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @param __bl B low.
	 * @param __bh B high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static double mul(int __al, int __ah, int __bl, int __bh)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static double neg(int __al, int __ah)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * ORs value, used for constants.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @param __bl B low.
	 * @param __bh B high.
	 * @return The result.
	 * @since 2019/05/27
	 */
	public static double or(int __al, int __ah, int __bl, int __bh)
	{
		return Assembly.doublePack(__al | __bl, __ah | __bh);
	}
	
	/**
	 * Remainders a value.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @param __bl B low.
	 * @param __bh B high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static double rem(int __al, int __ah, int __bl, int __bh)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Subtracts values.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @param __bl B low.
	 * @param __bh B high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static double sub(int __al, int __ah, int __bl, int __bh)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to float.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float toFloat(int __al, int __ah)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to integer.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static int toInteger(int __al, int __ah)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to long.
	 *
	 * @param __al A low.
	 * @param __ah A high.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long toLong(int __al, int __ah)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

