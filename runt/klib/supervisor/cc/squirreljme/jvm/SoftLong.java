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
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long add(int __ah, int __al, int __bh, int __bl)
	{
		// Add the higher/lower parts
		int ch = __ah + __bh,
			cl = __al + __bl;
		
		// If the low addition carried a bit over, then set that bit in the
		// high part
		if ((cl + 0x80000000) < (__al + 0x80000000))
			ch++;
		
		// Return result
		return Assembly.longPack(ch, cl);
	}
	
	/**
	 * Ands two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long and(int __ah, int __al, int __bh, int __bl)
	{
		return Assembly.longPack(__ah & __bh, __al & __bl);
	}
	
	/**
	 * Compares two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static int cmp(int __ah, int __al, int __bh, int __bl)
	{
		// Compare high values firsts
		if (__ah < __bh)
			return -1;
		else if (__ah > __bh)
			return 1;
		
		// Compare low values unsigned comparison
		__al += Integer.MAX_VALUE;
		__bl += Integer.MAX_VALUE;
		if (__al < __bl)
			return -1;
		else if (__al > __bl)
			return 1;
		else
			return 0;
	}
	
	/**
	 * Divides two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long div(int __ah, int __al, int __bh, int __bl)
	{
		return SoftLong.__div(false, Assembly.longPack(__ah, __al),
			Assembly.longPack(__bh, __bl));
	}
	
	/**
	 * Multiplies two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long mul(int __ah, int __al, int __bh, int __bl)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Negates a value.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long neg(int __ah, int __al)
	{
		// Negate and check for overflow
		int nh = (~__ah),
			nl = (~__al + 1);
		if (nl == 0)
			nh++;
		
		// Return result
		return Assembly.longPack(nh, nl);
	}
	
	/**
	 * Ors a value.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long or(int __ah, int __al, int __bh, int __bl)
	{
		return Assembly.longPack(__ah | __bh, __al | __bl);
	}
	
	/**
	 * Remainders a value.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long rem(int __ah, int __al, int __bh, int __bl)
	{
		return SoftLong.__div(true, Assembly.longPack(__ah, __al),
			Assembly.longPack(__bh, __bl));
	}
	
	/**
	 * Shifts value left by bits.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __s Shift amount.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long shl(int __ah, int __al, int __s)
	{
		// Mask the shift amount
		__s &= 0x3F;
		
		// Doing nothing?
		if (__s == 0)
			return Assembly.longPack(__ah, __al);
		
		// Shifting all the low bits to the high bits
		else if (__s >= 32)
			return Assembly.longPack(__al << (__s - 32), 0);
		
		// Merge of bits (shift in range of 1-31)
		else
			return Assembly.longPack((__ah << __s) | (__al >>> (32 - __s)),
				(__al << __s));
	}
	
	/**
	 * Shifts value right by bits.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __s Shift amount.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long shr(int __ah, int __al, int __s)
	{
		// Mask the shift amount
		__s &= 0x3F;
		
		// Doing nothing?
		if (__s == 0)
			return Assembly.longPack(__ah, __al);
		
		// Shifting all the high bits low
		else if (__s >= 32)
			return Assembly.longPack((__ah & 0x80000000) >> 31,
				__ah >> (__s - 32));
		
		// Merge of bits (shift in range of 1-31)
		else
			return Assembly.longPack((__ah >> __s),
				(__ah << (32 - __s)) | (__al >>> __s));
	}
	
	/**
	 * Subtracts values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long sub(int __ah, int __al, int __bh, int __bl)
	{
		// Negate and check for overflow
		int nh = (~__bh),
			nl = (~__bl + 1);
		if (nl == 0)
			nh++;
		
		// Add the higher/lower parts, with a negation
		int ch = __ah + nh,
			cl = __al + nl;
		
		// If the low addition carried a bit over, then set that bit in the
		// high part
		if ((cl + 0x80000000) < (__al + 0x80000000))
			ch++;
		
		// Return result
		return Assembly.longPack(ch, cl);
	}
	
	/**
	 * Converts to double.
	 *
	 * @param __a A.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static double toDouble(int __ah, int __al)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to float.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static float toFloat(int __ah, int __al)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Converts to integer.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static int toInteger(int __ah, int __al)
	{
		// Just return the low order bits
		return __al;
	}
	
	/**
	 * Shifts value bits right unsigned.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __s Shift amount.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long ushr(int __ah, int __al, int __s)
	{
		// Mask the shift amount
		__s &= 0x3F;
		
		// Doing nothing?
		if (__s == 0)
			return Assembly.longPack(__ah, __al);
		
		// Shifting all the high bits low
		else if (__s >= 32)
			return Assembly.longPack(0, __ah >>> (__s - 32));
		
		// Merge of bits (shift in range of 1-31)
		else
			return Assembly.longPack((__ah >>> __s),
				(__ah << (32 - __s)) | (__al >>> __s));
	}
	
	/**
	 * Xors two values.
	 *
	 * @param __ah A high.
	 * @param __al A low.
	 * @param __bh B high.
	 * @param __bl B low.
	 * @return The result.
	 * @since 2019/05/24
	 */
	public static long xor(int __ah, int __al, int __bh, int __bl)
	{
		return Assembly.longPack(__ah ^ __bh, __al ^ __bl);
	}
	
	/**
	 * Divides and remainders two values.
	 *
	 * @param __dorem Return the remainder?
	 * @param __num The numerator.
	 * @param __den The denominator.
	 * @return The result.
	 * @since 2019/05/24
	 */
	private static long __div(boolean __dorem, long __num, long __den)
	{
		/* Wikipedia (http://en.wikipedia.org/wiki/Division_%28digital%29) */
		/* if D == 0 then throw DivisionByZeroException end*/
		/* Q := 0 # initialize quotient and remainder to zero  */
		/* R := 0                                              */
		/* for i = n-1...0 do  # " where n is no of bits "     */
		/*   R := R << 1       # left-shift R by 1 bit         */
		/*   R(0) := N(i)      # set the least-significant bit */
		/*              # of R equal to bit i of the numerator */
		/*   if R >= D then                                    */
		/*     R = R - D                                       */
		/*     Q(i) := 1                                       */
		/*   end                                               */
		/* end                                                 */
		long rvquot = 0, rvrem = 0,
			inquot = 0, inrem = 0,
			i;
		boolean isneg;
		
		// Disallow division by zero
		if (__den == 0)
			return 0;
		
		// Negative?
		isneg = false;
		if ((__num < 0 && __den >= 0) || (__num >= 0 && __den < 0))
			isneg |= true;
		
		// Force Positive
		__num = (__num < 0 ? -__num : __num);
		__den = (__den < 0 ? -__den : __den);
		
		// Perform Math
		for (i = 63;; i--)
		{
			inrem <<= 1L;
			inrem &= 0xFFFFFFFFFFFFFFFEL;
			inrem |= ((__num >>> i) & 1L);
			
			// Unsigned comparison
			if ((inrem + Long.MAX_VALUE) >= (__den + Long.MAX_VALUE))
			{
				inrem -= __den;
				inquot |= (1L << i);
			}
			
			// Stop?
			if (i == 0)
				break;
		}
		
		// Restore Integers
		rvquot = inquot;
		rvrem = inrem;
		
		// Make Negative
		if (isneg)
			rvquot = -rvquot;
		
		// Return
		return (__dorem ? rvquot : rvrem);
	}
}

