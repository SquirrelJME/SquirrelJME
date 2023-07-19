// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.MathShelf;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.constants.MemoryProfileType;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

@Api
public final class Long
	extends Number
	implements Comparable<Long>
{
	/** The maximum value. */
	@Api
	public static final long MAX_VALUE =
		9223372036854775807L;
	
	/** The minimum value. */
	@Api
	public static final long MIN_VALUE =
		-9223372036854775808L;
	
	/** The size of the type in bits. */
	@Api
	public static final int SIZE =
		64;
	
	/** The class representing the primitive long type. */
	@Api
	public static final Class<Long> TYPE =
		TypeShelf.<Long>typeToClass(TypeShelf.typeOfLong());
	
	/** The value of this long. */
	private final long _value;
	
	/** The string representation of this value. */
	private Reference<String> _string;
	
	/**
	 * Initializes the long with the given value.
	 *
	 * @param __v The value to use.
	 * @since 2018/09/23
	 */
	@Api
	public Long(long __v)
	{
		this._value = __v;
	}
	
	@Api
	public Long(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public byte byteValue()
	{
		return (byte)this._value;
	}
	
	@Override
	public int compareTo(Long __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public double doubleValue()
	{
		return (double)this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof Long))
			return false;
		
		return this._value == ((Long)__o)._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public float floatValue()
	{
		return (float)this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public int hashCode()
	{
		long value = this._value;
		return (int)(value ^ (value >>> 32));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public int intValue()
	{
		return (int)this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/03
	 */
	@Override
	public long longValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/01/06
	 */
	@Override
	public short shortValue()
	{
		return (short)this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/23
	 */
	@Override
	public String toString()
	{
		// Normal memory profile?
		if (RuntimeShelf.memoryProfile() >= MemoryProfileType.NORMAL)
			return Long.toString(this._value, 10);
		
		// Try to reduce memory usage
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = Long.toString(this._value, 10)));
		
		return rv;
	}
	
	@Api
	public static int bitCount(long __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Long decode(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw Debugging.todo();
	}
	
	@Api
	public static Long getLong(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Long getLong(String __a, long __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Long getLong(String __a, Long __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static long highestOneBit(long __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static long lowestOneBit(long __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int numberOfLeadingZeros(long __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int numberOfTrailingZeros(long __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the value of the specified string using the given radix.
	 *
	 * @param __v The String to decode.
	 * @param __radix The radix to use.
	 * @return The resulting long.
	 * @throws NumberFormatException If the string is not valid or the radix
	 * is outside of the valid bounds.
	 * @since 2020/06/18
	 */
	@Api
	public static long parseLong(String __v, int __radix)
		throws NumberFormatException
	{
		/* {@squirreljme.error ZZ25 The radix is out of bounds. (The radix)} */
		if (__radix < Character.MIN_RADIX || __radix > Character.MAX_RADIX)
			throw new NumberFormatException("ZZ25 " + __radix);
			
		if (__v == null)
			throw new NumberFormatException("ZZ26");
		
		/* {@squirreljme.error ZZ26 String is null or has zero length.} */
		int n = __v.length();
		if (n <= 0)
			throw new NumberFormatException("ZZ26");
		
		// Detect sign
		boolean neg;
		boolean signed = false;
		char c = __v.charAt(0);
		if ((neg = (c == '-')) || c == '+')
			signed = true;
		
		// Read all digits
		long rv = 0;
		for (int i = (signed ? 1 : 0); i < n; i++)
		{
			// Read character
			c = __v.charAt(i);
			
			// Convert to digit
			long dig = Character.digit(c, __radix);
			
			/* {@squirreljme.error ZZ27 Character out of range of radix.
			(The input string; The out of range character)} */
			if (dig < 0)
				throw new NumberFormatException("ZZ27 " + __v + " " + c);
			
			/* {@squirreljme.error ZZ28 Input integer out of range of 64-bit
			long. (The input string)} */
			long prod = rv * __radix;
			if (rv != 0 && (neg ? (prod > rv) : (prod < rv)))
				throw new NumberFormatException("ZZ28 " + __v);
			
			// Add up
			if (neg)
				rv = prod - dig;
			else
				rv = prod + dig;
		}
		
		return rv;
	}
	
	/**
	 * Returns the value of the specified string.
	 *
	 * @param __v The String to decode.
	 * @return The parsed long.
	 * @throws NumberFormatException If the string is not valid.
	 * @since 2020/06/18
	 */
	@Api
	public static long parseLong(String __v)
		throws NumberFormatException
	{
		return Long.parseLong(__v, 10);
	}
	
	/**
	 * Reverses all of the bits in the given integer.
	 *
	 * @param __l The input value.
	 * @return The integer but with the bits reversed.
	 * @since 2022/01/07
	 */
	@Api
	public static long reverse(long __l)
	{
		int hi = MathShelf.longUnpackHigh(__l);
		int lo = MathShelf.longUnpackLow(__l);
		
		// Hi is placed lo, and lo is placed hi
		return MathShelf.longPack(Integer.reverse(hi), Integer.reverse(lo));
	}
	
	/**
	 * Reverses the given bytes.
	 * 
	 * @param __i The integer to reverse.
	 * @return The reversed bytes.
	 * @since 2021/02/18
	 */
	@Api
	public static long reverseBytes(long __i)
	{
		// 0xAABBCCDD_EEFFGGHH -> 0xBBAADDCC_FFEEHHGG
		__i = (((__i & 0xFF00FF00_FF00FF00L) >>> 8) |
			((__i & 0x00FF00FF_00FF00FFL) << 8));
			
		// 0xAABBCCDD_EEFFGGHH -> 0xDDCCBBAA_HHGGFFEE
		__i = (((__i & 0xFFFF0000_FFF0000L) >>> 8) |
			((__i & 0x0000FFFF_0000FFFFL) << 8));
		
		// 0xDDCCBBAA_HHGGFFEE -> 0xHHGGFFEE_DDCCBBAA
		return (__i >>> 32) | (__i << 32);
	}
	
	@Api
	public static long rotateLeft(long __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static long rotateRight(long __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int signum(long __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static long sum(long __a, long __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns a string of the given value in binary.
	 *
	 * @param __v The value.
	 * @return The representing string.
	 * @since 2019/12/25
	 */
	@Api
	public static String toBinaryString(long __v)
	{
		return Long.__unsignedString(__v, 2);
	}
	
	/**
	 * Returns a string of the given value in hexadecimal.
	 *
	 * @param __v The value.
	 * @return The representing string.
	 * @since 2019/12/25
	 */
	@Api
	public static String toHexString(long __v)
	{
		return Long.__unsignedString(__v, 16);
	}
	
	/**
	 * Returns a string of the given value in octal.
	 *
	 * @param __v The value.
	 * @return The representing string.
	 * @since 2019/12/25
	 */
	@Api
	public static String toOctalString(long __v)
	{
		return Long.__unsignedString(__v, 8);
	}
	
	/**
	 * Converts the value to a string using the given radix.
	 *
	 * @param __v The input value.
	 * @param __r The radix of the string, if it exceeds the maximum
	 * permitted radix specified in {@link Character} then this is set to 10.
	 * @return The resulting string.
	 * @since 2018/09/23
	 */
	@Api
	public static String toString(long __v, int __r)
	{
		// If the radix is not valid, then just force to 10
		if (__r < Character.MIN_RADIX || __r > Character.MAX_RADIX)
			__r = 10;
		
		StringBuilder sb = new StringBuilder();
		
		// Negative? Remember it but we need to swap the sign
		boolean negative = (__v < 0);
		if (negative)
			__v = -__v;
		
		// Insert characters at the end of the string, they will be reversed
		// later, it is easier this way
		for (boolean digit = false;;)
		{
			// Determine the current place
			int mod = (int)(__v % __r);
			
			// Do not print if any other digit was stored
			if (__v == 0 && digit)
				break;
			
			// If our remainder is negative then this is likely the minimum
			// number, so this needs to be corrected and normalized
			else if (mod < 0)
			{
				// We can negate the number once we have removed the remainder
				// from it. For power of 2 radixes (2, 4, 8, 16, and 32) this
				// will be zero, but no other radix will have a value of zero.
				// By the next run our number will be smaller, so that it will
				// auto-normalize itself and print appropriately.
				__v = -(__v - mod);
				mod = -mod;
			}
			
			// Print character
			sb.append(Character.forDigit(mod, __r));
			digit = true;
			
			// Stop printing characters
			if (__v == 0)
				break;
			
			// Use the remaining division
			else
				__v = __v / __r;
		}
		
		// Add the sign in
		if (negative)
			sb.append('-');
			
		// Because the values are added in the opposite order, reverse it
		sb.reverse();
		
		return sb.toString();
	}
	
	/**
	 * Calls {@link Long#toString(long, int)} with a radix of 10.
	 *
	 * @param __v The input value.
	 * @return The resulting string.
	 * @since 2018/09/23
	 */
	@Api
	public static String toString(long __v)
	{
		return Long.toString(__v, 10);
	}
	
	/**
	 * Returns the value of the specified string using the given radix.
	 *
	 * @param __v The String to decode.
	 * @param __r The radix to use.
	 * @return The boxed value.
	 * @throws NumberFormatException If the string is not valid or the radix
	 * is outside of the valid bounds.
	 * @since 2022/01/07
	 */
	@Api
	public static Long valueOf(String __v, int __r)
		throws NumberFormatException
	{
		return Long.parseLong(__v, __r);
	}
	
	/**
	 * Returns the value of the specified string.
	 *
	 * @param __v The String to decode.
	 * @return The boxed value.
	 * @throws NumberFormatException If the string is not valid.
	 * @since 2022/01/07
	 */
	@Api
	public static Long valueOf(String __v)
		throws NumberFormatException
	{
		return Long.parseLong(__v, 10);
	}
	
	/**
	 * Returns a instance of the given value, this may be cached.
	 *
	 * @param __v The value to box.
	 * @return The boxed value.
	 * @since 2018/09/23
	 */
	@Api
	@SuppressWarnings("UnnecessaryBoxing")
	@ImplementationNote("This is not cached.")
	public static Long valueOf(long __v)
	{
		return new Long(__v);
	}
	
	/**
	 * Returns an unsigned string of the given number and base.
	 *
	 * @param __v The value to translate.
	 * @param __b The number base.
	 * @return The resulting string.
	 * @since 2019/12/25
	 */
	private static String __unsignedString(long __v, int __b)
	{
		throw Debugging.todo();
	}
}


