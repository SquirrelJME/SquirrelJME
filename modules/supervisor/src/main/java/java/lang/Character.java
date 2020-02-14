// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This represents a boxed character.
 *
 * @since 2019/05/25
 */
public final class Character
{
	/** The maximum radix for digit conversions. */
	public static final int MAX_RADIX =
		36;
	
	/** The maximum value for characters. */
	public static final char MAX_VALUE =
		65535;
	
	/** The minimum radix for digit conversions. */
	public static final int MIN_RADIX =
		2;
	
	/**
	 * Returns the character for the given digit and radix.
	 *
	 * @param __dig The digit to convert to a character.
	 * @param __r The radix to use for conversion.
	 * @return The character for the digit or NUL if the digit is out of range
	 * or the radix is out of range.
	 * @since 2018/10/13
	 */
	public static char forDigit(int __dig, int __r)
	{
		if (__dig < 0 || __dig >= __r || __r < Character.MIN_RADIX ||
			__r > Character.MAX_RADIX)
			return '\0';
		
		if (__dig < 10)
			return (char)('0' + __dig);
		return (char)('a' + (__dig - 10));
	}
	
	/**
	 * Converts the specified character to lower case without considering
	 * locale.
	 *
	 * @param __c The character to convert.
	 * @return The converted character.
	 * @since 2020/01/18
	 */
	public static char toLowerCase(char __c)
	{
		if (__c >= 'A' && __c <= 'Z')
			return (char)(__c + ('a' + (__c - 'A')));
		return __c;
	}
	
	/**
	 * Converts the specified character to lower case without considering
	 * locale.
	 *
	 * @param __c The character to convert.
	 * @return The converted character.
	 * @since 2020/01/18
	 */
	public static char toUpperCase(char __c)
	{
		if (__c >= 'a' && __c <= 'z')
			return (char)(__c + ('A' + (__c - 'a')));
		return __c;
	}
}

