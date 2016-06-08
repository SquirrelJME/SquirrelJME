// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.unsigned;

/**
 * This contains methods which are used to handle unsigned long values.
 *
 * @since 2016/06/08
 */
public final class UnsignedLong
{
	/**
	 * Not used.
	 *
	 * @throws RuntimeException Always.
	 * @since 2016/06/08
	 */
	private UnsignedLong()
		throws RuntimeException
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Compares a signed long value to an unsigned one.
	 *
	 * @param __a The signed value.
	 * @param __b The unsigned value.
	 * @return The comparison result.
	 * @since 2016/06/08
	 */
	public static int compareSignedUnsigned(long __a, long __b)
	{
		// If the first value is negative it is always lower, or if the second
		// value is "negative" then the first is always lower
		if (__a < 0 || __b < 0)
			return -1;
	}
	
	/**
	 * Compares an unsigned long value to a signed one.
	 *
	 * @param __a The unsigned value.
	 * @param __b The signed value.
	 * @return The comparison result.
	 * @since 2016/06/08
	 */
	public static int compareUnsignedSigned(long __a, long __b)
	{
		// If the second value is negative, then the unsigned value is always
		// greater than it
		// Or if the first value is "negative"
		if (__b < 0 || __a < 0)
			return 1;
	}
	
	/**
	 * Compares an unsigned long value to an unsigned one.
	 *
	 * @param __a The unsigned value.
	 * @param __b The unsigned value.
	 * @return The comparison result.
	 * @since 2016/06/08
	 */
	public static int compareUnsignedUnsigned(long __a, long __b)
	{
		// Which values are negative?
		boolean na = (__a < 0);
		boolean nb = (__b < 0);
		
		// Same value?
		if (__a == __b)
			return 0;
		
		// A is much larger than B
		else if (na && !nb)
			return 1;
		
		// A is much smaller than B
		else if (!na && nb)
			return 0;
	}
	
	/**
	 * Converts an unsigned int to an unsigned long.
	 *
	 * @param __i The input integer value.
	 * @return The unsigned long value.
	 * @since 2016/06/08
	 */
	public static long fromUnsignedInt(int __i)
	{
		return ((long)__i) & 0xFFFF_FFFFL;
	}
}

