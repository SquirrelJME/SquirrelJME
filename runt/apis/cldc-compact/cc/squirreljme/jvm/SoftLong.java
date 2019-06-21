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
 * Software math operations on 64-bit integer types.
 *
 * @since 2019/05/24
 */
public final class SoftLong
{
	/**
	 * Not used.
	 *
	 * @since 2019/05/24
	 */
	private SoftLong()
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
		// Add the higher/lower parts
		int ch = __ah + __bh,
			cl = __al + __bl;
		
		// If the low addition carried a bit over, then set that bit in the
		// high part
		if ((cl + 0x80000000) < (__al + 0x80000000))
			ch++;
		
		// Return result
		Assembly.returnFrame(ch, cl);
	}
	
	/**
	 * Ands two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
	 */
	public static void and(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.returnFrame(__ah & __bh, __al & __bl);
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
	public static void cmp(int __ah, int __al, int __bh, int __bl)
	{
		// Compare high values firsts
		if (__ah < __bh)
			Assembly.returnFrame(-1);
		else if (__ah > __bh)
			Assembly.returnFrame(1);
		
		// Compare low values unsigned comparison
		__al += Integer.MAX_VALUE;
		__bl += Integer.MAX_VALUE;
		if (__al < __bl)
			Assembly.returnFrame(-1);
		else if (__al > __bl)
			Assembly.returnFrame(1);
		else
			Assembly.returnFrame(0);
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
	 * Ors a value.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
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
	 * Shifts value left by bits.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __s Shift amount.
	 * @since 2019/05/24
	 */
	public static void shl(int __ah, int __al, int __s)
	{
		// Mask the shift amount
		__s &= 0x3F;
		
		// Doing nothing?
		if (__s == 0)
			Assembly.returnFrame(__ah, __al);
		
		// Shifting all the low bits to the high bits
		else if (__s >= 32)
			Assembly.returnFrame(__al << (__s - 32), 0);
		
		// Merge of bits (shift in range of 1-31)
		else
			Assembly.returnFrame((__ah << __s) | (__al >>> (32 - __s)),
				(__al << __s));
	}
	
	/**
	 * Shifts value right by bits.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __s Shift amount.
	 * @since 2019/05/24
	 */
	public static void shr(int __ah, int __al, int __s)
	{
		// Mask the shift amount
		__s &= 0x3F;
		
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
	 * Converts to double.
	 *
	 * @param __a A.
	 * @since 2019/05/24
	 */
	public static void toDouble(int __ah, int __al)
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
		// Just return the low order bits
		Assembly.returnFrame(__al);
	}
	
	/**
	 * Shifts value bits right unsigned.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __s Shift amount.
	 * @since 2019/05/24
	 */
	public static void ushr(int __ah, int __al, int __s)
	{
		// Mask the shift amount
		__s &= 0x3F;
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Xors two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @since 2019/05/24
	 */
	public static void xor(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.returnFrame(__ah ^ __bh, __al ^ __bl);
	}
}

