// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.asm.ObjectAccess;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public final class Long
	extends Number
	implements Comparable<Long>
{
	public static final long MAX_VALUE =
		9223372036854775807L;
	
	public static final long MIN_VALUE =
		-9223372036854775808L;
	
	public static final int SIZE =
		64;
	
	/** The class representing the primitive long type. */
	public static final Class<Long> TYPE =
		ObjectAccess.<Long>classByNameType("long");
	
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
	public Long(long __v)
	{
		this._value = __v;
	}
	
	public Long(String __a)
		throws NumberFormatException
	{
		super();
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	@Override
	public byte byteValue()
	{
		throw new todo.TODO();
	}
	
	public int compareTo(Long __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public double doubleValue()
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public float floatValue()
	{
		throw new todo.TODO();
	}
	
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int intValue()
	{
		throw new todo.TODO();
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
	
	@Override
	public short shortValue()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/23
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = Long.toString(this._value)));
		
		return rv;
	}
	
	public static int bitCount(long __a)
	{
		throw new todo.TODO();
	}
	
	public static Long decode(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static Long getLong(String __a)
	{
		throw new todo.TODO();
	}
	
	public static Long getLong(String __a, long __b)
	{
		throw new todo.TODO();
	}
	
	public static Long getLong(String __a, Long __b)
	{
		throw new todo.TODO();
	}
	
	public static long highestOneBit(long __a)
	{
		throw new todo.TODO();
	}
	
	public static long lowestOneBit(long __a)
	{
		throw new todo.TODO();
	}
	
	public static int numberOfLeadingZeros(long __a)
	{
		throw new todo.TODO();
	}
	
	public static int numberOfTrailingZeros(long __a)
	{
		throw new todo.TODO();
	}
	
	public static long parseLong(String __a, int __b)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static long parseLong(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static long reverse(long __a)
	{
		throw new todo.TODO();
	}
	
	public static long reverseBytes(long __a)
	{
		throw new todo.TODO();
	}
	
	public static long rotateLeft(long __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public static long rotateRight(long __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public static int signum(long __a)
	{
		throw new todo.TODO();
	}
	
	public static long sum(long __a, long __b)
	{
		throw new todo.TODO();
	}
	
	public static String toBinaryString(long __a)
	{
		throw new todo.TODO();
	}
	
	public static String toHexString(long __a)
	{
		throw new todo.TODO();
	}
	
	public static String toOctalString(long __a)
	{
		throw new todo.TODO();
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
	public static String toString(long __v, int __r)
	{
		// If the radix is not valid, then just force to 10
		if (__r < Character.MIN_RADIX || __r > Character.MAX_RADIX)
			__r = 10;
		
		StringBuilder sb = new StringBuilder();
		
		// Negative? Remember it but we need to swap the sign
		boolean negative;
		if ((negative = (__v < 0)))
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
			
			// Print character
			sb.append((char)(mod < 10 ? '0' + mod : 'a' + (mod - 10)));
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
	 * Calls {@link Long#toStirng(long, int)} with a radix of 10.
	 *
	 * @param __v The input value.
	 * @return The resulting string.
	 * @since 2018/09/23
	 */
	public static String toString(long __v)
	{
		return Long.toString(__v, 10);
	}
	
	public static Long valueOf(String __a, int __b)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	public static Long valueOf(String __a)
		throws NumberFormatException
	{
		if (false)
			throw new NumberFormatException();
		throw new todo.TODO();
	}
	
	/**
	 * Returns a instance of the given value, this may be cached.
	 *
	 * @param __v The value to box.
	 * @return The boxed value.
	 * @since 2018/09/23
	 */
	@ImplementationNote("This is not cached.")
	public static Long valueOf(long __v)
	{
		return new Long(__v);
	}
}


