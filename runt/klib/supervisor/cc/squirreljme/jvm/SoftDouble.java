// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
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
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
	 */
	public static void add(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
	 */
	public static void cmpl(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
	 */
	public static void cmpg(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
	 */
	public static void div(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
	 */
	public static void mul(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @since 2019/05/24
	 */
	public static void neg(int __ah, int __al)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * ORs value, used for constants.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/27
	 */
	public static void or(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.returnFrame(__ah | __bh, __al | __bl);
	}
	
	/**
	 * Remainders a value.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
	 */
	public static void rem(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Subtracts values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
	 */
	public static void sub(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to float.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @since 2019/05/24
	 */
	public static void toFloat(int __ah, int __al)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to integer.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @since 2019/05/24
	 */
	public static void toInteger(int __ah, int __al)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to long.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @since 2019/05/24
	 */
	public static void toLong(int __ah, int __al)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

